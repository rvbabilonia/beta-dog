package nz.org.vincenzo.betadog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.org.vincenzo.betadog.configuration.ProxyConfiguration;
import nz.org.vincenzo.betadog.domain.Instrument;
import nz.org.vincenzo.betadog.enumeration.InstrumentFilter;
import nz.org.vincenzo.betadog.enumeration.SortOrder;
import nz.org.vincenzo.betadog.service.ScrapeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * The test case for {@link ScrapeController}.
 *
 * @author Rey Vincent Babilonia
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(ScrapeController.class)
@Import(ScrapeService.class)
class ScrapeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScrapeService scrapeService;

    @MockBean
    private ProxyConfiguration proxyConfiguration;

    @Test
    void getInstruments() throws JsonProcessingException {
        Instrument.Snapshot airSnapshot = new Instrument.Snapshot();
        airSnapshot.setCode("AIR");
        airSnapshot.setCompany("Air New Zealand Limited (NS)");
        airSnapshot.setInstrumentType("Ordinary Shares");
        airSnapshot.setPrice(1.965);
        airSnapshot.setDayChange("-$0.095 / -4.61%");
        airSnapshot.setYearChange("-$0.453 / -19.74%");

        Instrument.Performance airPerformance = new Instrument.Performance();
        airPerformance.setOpen(2.060);
        airPerformance.setHigh(2.055);
        airPerformance.setLow(1.950);
        airPerformance.setHighBid(1.965);
        airPerformance.setLowOffer(1.970);

        Instrument.Fundamental airFundamental = new Instrument.Fundamental();
        airFundamental.setPriceToEarningsRatio(10.570);
        airFundamental.setEarningsPerShare(0.195);
        airFundamental.setNetTangibleAssetsPerShare(1.630);
        airFundamental.setGrossDividendYield(14.833);

        Instrument air = new Instrument();
        air.setSnapshot(airSnapshot);
        air.setPerformance(airPerformance);
        air.setFundamental(airFundamental);

        Instrument.Snapshot ampSnapshot = new Instrument.Snapshot();
        ampSnapshot.setCode("AMP");
        ampSnapshot.setCompany("AMP Limited");
        ampSnapshot.setInstrumentType("Ordinary Shares");
        ampSnapshot.setPrice(1.490);
        ampSnapshot.setDayChange("-$0.170 / -10.24%");
        ampSnapshot.setYearChange("-$0.664 / -34.12%");

        Instrument.Performance ampPerformance = new Instrument.Performance();
        ampPerformance.setOpen(1.660);
        ampPerformance.setHigh(1.640);
        ampPerformance.setLow(1.430);
        ampPerformance.setHighBid(1.420);
        ampPerformance.setLowOffer(1.500);

        Instrument.Fundamental ampFundamental = new Instrument.Fundamental();
        ampFundamental.setPriceToEarningsRatio(0.000);
        ampFundamental.setEarningsPerShare(-0.821);
        ampFundamental.setNetTangibleAssetsPerShare(1.217);
        ampFundamental.setGrossDividendYield(2.513);

        Instrument amp = new Instrument();
        amp.setSnapshot(ampSnapshot);
        amp.setPerformance(ampPerformance);
        amp.setFundamental(ampFundamental);

        when(scrapeService.getInstruments(InstrumentFilter.ALL, SortOrder.DEFAULT))
                .thenReturn(List.of(air, amp));

        webTestClient.get()
                     .uri("/api/v1/instruments")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody(String.class).isEqualTo(objectMapper.writeValueAsString(List.of(air, amp)));
    }
}
