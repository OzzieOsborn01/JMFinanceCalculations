package org.finance.calcs.core.percent;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import org.finance.calcs.core.dataSerializing.PercentSerializer;
import org.finance.calcs.core.util.RoundingUtil;

@EqualsAndHashCode
@JsonSerialize(using = PercentSerializer.class)
public class Percent implements Comparable<Percent> {

    public static final Percent ZERO_PERCENT = new Percent(0.0);

    public static final Percent ONE_HUNDRED_PERCENT = new Percent(1.0);

    private final Double percentValue;

    @EqualsAndHashCode.Exclude
    private final Integer significantDigits;

    public Percent(final Double value, final Integer digits) {
        percentValue = RoundingUtil.roundValue(value, digits);
        this.significantDigits = digits;
    }

    public Percent(final Double value) {
        percentValue = RoundingUtil.roundValue(value);
        this.significantDigits = 2;
    }

    public static Percent fromPercent(final Double percent, final Integer digits) {
        return new Percent(percent / 100.0, digits + 2);
    }

    public static Percent fromPercent(final Double percent) {
        return new Percent(percent / 100.0);
    }

    public static Percent fromDecimal(final Double percent, final Integer digits) {
        return new Percent(percent, digits);
    }

    public static Percent fromDecimal(final Double percent) {
        return new Percent(percent);
    }

    public Double asDouble() {
        return percentValue;
    }

    @Override
    public String toString() {
        return percentValue * 100.0 + "%";
    }

    @Override
    public int compareTo(Percent o) {
        return percentValue.compareTo(o.percentValue);
    }

    public Percent increaseReversePercentage() {
        return Percent.ONE_HUNDRED_PERCENT.add(this);
    }

    public Percent decreaseReversePercentage() {
        return Percent.ONE_HUNDRED_PERCENT.minus(this);
    }

    public Percent add(final Percent other) {
        final Integer fewestSignificantDigits = getFewestSignificantDigits(other);
        return add(other, fewestSignificantDigits);
    }

    public Percent add(final Percent other, Integer digits) {
        return Percent.fromDecimal(percentValue + other.percentValue, digits);
    }

    public Percent minus(final Percent other) {
        final Integer fewestSignificantDigits = getFewestSignificantDigits(other);
        return minus(other, fewestSignificantDigits);
    }

    public Percent minus(final Percent other, Integer digits) {
        return Percent.fromDecimal(percentValue - other.percentValue, digits);
    }

    public Percent multiply(final Percent other) {
        final Integer fewestSignificantDigits = getFewestSignificantDigits(other);
        return multiply(other, fewestSignificantDigits);
    }

    public Percent multiply(final Percent other, Integer digits) {
        return Percent.fromDecimal(percentValue * other.percentValue, digits);
    }

    public Percent divide(final Percent other) {
        final Integer fewestSignificantDigits = getFewestSignificantDigits(other);
        return divide(other, fewestSignificantDigits);
    }

    public Percent divide(final Percent other, Integer digits) {
        return Percent.fromDecimal(percentValue / other.percentValue, digits);
    }

    public Integer getFewestSignificantDigits(final Percent other) {
        return Math.min(this.significantDigits, other.significantDigits);
    }
}
