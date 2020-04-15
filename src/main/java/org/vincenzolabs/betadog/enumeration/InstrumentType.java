package org.vincenzolabs.betadog.enumeration;

/**
 * The enumeration of instrument types.
 *
 * @author Rey Vincent Babilonia
 */
public enum InstrumentType {

    /**
     * Ordinary shares.
     */
    ORDINARY_SHARES("Ordinary Shares"),
    /**
     * Exchange-traded or index funds.
     */
    FUND("Index Fund"),
    /**
     * Units.
     */
    UNITS("Units"),
    /**
     * Convertible notes.
     */
    CONVERTIBLE_NOTES("Convertible Notes"),
    /**
     * Options.
     */
    OPTION("Option"),
    /**
     * Equity warrants.
     */
    EQUITY_WARRANTS("Equity Warrants"),
    /**
     * Cumulative preference shares.
     */
    CUMULATIVE_PREFERENCE_SHARES("Cumulative Preference Shares"),
    /**
     * Tradeable rights issued by cash-strapped companies.
     */
    TRADEABLE_RIGHTS("Tradeable Rights"),
    /**
     * Unknown.
     */
    UNKNOWN("Unknown");

    private final String description;

    /**
     * Default constructor.
     *
     * @param description the description
     */
    InstrumentType(String description) {
        this.description = description;
    }

    /**
     * Returns the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the {@link InstrumentType} matching the description.
     *
     * @param description the description
     * @return the {@link InstrumentType}
     */
    public static InstrumentType getInstrumentType(final String description) {
        for (InstrumentType instrumentType : values()) {
            if (instrumentType.getDescription().equalsIgnoreCase(description)) {
                return instrumentType;
            }
        }

        throw new IllegalArgumentException("Instrument type [" + description + "] is invalid");
    }
}
