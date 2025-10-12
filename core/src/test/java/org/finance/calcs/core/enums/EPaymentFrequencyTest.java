package org.finance.calcs.core.enums;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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

    @Test
    public void getNextDate() {
        final LocalDate date = LocalDate.of(2025, 9, 1);
        Assertions.assertEquals(LocalDate.of(2025, 10, 1), EPaymentFrequency.MONTHLY.getNextDate(date));
        Assertions.assertEquals(LocalDate.of(2025, 9, 15), EPaymentFrequency.BI_WEEKLY.getNextDate(date));
    }

    @Test
    public void getNextDate_notImplemented() {
        final LocalDate date = LocalDate.of(2025, 9, 1);
        Assertions.assertThrows(NotImplementedException.class, () -> EPaymentFrequency.NOT_IMPLEMENTED.getNextDate(date));
    }
}
