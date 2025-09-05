package org.finance.calcs.core.util;

import org.finance.calcs.core.percent.Percent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PaymentCalculationUtilTest {

    @Test
    public void PMT() {
        final double pmt = PaymentCalculationUtil.PMT(
                Percent.fromPercent(6.125, 4),
                360,
                775000.0,
                12
        );
        Assertions.assertEquals(4708.98, pmt);
    }

    @Test
    public void TaxPMT() {
        final double pmt = PaymentCalculationUtil.TaxPMT(
                Percent.fromPercent(1.5, 4),
                875000.0,
                12
        );
        Assertions.assertEquals(1093.75, pmt);
    }
}
