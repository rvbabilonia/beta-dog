package nz.org.vincenzo.betadog.service.impl;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlAdapter;
import nz.org.vincenzo.betadog.domain.Instrument;
import nz.org.vincenzo.betadog.domain.MainBoard;
import nz.org.vincenzo.betadog.enumeration.InstrumentFilter;
import nz.org.vincenzo.betadog.enumeration.InstrumentType;
import nz.org.vincenzo.betadog.enumeration.SortOrder;
import nz.org.vincenzo.betadog.service.ScrapeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapeServiceImpl.class);

    private static final String MARKETS = "/markets/NZSX";

    private static final String INSTRUMENTS = "/instruments/";

    private static final List<String> UNITS_IN_EQUITY = List.of("GMT", "VHP");

    private static final List<String> UNITS_IN_FUND = List.of("CO2", "FSF", "PLP", "SRF");

    private static final List<InstrumentType> EQUITIES = List.of(InstrumentType.ORDINARY_SHARES,
            InstrumentType.CUMULATIVE_PREFERENCE_SHARES, InstrumentType.OPTION, InstrumentType.CONVERTIBLE_NOTES,
            InstrumentType.EQUITY_WARRANTS);

    private final HtmlToPojoEngine engine;

    private final WebClient webClient;

    /**
     * Default constructor.
     *
     * @param engine    the {@link HtmlToPojoEngine}
     * @param webClient the {@link WebClient}
     */
    @Autowired
    public ScrapeServiceImpl(HtmlToPojoEngine engine, WebClient webClient) {
        this.engine = engine;
        this.webClient = webClient;
    }

    @Cacheable("mainBoard")
    @Override
    public MainBoard getMainBoard() {
        Mono<String> mono = webClient
                .get()
                .uri(MARKETS)
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .bodyToMono(String.class);

        AtomicReference<String> html = new AtomicReference<>();
        Disposable disposable = mono.subscribe(html::set);

        int i = 0;
        do {
            i++;
            if (i == 5) {
                html.set("");
            }
            try {
                LOGGER.info("Waiting for [NZSX]...");
                Thread.sleep(200);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        } while (html.get() == null);

        disposable.dispose();

        if (!html.get().isBlank()) {
            HtmlAdapter<MainBoard> adapter = engine.adapter(MainBoard.class);

            return adapter.fromHtml(html.get());
        } else {
            return new MainBoard();
        }
    }

    @Cacheable("instruments")
    @Override
    public Instrument getInstrument(final String code) {
        Mono<String> mono = webClient
                .get()
                .uri(INSTRUMENTS + code)
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .bodyToMono(String.class);

        AtomicReference<String> html = new AtomicReference<>();
        Disposable disposable = mono.subscribe(html::set);

        int i = 0;
        do {
            i++;
            if (i == 5) {
                html.set("");
            }
            try {
                LOGGER.info("Waiting for [{}]...", code);
                Thread.sleep(200);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        } while (html.get() == null);

        disposable.dispose();

        if (!html.get().isBlank()) {
            HtmlAdapter<Instrument> adapter = engine.adapter(Instrument.class);

            return adapter.fromHtml(html.get());
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
                .collect(Collectors.toCollection(LinkedList::new));

        if (InstrumentFilter.EQUITIES == instrumentFilter) {
            instruments = instruments
                    .stream()
                    .filter(instrument -> {
                        InstrumentType instrumentType =
                                InstrumentType.getInstrumentType(instrument.getSnapshot().getInstrumentType());
                        return EQUITIES.contains(instrumentType)
                               || UNITS_IN_EQUITY.contains(instrument.getSnapshot().getCode());
                    })
                    .collect(Collectors.toCollection(LinkedList::new));
        } else if (InstrumentFilter.FUNDS == instrumentFilter) {
            instruments = instruments
                    .stream()
                    .filter(instrument -> {
                        InstrumentType instrumentType =
                                InstrumentType.getInstrumentType(instrument.getSnapshot().getInstrumentType());
                        return InstrumentType.FUND == instrumentType
                               || UNITS_IN_FUND.contains(instrument.getSnapshot().getCode());
                    })
                    .collect(Collectors.toCollection(LinkedList::new));
        }

        if (SortOrder.PE_RATIO == sortOrder) {
            // the lower the better
            instruments = instruments
                    .stream()
                    .sorted(Comparator.comparingDouble(instrument -> instrument.getFundamental().getPriceToEarningsRatio()))
                    .filter(instrument -> instrument.getFundamental().getPriceToEarningsRatio() > 0)
                    .collect(Collectors.toList());
        } else if (SortOrder.EPS == sortOrder) {
            // the higher the better
            instruments = instruments
                    .stream()
                    .sorted(Comparator.comparingDouble(instrument -> instrument.getFundamental().getEarningsPerShare()))
                    .filter(instrument -> instrument.getFundamental().getEarningsPerShare() > 0)
                    .collect(Collectors.toList());
            Collections.reverse(instruments);
        } else {
            instruments.sort(Comparator.comparing(instrument -> instrument.getSnapshot().getCode()));
        }

        return instruments;
    }
}
