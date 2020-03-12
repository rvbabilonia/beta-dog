package nz.org.vincenzo.betadog;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * The beta dog application.
 *
 * @author Rey Vincent Babilonia
 */
@SpringBootApplication
@EnableWebFlux
@EnableCaching
public class BetaDogApplication {

    /**
     * Main application.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BetaDogApplication.class, args);
    }

    /**
     * Returns the {@link RestTemplate}.
     *
     * @return the {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Returns the {@link HtmlToPojoEngine}.
     *
     * @return the {@link HtmlToPojoEngine}
     */
    @Bean
    public HtmlToPojoEngine engine() {
        return HtmlToPojoEngine.create();
    }
}
