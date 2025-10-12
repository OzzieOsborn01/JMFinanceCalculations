package org.finance.calcs.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundingUtil {
    public static double roundValue(final double value) {
        return roundValue(value, 2);
    }

    public static double roundValue(final double value, final int digits) {
        return new BigDecimal(value).setScale(digits, RoundingMode.HALF_UP).doubleValue();
    }

    public static double roundValueWithMinCap(final double value, final double minCap) {
        return roundValueWithMinCap(value, 2, minCap);
    }

    public static double roundValueWithMinCap(final double value, final int digits, final double minCap) {
        final BigDecimal bdMinCap = new BigDecimal(minCap);
        return new BigDecimal(value).max(bdMinCap).setScale(digits, RoundingMode.HALF_UP).doubleValue();
    }

    public static double roundValueWithMaxCap(final double value, final double maxCap) {
        return roundValueWithMaxCap(value, 2, maxCap);
    }

    public static double roundValueWithMaxCap(final double value, final int digits, final double maxCap) {
        final BigDecimal bdMaxCap = new BigDecimal(maxCap);
        return new BigDecimal(value).min(bdMaxCap).setScale(digits, RoundingMode.HALF_UP).doubleValue();
    }

    public static double roundValueWithCaps(final double value, final double maxCap, final double minCap) {
        return roundValueWithCaps(value, 2, maxCap, minCap);
    }

    public static double roundValueWithCaps(final double value, final int digits, final double maxCap, final double minCap) {
        final BigDecimal bdMaxCap = new BigDecimal(maxCap);
        final BigDecimal bdMinCap = new BigDecimal(minCap);
        return new BigDecimal(value).min(bdMaxCap).max(bdMinCap).setScale(digits, RoundingMode.HALF_UP).doubleValue();
    }

    public static double roundValueUp(final double value) {
        return roundValueUp(value, 2);
    }

    public static double roundValueUp(final double value, final int digits) {
        return new BigDecimal(value).setScale(digits, RoundingMode.UP).doubleValue();
    }

    public static double roundValueDown(final double value) {
        return roundValueDown(value, 2);
    }

    public static double roundValueDown(final double value, final int digits) {
        return new BigDecimal(value).setScale(digits, RoundingMode.DOWN).doubleValue();
    }
}
