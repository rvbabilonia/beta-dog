package nz.org.vincenzo.betadog.domain;

import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Objects;

/**
 * The domain model object for the security.
 *
 * @author Rey Vincent Babilonia
 */
public class Security {

    @Selector(value = "td:nth-child(1) > a > strong")
    private String code;

    @Selector(value = "td:nth-child(2) > span > a")
    private String company;

    /**
     * Returns the code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code.
     *
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Returns the company.
     *
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * Sets the company.
     *
     * @param company the company
     */
    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Security security = (Security) o;
        return Objects.equals(code, security.code)
                && Objects.equals(company, security.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, company);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
