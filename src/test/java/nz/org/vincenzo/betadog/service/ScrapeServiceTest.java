package nz.org.vincenzo.betadog.service;

import nz.org.vincenzo.betadog.domain.Instrument;
import nz.org.vincenzo.betadog.enumeration.InstrumentFilter;
import nz.org.vincenzo.betadog.enumeration.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The integration test case for {@link ScrapeService}.
 *
 * @author Rey Vincent Babilonia
 */
@SpringBootTest
@ActiveProfiles("test")
class ScrapeServiceTest {

    @Autowired
    private ScrapeService scrapeService;

    @Test
    void getMainBoard() {
        // 180 as of 3 March 2020
        assertThat(scrapeService.getMainBoard().getSecurities()).hasSize(180);
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

        // 180 as of 3 March 2020
        // ABA is first as of 3 March 2020
        assertThat(instruments)
                .hasSize(180)
                .first()
                .extracting("snapshot.code", "snapshot.company", "snapshot.instrumentType")
                .containsExactly("ABA", "Abano Healthcare Group Limited", "Ordinary Shares");
    }

    @Test
    void getEquityInstrumentsByPERatio() {
        List<Instrument> instruments = scrapeService.getInstruments(InstrumentFilter.EQUITIES, SortOrder.PE_RATIO);

        // 145 as of 3 March 2020
        assertThat(instruments).hasSize(145);
    }

    @Test
    void getFundInstrumentsByEPS() {
        List<Instrument> instruments = scrapeService.getInstruments(InstrumentFilter.FUNDS, SortOrder.EPS);

        // 35 as of 3 March 2020
        assertThat(instruments).hasSize(35);
    }
}
