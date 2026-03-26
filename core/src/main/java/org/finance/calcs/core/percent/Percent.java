package org.finance.calcs.core.percent;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import org.finance.calcs.core.dataSerializing.PercentSerializer;
import org.finance.calcs.core.util.RoundingUtil;

/**
 * Represents the mathematical percent. Typically kept between 0.0 and 1.0. A decimal value of 0.5 would represent 50%.
 * This includes functions to perform basic calculations. <p>
 * Unless otherwise noted the phrase <strong>decimal value</strong> means the decimal representation of the percent
 * (0.5) and the <strong>percent value</strong> represents the percent string (50%). Rounding at the percent level means
 * more digits will be shown than rounding at the decimal level. For example the value 12.1234% or 0.121234 rounded to 2
 * positions will be 12.12% / 0.1212 if rounded at the percent level (2 digits after the percent decimal) but be
 * 12% / 0.12 if rounded at the decimal level.<p>
 * Serializer = {@link PercentSerializer}
 */
@EqualsAndHashCode
@JsonSerialize(using = PercentSerializer.Serializer.class)
@JsonDeserialize(using = PercentSerializer.Deserializer.class)
public class Percent implements Comparable<Percent> {

    public static final Percent ZERO_PERCENT = new Percent(0.0);

    public static final Percent ONE_HUNDRED_PERCENT = new Percent(1.0);

    private final Double percentValue;

    @EqualsAndHashCode.Exclude
    private final Integer significantDigits;

    /**
     * Constructor with a decimal value and some number of significant digits. A decimal value of 0.12345 with digit
     * count of 3 will result in 0.123 or 12.3%
     * @param value decimal value (example 0.1 which is equal to 10%)
     * @param digits number of digits to keep (begins at the decimal value)
     */
    public Percent(final Double value, final Integer digits) {
        percentValue = RoundingUtil.roundValue(value, digits);
        this.significantDigits = digits;
    }

    /**
     * Constructor with a decimal value and 2 significant digits. A decimal value of 0.12345 will result in 0.12 or 12%
     * @param value decimal value (example 0.1 which is equal to 10%)
     */
    public Percent(final Double value) {
        percentValue = RoundingUtil.roundValue(value);
        this.significantDigits = 2;
    }

    /**
     * Create a percent from a percent double (25.5 = 25.5%) rounded to the nearest digit place. 12.3456% with 3 digits
     * would be 12.346%
     * @param percent percent in double form
     * @param digits number of digits after the decimal percent to round to
     * @return a new Percent
     */
    public static Percent fromPercent(final Double percent, final Integer digits) {
        return new Percent(percent / 100.0, digits + 2);
    }

    /**
     * Create a percent from a percent double (25.5 = 25.5%) rounded to the nearest hundredths place. 12.3456% would be
     * 12%
     * @param percent percent in double form
     * @return a new Percent
     */
    public static Percent fromPercent(final Double percent) {
        return new Percent(percent / 100.0);
    }

    /**
     * Create a percent from a percent double (25.5 = 25.5%) rounded to the nearest digit place. 12.3456% with 3 digits
     * would be 12.3%
     * @param decimal decimal value
     * @param digits number of digits after the decimal percent to round to
     * @return a new Percent
     */
    public static Percent fromDecimal(final Double decimal, final Integer digits) {
        return new Percent(decimal, digits);
    }

    /**
     * Create a percent from a double (0.255 = 25.5%) rounded to the nearest hundredths place. 0.123456 would be 12%
     * @param decimal decimal value
     * @return a new Percent
     */
    public static Percent fromDecimal(final Double decimal) {
        return new Percent(decimal);
    }

    /**
     * Get the percent as a decimal value. E.g. 12.56% would return 0.1256
     * @return percent as decimal value
     */
    public Double asDouble() {
        return percentValue;
    }

    /**
     * Provide the Percent as a string value. E.g. 12.56%
     */
    @Override
    public String toString() {
        return percentValue * 100.0 + "%";
    }

    /**
     * {@inheritDoc}
     * Compares the percent with another percent
     */
    @Override
    public int compareTo(Percent otherPercent) {
        return percentValue.compareTo(otherPercent.percentValue);
    }

    /**
     * Compare the percent to a given decimal.
     * @see #compareTo(Percent)  
     */
    public int compareTo(Double decimal) {
        return percentValue.compareTo(decimal);
    }

    /**
     * Get the percent increase. For example a 5% increase would result in a multiplier of 105%. A 5% increase of $100
     * would result in $105 ($100 * (100% + 5%))
     * @return the percent value represented as an increase
     */
    public Percent increaseReversePercentage() {
        return Percent.ONE_HUNDRED_PERCENT.add(this);
    }

    /**
     * Get the percent decrease. For example a 5% decrease would result in a multiplier of 95%. A 5% decrease of $100
     * would result in $95 ($100 * (100% - 5%))
     * @return the percent value represented as a decrease
     */
    public Percent decreaseReversePercentage() {
        return Percent.ONE_HUNDRED_PERCENT.minus(this);
    }

    /**
     * Add the current value and another percent. This function returns a new percent does not change either input
     * percents. The percent with the lowest significant digits will be used to determine the number
     * of digits to return. Example percents 12.12345% and 12.123% will use 5 digits (12.123%) not 7.
     * @param other percent value
     * @return a copy of the sum of both values added together
     */
    public Percent add(final Percent other) {
        final Integer fewestSignificantDigits = getFewestSignificantDigits(other);
        return add(other, fewestSignificantDigits);
    }

    /**
     * Add the current value and another percent. This function returns a new percent does not change either input
     * percents.
     * @param other percent value
     * @param digits number of digits to round to at the decimal level
     * @return a copy of the sum of both values added together
     */
    public Percent add(final Percent other, Integer digits) {
        return Percent.fromDecimal(percentValue + other.percentValue, digits);
    }

    /**
     * Subtracts the current value and another percent (current - other). This function returns a new percent does not
     * change either input percents. The percent with the lowest significant digits will be used to determine the number
     * of digits to return. Example percents 12.12345% and 12.123% will use 5 digits (12.123%) not 7.
     * @param other percent value
     * @return a copy of the difference of both values
     */
    public Percent minus(final Percent other) {
        final Integer fewestSignificantDigits = getFewestSignificantDigits(other);
        return minus(other, fewestSignificantDigits);
    }

    /**
     * Subtracts the current value and another percent (current - other). This function returns a new percent does not
     * change either input percents.
     * @param other percent value
     * @param digits number of digits to round to at the decimal level
     * @return a copy of the difference of both values
     */
    public Percent minus(final Percent other, Integer digits) {
        return Percent.fromDecimal(percentValue - other.percentValue, digits);
    }

    /**
     * Multiplies the current value and another percent. This function returns a new percent does not change either
     * input percents. The percent with the lowest significant digits will be used to determine the number
     * of digits to return. Example percents 12.12345% and 12.123% will use 5 digits (12.123%) not 7.
     * @param other percent value
     * @return a copy of the product of both values
     */
    public Percent multiply(final Percent other) {
        final Integer fewestSignificantDigits = getFewestSignificantDigits(other);
        return multiply(other, fewestSignificantDigits);
    }

    /**
     * Multiplies the current value and another percent. This function returns a new percent does not change either
     * input percents.
     * @param other percent value
     * @param digits number of digits to round to at the decimal level
     * @return a copy of the product of both values
     */
    public Percent multiply(final Percent other, Integer digits) {
        return Percent.fromDecimal(percentValue * other.percentValue, digits);
    }

    /**
     * Divides the current value and another percent (current / other). This function returns a new percent does not
     * change either input percents. The percent with the lowest significant digits will be used to determine the number
     * of digits to return. Example percents 12.12345% and 12.123% will use 5 digits (12.123%) not 7.
     * @param other percent value
     * @return a copy of the dividend of both values
     */
    public Percent divide(final Percent other) {
        final Integer fewestSignificantDigits = getFewestSignificantDigits(other);
        return divide(other, fewestSignificantDigits);
    }

    /**
     * Divides the current value and another percent (current / other). This function returns a new percent does not
     * change either input percents.
     * @param other percent value
     * @param digits number of digits to round to at the decimal level
     * @return a copy of the dividend of both values
     */
    public Percent divide(final Percent other, Integer digits) {
        return Percent.fromDecimal(percentValue / other.percentValue, digits);
    }

    /**
     * Get the fewest significant digits between two percents. For mathematical operations where the digits is not
     * provided, lowest significant digits will be used. Significant digits are based on decimal representation
     * @param other
     * @return
     */
    public Integer getFewestSignificantDigits(final Percent other) {
        return Math.min(this.significantDigits, other.significantDigits);
    }
}
