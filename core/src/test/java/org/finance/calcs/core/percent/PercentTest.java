package org.finance.calcs.core.percent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PercentTest {

    @Test
    public void percent_equals() {
        final Percent percent1 = Percent.fromDecimal(0.06);
        final Percent percent2 = Percent.fromDecimal(0.06);
        Assertions.assertEquals(percent1, percent2);
    }

    @Test
    public void percent_fromDecimal() {
        final Percent percent = Percent.fromDecimal(0.06);
        Assertions.assertEquals(0.06, percent.asDouble());
        Assertions.assertEquals("6.0%", percent.toString());
    }

    @Test
    public void percent_4Digits() {
        final Percent percent = Percent.fromDecimal(0.06125, 5);
        Assertions.assertEquals(0.06125, percent.asDouble());
        Assertions.assertEquals("6.125%", percent.toString());
    }

    @Test
    public void percent_2Digits() {
        final Percent percent = Percent.fromDecimal(0.06125);
        Assertions.assertEquals(0.06, percent.asDouble());
        Assertions.assertEquals("6.0%", percent.toString());
    }

    @Test
    public void percent_fromPercent() {
        final Percent percent = Percent.fromPercent(6.0);
        Assertions.assertEquals(0.06, percent.asDouble());
        Assertions.assertEquals("6.0%", percent.toString());
    }

    @Test
    public void percent_4Digits_fromPercent() {
        final Percent percent = Percent.fromPercent(6.125, 4);
        Assertions.assertEquals(0.06125, percent.asDouble());
        Assertions.assertEquals("6.125%", percent.toString());
    }

    @Test
    public void percent_2Digits_fromPercent() {
        final Percent percent = Percent.fromPercent(6.125);
        Assertions.assertEquals(0.06, percent.asDouble());
        Assertions.assertEquals("6.0%", percent.toString());
    }
}
