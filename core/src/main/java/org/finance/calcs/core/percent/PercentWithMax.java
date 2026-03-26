package org.finance.calcs.core.percent;

import lombok.Builder;

/**
 * Calculates a percent value with a max value. For example 10% of a value with a max of 100. If the calculated percent
 * is of the value 100 then the output would be 10 (less than max value), but if the input was 500,000 the max result
 * would be 100 (because 50,000 is more than 100).<p>
 * The max value must be positive double value.
 */
@Builder
public class PercentWithMax implements IPercentCalc {
    private final Percent percent;
    @Builder.Default
    private final Double maxValue = -1.0;

    /**
     * {@inheritDoc}
     * Provides the percent value up to the max value.
     */
    @Override
    public Double calculatePercentValueOf(Double baseValue) {
        if (maxValue == -1) {
            return percent.asDouble() * baseValue;
        }
        return Math.min(percent.asDouble() * baseValue, maxValue);
    }
}
