package nz.org.vincenzo.betadog.domain;

import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;
import java.util.Objects;

/**
 * The main board of the NZ stock exchange. This will include equities and funds.
 *
 * @author Rey Vincent Babilonia
 */
public class MainBoard {

    @Selector("tbody > tr")
    List<Security> securities;

    /**
     * Returns the {@link List} of {@link Security}.
     *
     * @return the {@link List} of {@link Security}
     */
    public List<Security> getSecurities() {
        return securities;
    }

    /**
     * Sets the {@link List} of {@link Security}.
     *
     * @param securities the {@link List} of {@link Security}
     */
    public void setSecurities(List<Security> securities) {
        this.securities = securities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MainBoard mainBoard = (MainBoard) o;
        return Objects.equals(securities, mainBoard.securities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(securities);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
