package org.finance.calcs.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoundingUtilTest {
    @Test
    public void roundValue_sameValue() {
        Assertions.assertEquals(5.25, RoundingUtil.roundValue(5.25));
    }

    @Test
    public void roundValue_roundDown() {
        Assertions.assertEquals(5.37, RoundingUtil.roundValue(5.373));
    }

    @Test
    public void roundValue_roundUp() {
        Assertions.assertEquals(5.38, RoundingUtil.roundValue(5.378));
    }

    @Test
    public void roundValue_roundUpHalf() {
        Assertions.assertEquals(5.38, RoundingUtil.roundValue(5.375));
    }

    @Test
    public void roundValueUp_sameValue() {
        Assertions.assertEquals(5.25, RoundingUtil.roundValueUp(5.25));
    }

    @Test
    public void roundValueUp_roundDown() {
        Assertions.assertEquals(5.38, RoundingUtil.roundValueUp(5.373));
    }

    @Test
    public void roundValueUp_roundUp() {
        Assertions.assertEquals(5.38, RoundingUtil.roundValueUp(5.378));
    }

    @Test
    public void roundValueUp_roundUpHalf() {
        Assertions.assertEquals(5.38, RoundingUtil.roundValueUp(5.375));
    }

    @Test
    public void roundValueDown_sameValue() {
        Assertions.assertEquals(5.25, RoundingUtil.roundValueDown(5.25));
    }

    @Test
    public void roundValueDown_roundDown() {
        Assertions.assertEquals(5.37, RoundingUtil.roundValueDown(5.373));
    }

    @Test
    public void roundValueDown_roundUp() {
        Assertions.assertEquals(5.37, RoundingUtil.roundValueDown(5.378));
    }

    @Test
    public void roundValueDown_roundUpHalf() {
        Assertions.assertEquals(5.37, RoundingUtil.roundValueDown(5.375));
    }
}
