package nz.org.vincenzo.betadog.service;

import nz.org.vincenzo.betadog.domain.Instrument;
import nz.org.vincenzo.betadog.domain.MainBoard;
import nz.org.vincenzo.betadog.domain.Security;
import nz.org.vincenzo.betadog.enumeration.InstrumentFilter;
import nz.org.vincenzo.betadog.enumeration.SortOrder;

import java.util.List;

/**
 * The service for scraping the NZ stock exchange.
 *
 * @author Rey Vincent Babilonia
 */
public interface ScrapeService {

    /**
     * Generates the {@link MainBoard} from https://www.nzx.com/markets/NZSX.
     *
     * @return the {@link MainBoard}
     */
    MainBoard getMainBoard();

    /**
     * Generates the {@link Instrument} for the given security code from https://www.nzx.com/instruments/.
     *
     * @param code the {@link Security}'s code
     * @return the {@link Instrument}
     */
    Instrument getInstrument(String code);

    /**
     * Returns the {@link List} of {@link Instrument}s.
     *
     * @param instrumentFilter the {@link InstrumentFilter}
     * @param sortOrder        the {@link SortOrder}
     * @return the {@link List} of {@link Instrument}s
     */
    List<Instrument> getInstruments(InstrumentFilter instrumentFilter, SortOrder sortOrder);
}
