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

    @Test
    public void percent_Compare_Equals() {
        final Percent percent = Percent.fromPercent(6.125);
        final Percent percentToCompare = Percent.fromPercent(6.125);
        Assertions.assertEquals(0, percent.compareTo(percentToCompare));
    }

    @Test
    public void percent_Compare_Lesser() {
        final Percent percent = Percent.fromPercent(6.125);
        final Percent percentToCompare = Percent.fromPercent(7.125);
        Assertions.assertEquals(-1, percent.compareTo(percentToCompare));
    }

    @Test
    public void percent_Compare_Greater() {
        final Percent percent = Percent.fromPercent(6.125);
        final Percent percentToCompare = Percent.fromPercent(5.125);
        Assertions.assertEquals(1, percent.compareTo(percentToCompare));
    }

    @Test
    public void percent_Add() {
        final Percent p1 = Percent.fromPercent(5.0);
        final Percent p2 = Percent.fromPercent(5.0);
        final Percent expected = Percent.fromPercent(10.0);
        Assertions.assertEquals(expected, p1.add(p2));
    }

    @Test
    public void percent_Minus() {
        final Percent p1 = Percent.fromPercent(5.0);
        final Percent p2 = Percent.fromPercent(5.0);
        final Percent expected = Percent.fromPercent(0.0);
        Assertions.assertEquals(expected, p1.minus(p2));
    }

    @Test
    public void percent_Multiply() {
        final Percent p1 = Percent.fromPercent(5.0,7);
        final Percent p2 = Percent.fromPercent(5.0,7);
        final Percent expected = Percent.fromPercent(0.25,7);
        Assertions.assertEquals(expected, p1.multiply(p2));
    }

    @Test
    public void percent_Multiply_CustomDigits() {
        final Percent p1 = Percent.fromPercent(5.0,7);
        final Percent p2 = Percent.fromPercent(5.0,7);
        final Percent expected = Percent.fromPercent(0.3,7);
        Assertions.assertEquals(expected, p1.multiply(p2,3));
    }

    @Test
    public void percent_divide() {
        final Percent p1 = Percent.fromPercent(5.0);
        final Percent p2 = Percent.fromPercent(5.0);
        final Percent expected = Percent.ONE_HUNDRED_PERCENT;
        Assertions.assertEquals(expected, p1.divide(p2));
    }

    @Test
    public void percent_increaseReversePercentage() {
        final Percent p1 = Percent.fromPercent(5.0);
        final Percent expected = Percent.fromPercent(105.0);
        Assertions.assertEquals(expected, p1.increaseReversePercentage());
    }

    @Test
    public void percent_decreaseReversePercentage() {
        final Percent p1 = Percent.fromPercent(5.0);
        final Percent expected = Percent.fromPercent(95.0);
        Assertions.assertEquals(expected, p1.decreaseReversePercentage());
    }
}
