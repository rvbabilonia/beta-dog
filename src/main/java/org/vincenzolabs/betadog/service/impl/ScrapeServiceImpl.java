package org.vincenzolabs.betadog.service.impl;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlAdapter;
import org.vincenzolabs.betadog.domain.Instrument;
import org.vincenzolabs.betadog.domain.MainBoard;
import org.vincenzolabs.betadog.enumeration.InstrumentFilter;
import org.vincenzolabs.betadog.enumeration.InstrumentType;
import org.vincenzolabs.betadog.enumeration.SortOrder;
import org.vincenzolabs.betadog.service.ScrapeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The implementation of {@link ScrapeService}.
 *
 * @author Rey Vincent Babilonia
 */
@Service
@CacheConfig(cacheNames = {"mainBoard", "instruments"})
public class ScrapeServiceImpl
        implements ScrapeService {

    private static final String MARKETS = "/markets/NZSX";

    private static final String INSTRUMENTS = "/instruments/";

    private static final List<String> UNITS_IN_EQUITY = List.of("GMT", "VHP");

    private static final List<String> UNITS_IN_FUND = List.of("CO2", "FSF", "PLP", "SRF");

    private static final List<InstrumentType> EQUITIES = List.of(InstrumentType.ORDINARY_SHARES,
            InstrumentType.CUMULATIVE_PREFERENCE_SHARES, InstrumentType.OPTION, InstrumentType.CONVERTIBLE_NOTES,
            InstrumentType.EQUITY_WARRANTS, InstrumentType.TRADEABLE_RIGHTS);

    private final HtmlToPojoEngine engine;

    private final RestTemplate restTemplate;

    /**
     * Default constructor.
     *
     * @param engine       the {@link HtmlToPojoEngine}
     * @param restTemplate the {@link RestTemplate}
     */
    @Autowired
    public ScrapeServiceImpl(HtmlToPojoEngine engine, RestTemplate restTemplate) {
        this.engine = engine;
        this.restTemplate = restTemplate;
    }

    @Cacheable("mainBoard")
    @Override
    public MainBoard getMainBoard() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://www.nzx.com" + MARKETS, String.class);

        if (HttpStatus.OK == responseEntity.getStatusCode() && StringUtils.isNotBlank(responseEntity.getBody())) {
            HtmlAdapter<MainBoard> adapter = engine.adapter(MainBoard.class);

            return adapter.fromHtml(responseEntity.getBody());
        } else {
            return new MainBoard();
        }
    }

    @Cacheable("instruments")
    @Override
    public Instrument getInstrument(final String code) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://www.nzx.com" + INSTRUMENTS + code,
                String.class);

        if (HttpStatus.OK == responseEntity.getStatusCode() && StringUtils.isNotBlank(responseEntity.getBody())) {
            HtmlAdapter<Instrument> adapter = engine.adapter(Instrument.class);

            return adapter.fromHtml(responseEntity.getBody());
        } else {
            Instrument.Snapshot snapshot = new Instrument.Snapshot();
            snapshot.setCode(code);
            snapshot.setInstrumentType(InstrumentType.UNKNOWN.getDescription());

            Instrument instrument = new Instrument();
            instrument.setSnapshot(snapshot);

            return instrument;
        }
    }

    @Override
    public List<Instrument> getInstruments(InstrumentFilter instrumentFilter, SortOrder sortOrder) {
        MainBoard mainBoard = getMainBoard();

        List<Instrument> instruments = mainBoard
                .getSecurities()
                .parallelStream()
                .map(security -> getInstrument(security.getCode()))
                .collect(Collectors.toList());

        if (InstrumentFilter.EQUITIES == instrumentFilter) {
            instruments = instruments
                    .stream()
                    .filter(instrument -> {
                        InstrumentType instrumentType =
                                InstrumentType.getInstrumentType(instrument.getSnapshot().getInstrumentType());
                        return EQUITIES.contains(instrumentType)
                               || UNITS_IN_EQUITY.contains(instrument.getSnapshot().getCode());
                    })
                    .collect(Collectors.toList());
        } else if (InstrumentFilter.FUNDS == instrumentFilter) {
            instruments = instruments
                    .stream()
                    .filter(instrument -> {
                        InstrumentType instrumentType =
                                InstrumentType.getInstrumentType(instrument.getSnapshot().getInstrumentType());
                        return InstrumentType.FUND == instrumentType
                               || UNITS_IN_FUND.contains(instrument.getSnapshot().getCode());
                    })
                    .collect(Collectors.toList());
        }

        if (SortOrder.PE_RATIO == sortOrder) {
            // the lower the better
            instruments = instruments
                    .stream()
                    .sorted(Comparator.comparingDouble(instrument -> instrument.getFundamental().getPriceToEarningsRatio()))
                    .filter(instrument -> instrument.getFundamental().getPriceToEarningsRatio() > 0)
                    .collect(Collectors.toCollection(LinkedList::new));
        } else if (SortOrder.EPS == sortOrder) {
            // the higher the better
            instruments = instruments
                    .stream()
                    .sorted(Comparator.comparingDouble(instrument -> instrument.getFundamental().getEarningsPerShare()))
                    .filter(instrument -> instrument.getFundamental().getEarningsPerShare() > 0)
                    .collect(Collectors.toCollection(LinkedList::new));
            Collections.reverse(instruments);
        } else {
            instruments.sort(Comparator.comparing(instrument -> instrument.getSnapshot().getCode()));
        }

        return instruments;
    }
}
