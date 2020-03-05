package nz.org.vincenzo.betadog.service.impl;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import fr.whimtrip.ext.jwhthtmltopojo.intrf.HtmlAdapter;
import io.netty.channel.ChannelOption;
import nz.org.vincenzo.betadog.domain.Instrument;
import nz.org.vincenzo.betadog.domain.MainBoard;
import nz.org.vincenzo.betadog.enumeration.InstrumentFilter;
import nz.org.vincenzo.betadog.enumeration.InstrumentType;
import nz.org.vincenzo.betadog.enumeration.SortOrder;
import nz.org.vincenzo.betadog.service.ScrapeService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The implementation of {@link ScrapeService}.
 *
 * @author Rey Vincent Babilonia
 */
@Service
public class ScrapeServiceImpl
        implements ScrapeService {

    private static final String MARKETS = "https://www.nzx.com/markets/NZSX";

    private static final String INSTRUMENTS = "https://www.nzx.com/instruments/";

    private static final HtmlToPojoEngine ENGINE = HtmlToPojoEngine.create();

    private static final List<String> UNITS_IN_EQUITY = List.of("GMT", "VHP");

    private static final List<String> UNITS_IN_FUND = List.of("CO2", "FSF", "PLP", "SRF");

    private static final List<InstrumentType> EQUITIES = List.of(InstrumentType.ORDINARY_SHARES,
            InstrumentType.CUMULATIVE_PREFERENCE_SHARES, InstrumentType.OPTION, InstrumentType.CONVERTIBLE_NOTES,
            InstrumentType.EQUITY_WARRANTS);

    @Override
    public MainBoard getMainBoard() {
        Mono<ResponseEntity<String>> mono = getWebClient().get()
                .uri(MARKETS)
                .exchange()
                .timeout(Duration.ofSeconds(10))
                .flatMap(clientResponse -> clientResponse.toEntity(String.class));

        String html = Objects.requireNonNull(mono.single().block()).getBody();

        HtmlAdapter<MainBoard> adapter = ENGINE.adapter(MainBoard.class);

        return adapter.fromHtml(html);
    }

    @Override
    public Instrument getInstrument(final String code) {
        Mono<ResponseEntity<String>> mono = getWebClient().get()
                .uri(INSTRUMENTS + code)
                .exchange()
                .timeout(Duration.ofSeconds(10))
                .flatMap(clientResponse -> clientResponse.toEntity(String.class));

        String html = Objects.requireNonNull(mono.single().block()).getBody();

        HtmlAdapter<Instrument> adapter = ENGINE.adapter(Instrument.class);

        return adapter.fromHtml(html);
    }

    @Override
    public List<Instrument> getInstruments(InstrumentFilter instrumentFilter, SortOrder sortOrder) {
        MainBoard mainBoard = getMainBoard();

        List<Instrument> instruments = mainBoard.getSecurities().stream()
                .map(security -> getInstrument(security.getCode()))
                .collect(Collectors.toCollection(LinkedList::new));

        if (InstrumentFilter.EQUITIES == instrumentFilter) {
            instruments = instruments.stream()
                    .filter(o -> {
                        InstrumentType instrumentType =
                                InstrumentType.getInstrumentType(o.getSnapshot().getInstrumentType());
                        return EQUITIES.contains(instrumentType)
                                || UNITS_IN_EQUITY.contains(o.getSnapshot().getCode());
                    })
                    .collect(Collectors.toList());
        } else if (InstrumentFilter.FUNDS == instrumentFilter) {
            instruments = instruments.stream()
                    .filter(o -> {
                        InstrumentType instrumentType =
                                InstrumentType.getInstrumentType(o.getSnapshot().getInstrumentType());
                        return InstrumentType.FUND == instrumentType
                                || UNITS_IN_FUND.contains(o.getSnapshot().getCode());
                    })
                    .collect(Collectors.toList());
        }

        if (SortOrder.PE_RATIO == sortOrder) {
            instruments.sort(Comparator.comparingDouble(o -> o.getFundamental().getPriceToEarningsRatio()));
        } else if (SortOrder.EPS == sortOrder) {
            instruments.sort(Comparator.comparingDouble(o -> o.getFundamental().getEarningsPerShare()));
        } else {
            instruments.sort(Comparator.comparing(o -> o.getSnapshot().getCode()));
        }

        return instruments;
    }

    private static WebClient getWebClient() {
        HttpClient httpClient = HttpClient.create();
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return WebClient.builder().clientConnector(connector).build();
    }
}
