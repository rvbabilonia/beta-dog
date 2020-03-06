package nz.org.vincenzo.betadog.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import nz.org.vincenzo.betadog.configuration.BetaDogConfiguration;
import nz.org.vincenzo.betadog.domain.Instrument;
import nz.org.vincenzo.betadog.enumeration.InstrumentFilter;
import nz.org.vincenzo.betadog.enumeration.SortOrder;
import nz.org.vincenzo.betadog.service.ScrapeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * The {@link RequestHandler} for scraping NZ stock market.
 *
 * @author Rey Vincent Babilonia
 */
@Component
public class ScrapeRequestHandler
        implements RequestHandler<Request, Response> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapeRequestHandler.class);

    private static final ApplicationContext APPLICATION_CONTEXT =
            new AnnotationConfigApplicationContext(BetaDogConfiguration.class);

    @Override
    public Response handleRequest(Request request, Context context) {
        ScrapeService scrapeService = APPLICATION_CONTEXT.getBean(ScrapeService.class);
        Gson gson = APPLICATION_CONTEXT.getBean(Gson.class);

        Response response = new Response();
        try {
            Map<String, String> queryParameters = request.getQueryStringParameters();
            String filter = queryParameters.getOrDefault("filter", "ALL");
            String order = queryParameters.getOrDefault("order", "DEFAULT");

            InstrumentFilter instrumentFilter = InstrumentFilter.valueOf(filter.toUpperCase());
            SortOrder sortOrder = SortOrder.valueOf(order.toUpperCase());

            List<Instrument> instruments = scrapeService.getInstruments(instrumentFilter, sortOrder);

            response.setStatusCode(200);
            response.setBody(gson.toJson(instruments));
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);

            response.setStatusCode(400);
            response.setBody(String.format(Response.ERROR_MESSAGE, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            response.setStatusCode(500);
            response.setBody(String.format(Response.ERROR_MESSAGE, e.getMessage()));
        }

        return response;
    }
}
