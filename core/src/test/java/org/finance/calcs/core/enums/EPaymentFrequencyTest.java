package org.finance.calcs.core.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EPaymentFrequencyTest {

    @Test
    public void paymentRateConverter() {
        Assertions.assertEquals(5672.0, EPaymentFrequency.MONTHLY.paymentRateConverter(5672.0));
        Assertions.assertEquals(2836.0, EPaymentFrequency.BI_WEEKLY.paymentRateConverter(5672.0));
    }

    @Test
    public void regularPaymentsPerYear() {
        Assertions.assertEquals(12, EPaymentFrequency.MONTHLY.regularPaymentsPerYear());
        Assertions.assertEquals(26, EPaymentFrequency.BI_WEEKLY.regularPaymentsPerYear());
    }

    @Test
    public void interestFrequency() {
        Assertions.assertEquals(EInterestFrequency.MONTHLY_12_BY_360, EPaymentFrequency.MONTHLY.interestFrequency());
        Assertions.assertEquals(EInterestFrequency.WEEKLY_52_BY_364, EPaymentFrequency.BI_WEEKLY.interestFrequency());
    }
}
