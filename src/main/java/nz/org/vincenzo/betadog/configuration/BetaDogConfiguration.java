package nz.org.vincenzo.betadog.configuration;

import com.google.gson.Gson;
import nz.org.vincenzo.betadog.service.ScrapeService;
import nz.org.vincenzo.betadog.service.impl.ScrapeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration.
 *
 * @author Rey Vincent Babilonia
 */
@Configuration
public class BetaDogConfiguration {

    /**
     * Returns the {@link Gson}.
     *
     * @return the {@link Gson}
     */
    @Bean
    public Gson gson() {
        return new Gson();
    }

    /**
     * Returns the {@link ScrapeService}.
     *
     * @return the {@link ScrapeService}
     */
    @Bean
    public ScrapeService scrapeService() {
        return new ScrapeServiceImpl();
    }
}
