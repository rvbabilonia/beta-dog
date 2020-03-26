package org.vincenzolabs.betadog.domain;

import fr.whimtrip.ext.jwhthtmltopojo.annotation.ReplaceWith;
import fr.whimtrip.ext.jwhthtmltopojo.annotation.Selector;
import fr.whimtrip.ext.jwhthtmltopojo.impl.ReplacerDeserializer;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Objects;

/**
 * The domain model object of the instrument.
 *
 * @author Rey Vincent Babilonia
 */
public class Instrument {

    @Selector(value = "section.instrument-snapshot > div.row")
    private Snapshot snapshot;

    @Selector(value = "div.small-12.medium-9.columns.content > div:nth-child(2) > div:nth-child(2)")
    private Performance performance;

    @Selector(value = "div.small-12.medium-9.columns.content > div:nth-child(2) > div:nth-child(3)")
    private Fundamental fundamental;

    /**
     * Returns the {@link Snapshot}.
     *
     * @return the {@link Snapshot}
     */
    public Snapshot getSnapshot() {
        return snapshot;
    }

    /**
     * Sets the {@link Snapshot}.
     *
     * @param snapshot the {@link Snapshot}
     */
    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    /**
     * Returns the {@link Performance}.
     *
     * @return the {@link Performance}
     */
    public Performance getPerformance() {
        return performance;
    }

    /**
     * Sets the {@link Performance}.
     *
     * @param performance the {@link Performance}
     */
    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    /**
     * Returns the {@link Fundamental}.
     *
     * @return the {@link Fundamental}
     */
    public Fundamental getFundamental() {
        return fundamental;
    }

    /**
     * Sets the {@link Fundamental}.
     *
     * @param fundamental the {@link Fundamental}
     */
    public void setFundamental(Fundamental fundamental) {
        this.fundamental = fundamental;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Instrument that = (Instrument) o;
        return Objects.equals(snapshot, that.snapshot)
                && Objects.equals(performance, that.performance)
                && Objects.equals(fundamental, that.fundamental);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snapshot, performance, fundamental);
    }

    @Override
    public String toString() {
        return "Instrument{"
                + "snapshot=" + snapshot
                + ", performance=" + performance
                + ", fundamental=" + fundamental
                + '}';
    }

    /**
     * The instrument snapshot.
     */
    public static class Snapshot {
        @Selector(value = "div.small-12.medium-5.columns > h2")
        private String code;

        @Selector(value = "div.small-12.medium-5.columns > h1",
                useDeserializer = true,
                preConvert = true,
                deserializer = ReplacerDeserializer.class)
        @ReplaceWith(value = "\\$", with = "")
        private Double price;

        @Selector(value = "div.small-12.medium-5.columns > span")
        private String dayChange;

        @Selector(value = "div.small-12.medium-5.columns > small > span")
        private String yearChange;

        @Selector(value = "div.small-12.medium-7.columns > table > tbody > tr:nth-child(3) > td:nth-child(2) > a")
        private String company;

        @Selector(value = "div.small-12.medium-7.columns > table > tbody > tr:nth-child(7) > td:nth-child(2)")
        private String instrumentType;

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
         * Returns the price.
         *
         * @return the price
         */
        public Double getPrice() {
            return price;
        }

        /**
         * Sets the price.
         *
         * @param price the price
         */
        public void setPrice(Double price) {
            this.price = price;
        }

        /**
         * Returns the day change.
         *
         * @return the day change
         */
        public String getDayChange() {
            return dayChange;
        }

        /**
         * Sets the day change.
         *
         * @param dayChange the day change
         */
        public void setDayChange(String dayChange) {
            this.dayChange = dayChange;
        }

        /**
         * Returns the 52-week change.
         *
         * @return the 52-week change
         */
        public String getYearChange() {
            return yearChange;
        }

        /**
         * Sets the 52-week change.
         *
         * @param yearChange the 52-week change
         */
        public void setYearChange(String yearChange) {
            this.yearChange = yearChange;
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

        /**
         * Returns the instrument type.
         *
         * @return the instrument type
         */
        public String getInstrumentType() {
            return instrumentType;
        }

        /**
         * Sets the instrument type.
         *
         * @param instrumentType the instrument type
         */
        public void setInstrumentType(String instrumentType) {
            this.instrumentType = instrumentType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Snapshot snapshot = (Snapshot) o;
            return Objects.equals(code, snapshot.code)
                    && Objects.equals(price, snapshot.price)
                    && Objects.equals(dayChange, snapshot.dayChange)
                    && Objects.equals(yearChange, snapshot.yearChange)
                    && Objects.equals(company, snapshot.company)
                    && Objects.equals(instrumentType, snapshot.instrumentType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, price, dayChange, yearChange, company, instrumentType);
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }
    }

    /**
     * The instrument information on performance.
     */
    public static class Performance {
        @Selector(value = "tr:nth-child(1) > td:nth-child(2)",
                useDeserializer = true,
                preConvert = true,
                deserializer = ReplacerDeserializer.class)
        @ReplaceWith(value = "\\$", with = "")
        private Double open;

        @Selector(value = "tr:nth-child(2) > td:nth-child(2)",
                useDeserializer = true,
                preConvert = true,
                deserializer = ReplacerDeserializer.class)
        @ReplaceWith(value = "\\$", with = "")
        private Double high;

        @Selector(value = "tr:nth-child(3) > td:nth-child(2)",
                useDeserializer = true,
                preConvert = true,
                deserializer = ReplacerDeserializer.class)
        @ReplaceWith(value = "\\$", with = "")
        private Double low;

        @Selector(value = "tr:nth-child(4) > td:nth-child(2)",
                useDeserializer = true,
                preConvert = true,
                deserializer = ReplacerDeserializer.class)
        @ReplaceWith(value = "\\$", with = "")
        private Double highBid;

        @Selector(value = "tr:nth-child(5) > td:nth-child(2)",
                useDeserializer = true,
                preConvert = true,
                deserializer = ReplacerDeserializer.class)
        @ReplaceWith(value = "\\$", with = "")
        private Double lowOffer;

        /**
         * Returns the opening price.
         *
         * @return the opening price
         */
        public Double getOpen() {
            return open;
        }

        /**
         * Sets the opening price.
         *
         * @param open the opening price
         */
        public void setOpen(Double open) {
            this.open = open;
        }

        /**
         * Returns the highest sale.
         *
         * @return the highest sale
         */
        public Double getHigh() {
            return high;
        }

        /**
         * Sets the highest sale.
         *
         * @param high the highest sale
         */
        public void setHigh(Double high) {
            this.high = high;
        }

        /**
         * Returns the lowest sale.
         *
         * @return the lowest sale
         */
        public Double getLow() {
            return low;
        }

        /**
         * Sets the lowest sale.
         *
         * @param low the lowest sale
         */
        public void setLow(Double low) {
            this.low = low;
        }

        /**
         * Returns the highest/best bid.
         *
         * @return the highest/best bid
         */
        public Double getHighBid() {
            return highBid;
        }

        /**
         * Sets the highest/best bid.
         *
         * @param highBid the highest/best bid
         */
        public void setHighBid(Double highBid) {
            this.highBid = highBid;
        }

        /**
         * Returns the lowest offer.
         *
         * @return the lowest offer
         */
        public Double getLowOffer() {
            return lowOffer;
        }

        /**
         * Sets the lowest offer.
         *
         * @param lowOffer the lowest offer
         */
        public void setLowOffer(Double lowOffer) {
            this.lowOffer = lowOffer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Performance that = (Performance) o;
            return Objects.equals(open, that.open)
                    && Objects.equals(high, that.high)
                    && Objects.equals(low, that.low)
                    && Objects.equals(highBid, that.highBid)
                    && Objects.equals(lowOffer, that.lowOffer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(open, high, low, highBid, lowOffer);
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }
    }

    /**
     * The instrument information on fundamentals.
     */
    public static class Fundamental {
        @Selector(value = "tr:nth-child(1) > td:nth-child(2)")
        private Double priceToEarningsRatio;

        @Selector(value = "tr:nth-child(2) > td:nth-child(2)",
                useDeserializer = true,
                preConvert = true,
                deserializer = ReplacerDeserializer.class)
        @ReplaceWith(value = "\\$", with = "")
        private Double earningsPerShare;

        @Selector(value = "tr:nth-child(3) > td:nth-child(2)",
                useDeserializer = true,
                preConvert = true,
                deserializer = ReplacerDeserializer.class)
        @ReplaceWith(value = "\\$", with = "")
        private Double netTangibleAssetsPerShare;

        @Selector(value = "tr:nth-child(4) > td:nth-child(2)",
                useDeserializer = true,
                preConvert = true,
                deserializer = ReplacerDeserializer.class)
        @ReplaceWith(value = "%", with = "")
        private Double grossDividendYield;

        /**
         * Returns the price-to-earnings ratio (P/E) in dollars.
         *
         * @return the price-to-earnings ratio (P/E) in dollars
         */
        public Double getPriceToEarningsRatio() {
            return priceToEarningsRatio;
        }

        /**
         * Sets the price-to-earnings ratio (P/E) in dollars.
         *
         * @param priceToEarningsRatio the price-to-earnings ratio (P/E) in dollars
         */
        public void setPriceToEarningsRatio(Double priceToEarningsRatio) {
            this.priceToEarningsRatio = priceToEarningsRatio;
        }

        /**
         * Returns the earnings per share (EPS) in dollars.
         *
         * @return the earnings per share (EPS) in dollars
         */
        public Double getEarningsPerShare() {
            return earningsPerShare;
        }

        /**
         * Sets the earnings per share (EPS) in dollars.
         *
         * @param earningsPerShare the earnings per share (EPS) in dollars
         */
        public void setEarningsPerShare(Double earningsPerShare) {
            this.earningsPerShare = earningsPerShare;
        }

        /**
         * Returns the net tangible assets (NTA) per share in dollars.
         *
         * @return the net tangible assets (NTA) per share in dollars
         */
        public Double getNetTangibleAssetsPerShare() {
            return netTangibleAssetsPerShare;
        }

        /**
         * Sets the net tangible assets (NTA) per share in dollars.
         *
         * @param netTangibleAssetsPerShare the net tangible assets (NTA) per share in dollars
         */
        public void setNetTangibleAssetsPerShare(Double netTangibleAssetsPerShare) {
            this.netTangibleAssetsPerShare = netTangibleAssetsPerShare;
        }

        /**
         * Returns the gross dividend yield in percentage.
         *
         * @return the gross dividend yield in percentage
         */
        public Double getGrossDividendYield() {
            return grossDividendYield;
        }

        /**
         * Sets the gross dividend yield in percentage.
         *
         * @param grossDividendYield the gross dividend yield in percentage
         */
        public void setGrossDividendYield(Double grossDividendYield) {
            this.grossDividendYield = grossDividendYield;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Fundamental that = (Fundamental) o;
            return Objects.equals(priceToEarningsRatio, that.priceToEarningsRatio)
                    && Objects.equals(earningsPerShare, that.earningsPerShare)
                    && Objects.equals(netTangibleAssetsPerShare, that.netTangibleAssetsPerShare)
                    && Objects.equals(grossDividendYield, that.grossDividendYield);
        }

        @Override
        public int hashCode() {
            return Objects.hash(priceToEarningsRatio, earningsPerShare, netTangibleAssetsPerShare, grossDividendYield);
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }
    }
}
