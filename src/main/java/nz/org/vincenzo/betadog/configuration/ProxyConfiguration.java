package nz.org.vincenzo.betadog.configuration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import reactor.netty.tcp.ProxyProvider;

/**
 * The proxy configuration.
 *
 * @author Rey Vincent Babilonia
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("proxy")
public class ProxyConfiguration {

    private boolean enabled;

    private ProxyProvider.Proxy type;

    private String hostname;

    private int port;

    /**
     * Checks if the proxy is enabled.
     *
     * @return {@code true} if the proxy is enabled; {@code false} otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enables the proxy.
     *
     * @param enabled {@code true} if the proxy is enabled; {@code false} otherwise
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns the {@link ProxyProvider.Proxy}.
     *
     * @return the {@link ProxyProvider.Proxy}
     */
    public ProxyProvider.Proxy getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the type
     */
    public void setType(ProxyProvider.Proxy type) {
        this.type = type;
    }

    /**
     * Returns the hostname.
     *
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Sets the hostname.
     *
     * @param hostname the hostname
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Returns the port number.
     *
     * @return the port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the port.
     *
     * @param port the port
     */
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProxyConfiguration that = (ProxyConfiguration) o;

        return new EqualsBuilder()
                .append(enabled, that.enabled)
                .append(port, that.port)
                .append(type, that.type)
                .append(hostname, that.hostname)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(enabled)
                .append(type)
                .append(hostname)
                .append(port)
                .toHashCode();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
