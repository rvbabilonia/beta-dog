package nz.org.vincenzo.betadog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * The beta dog application.
 *
 * @author Rey Vincent Babilonia
 */
@SpringBootApplication
@EnableWebFlux
public class BetaDogApplication {

    /**
     * Main application.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BetaDogApplication.class, args);
    }
}
