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
