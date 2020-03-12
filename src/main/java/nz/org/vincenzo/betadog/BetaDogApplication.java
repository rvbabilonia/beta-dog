package nz.org.vincenzo.betadog;

import fr.whimtrip.ext.jwhthtmltopojo.HtmlToPojoEngine;
import io.netty.channel.ChannelOption;
import nz.org.vincenzo.betadog.configuration.ProxyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.TcpClient;

/**
 * The beta dog application.
 *
 * @author Rey Vincent Babilonia
 */
@SpringBootApplication
@EnableWebFlux
@EnableCaching
public class BetaDogApplication {

    @Autowired
    private ProxyConfiguration proxyConfiguration;

    /**
     * Main application.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BetaDogApplication.class, args);
    }

    /**
     * Returns a {@link WebClient}.
     *
     * @return the {@link WebClient}
     */
    @Bean
    public WebClient webClient() {
        TcpClient tcpClient = TcpClient.create(ConnectionProvider.newConnection())
                                       .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000);
        if (proxyConfiguration.isEnabled()) {
            tcpClient.proxy(proxy -> proxy.type(proxyConfiguration.getType())
                                          .host(proxyConfiguration.getHostname())
                                          .port(proxyConfiguration.getPort()));
        }

        return WebClient.builder()
                        .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient).wiretap(true).compress(true)))
                        .baseUrl("https://www.nzx.com")
                        .build();
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
