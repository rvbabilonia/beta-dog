package org.vincenzolabs.betadog.service;

import org.vincenzolabs.betadog.domain.Instrument;
import org.vincenzolabs.betadog.domain.MainBoard;
import org.vincenzolabs.betadog.domain.Security;
import org.vincenzolabs.betadog.enumeration.InstrumentFilter;
import org.vincenzolabs.betadog.enumeration.SortOrder;
import reactor.core.publisher.Flux;

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
     * Returns the {@link Flux} of {@link Instrument}s.
     *
     * @param instrumentFilter the {@link InstrumentFilter}
     * @param sortOrder        the {@link SortOrder}
     * @return the {@link Flux} of {@link Instrument}s
     */
    List<Instrument> getInstruments(InstrumentFilter instrumentFilter, SortOrder sortOrder);
}
