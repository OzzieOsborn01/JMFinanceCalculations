package org.finance.calcs.core.util;

import org.finance.calcs.core.enums.EInterestFrequency;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PaymentFrequencyUtilTest {

    @Test
    public void paymentRateConverter_Monthly() {
        Assertions.assertEquals(5672.0, PaymentFrequencyUtil.paymentRateConverter(5672.0, EPaymentFrequency.MONTHLY));
    }

    @Test
    public void paymentRateConverter_BiWeekly() {
        Assertions.assertEquals(2836.0, PaymentFrequencyUtil.paymentRateConverter(5672.0, EPaymentFrequency.BI_WEEKLY));
    }

    @Test
    public void regularPaymentsPerYear_Monthly() {
        Assertions.assertEquals(12, PaymentFrequencyUtil.regularPaymentsPerYear(EPaymentFrequency.MONTHLY));
    }

    @Test
    public void regularPaymentsPerYear_BiWeekly() {
        Assertions.assertEquals(26, PaymentFrequencyUtil.regularPaymentsPerYear(EPaymentFrequency.BI_WEEKLY));
    }

    @Test
    public void regularInterestFrequency_Monthly() {
        Assertions.assertEquals(EInterestFrequency.MONTHLY_12_BY_360, PaymentFrequencyUtil.regularInterestFrequency(EPaymentFrequency.MONTHLY));
    }

    @Test
    public void regularInterestFrequency_BiWeekly() {
        Assertions.assertEquals(EInterestFrequency.WEEKLY_52_BY_364, PaymentFrequencyUtil.regularInterestFrequency(EPaymentFrequency.BI_WEEKLY));
    }
}
