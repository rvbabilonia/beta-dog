package nz.org.vincenzo.betadog.handler;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The response.
 *
 * @author Rey Vincent Babilonia
 */
public class Response {

    static final String ERROR_MESSAGE = "{\"errorType\":\"Exception\",\"errorMessage\":\"%s\"}";

    private int statusCode;

    private Map<String, String> headers = new HashMap<>();

    private String body;

    private boolean isBase64Encoded;

    /**
     * Returns the status code.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the status code.
     *
     * @param statusCode the status code
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Returns the {@link Map} of headers.
     *
     * @return the {@link Map} of headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets the {@link Map} of headers.
     *
     * @param headers the {@link Map} of headers
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Returns the body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the body.
     *
     * @param body the body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Returns the base-64 encoded flag.
     *
     * @return {@code true} if body is base-64 encoded; {@link false} otherwise
     */
    public boolean isBase64Encoded() {
        return isBase64Encoded;
    }

    /**
     * Sets the base-64 encoded flag.
     *
     * @param isBase64Encoded {@code true} if body is base-64 encoded; {@link false} otherwise
     */
    public void setBase64Encoded(boolean isBase64Encoded) {
        this.isBase64Encoded = isBase64Encoded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Response response = (Response) o;
        return statusCode == response.statusCode
                && isBase64Encoded == response.isBase64Encoded
                && Objects.equals(headers, response.headers)
                && Objects.equals(body, response.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, headers, body, isBase64Encoded);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
