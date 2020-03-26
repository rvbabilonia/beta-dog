package org.vincenzolabs.betadog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.vincenzolabs.betadog.domain.Instrument;
import org.vincenzolabs.betadog.enumeration.InstrumentFilter;
import org.vincenzolabs.betadog.enumeration.SortOrder;
import org.vincenzolabs.betadog.service.ScrapeService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The {@link RestController} for scraping NZ stock market.
 *
 * @author Rey Vincent Babilonia
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScrapeController {

    private final ScrapeService scrapeService;

    /**
     * Default constructor.
     *
     * @param scrapeService the {@link ScrapeService}
     */
    public ScrapeController(ScrapeService scrapeService) {
        this.scrapeService = scrapeService;
    }

    /**
     * Returns the {@link Flux} of {@link Instrument}s.
     *
     * @param filter the instrument filter
     * @param order  the sort order
     * @return the {@link ResponseEntity}
     */
    @GetMapping("/instruments")
    public ResponseEntity<Flux<Instrument>> getInstruments(
            @RequestParam(value = "filter", defaultValue = "ALL") String filter,
            @RequestParam(value = "order", defaultValue = "DEFAULT") String order) {
        InstrumentFilter instrumentFilter = InstrumentFilter.valueOf(filter.toUpperCase());
        SortOrder sortOrder = SortOrder.valueOf(order.toUpperCase());

        Flux<Instrument> instruments = Flux.fromStream(scrapeService.getInstruments(instrumentFilter, sortOrder).stream());

        return ResponseEntity.ok(instruments);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Mono<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(Mono.just("{\"message\":\"" + e.getMessage() + "\"}"));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Mono<String>> handleAllExceptions(Exception e) {
        return ResponseEntity.status(500)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(Mono.just("{\"message\":\"" + e.getMessage() + "\"}"));
    }
}
