package org.vincenzolabs.betadog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vincenzolabs.betadog.domain.Instrument;
import org.vincenzolabs.betadog.enumeration.InstrumentFilter;
import org.vincenzolabs.betadog.enumeration.SortOrder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The integration test case for {@link ScrapeService}.
 *
 * @author Rey Vincent Babilonia
 */
@SpringBootTest
class ScrapeServiceIntegrationTest {

    @Autowired
    private ScrapeService scrapeService;

    @Test
    void getMainBoard() {
        // 181 as of 10 March 2020
        assertThat(scrapeService.getMainBoard().getSecurities()).hasSize(181);
    }

    @Test
    void getInstrument() {
        Instrument instrument = scrapeService.getInstrument("AIR");

        assertThat(instrument).isNotNull();
        assertThat(instrument.getSnapshot())
                .extracting("code", "company", "instrumentType")
                .containsExactly("AIR", "Air New Zealand Limited (NS)", "Ordinary Shares");
    }


    @Test
    void getInstruments() {
        List<Instrument> instruments = scrapeService.getInstruments(InstrumentFilter.ALL, SortOrder.DEFAULT);

        // 181 as of 10 March 2020
        // ABA is first as of 10 March 2020
        assertThat(instruments)
                .hasSize(181)
                .first()
                .extracting("snapshot.code", "snapshot.company", "snapshot.instrumentType")
                .containsExactly("ABA", "Abano Healthcare Group Limited", "Ordinary Shares");
    }

    @Test
    void getEquityInstrumentsByPERatio() {
        List<Instrument> instruments = scrapeService.getInstruments(InstrumentFilter.EQUITIES, SortOrder.PE_RATIO);

        // 145 equities but only 100 have positive P/E ratio as of 10 March 2020
        assertThat(instruments).hasSize(100);
    }

    @Test
    void getFundInstrumentsByEPS() {
        List<Instrument> instruments = scrapeService.getInstruments(InstrumentFilter.FUNDS, SortOrder.EPS);

        // 35 funds but only 21 have positive EPS as of 10 March 2020
        assertThat(instruments).hasSize(21);
    }
}
