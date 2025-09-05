package org.finance.calcs.core.percent;

import lombok.Builder;

@Builder
public class PercentWithMax implements IPercentCalc {
    private final Percent percent;
    @Builder.Default
    private final Double maxValue = -1.0;

    @Override
    public Double calculatePercentValueOf(Double baseValue) {
        if (maxValue == -1) {
            return percent.asDouble() * baseValue;
        }
        return Math.min(percent.asDouble() * baseValue, maxValue);
    }
}
