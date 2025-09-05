package org.finance.calcs.core.percent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PercentWithMaxTest {

    @Test
    public void calculatePercentValueOf_Percent() {
        final PercentWithMax percent = PercentWithMax.builder()
                .percent(new Percent(0.25))
                .build();

        Assertions.assertEquals(1475.0, percent.calculatePercentValueOf(5900.0));
    }

    @Test
    public void calculatePercentValueOf_MaxValue() {
        final PercentWithMax percent = PercentWithMax.builder()
                .percent(new Percent(0.25))
                .maxValue(750.0)
                .build();

        Assertions.assertEquals(750.0, percent.calculatePercentValueOf(5900.0));
    }
}
