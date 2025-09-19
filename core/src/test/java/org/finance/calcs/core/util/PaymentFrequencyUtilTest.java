package org.finance.calcs.core.util;

import org.apache.commons.lang3.NotImplementedException;
import org.finance.calcs.core.enums.EInterestFrequency;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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
    public void paymentRateConverter_NotImplemented() {
        Assertions.assertThrows(NotImplementedException.class, () -> PaymentFrequencyUtil.paymentRateConverter(5672.0, EPaymentFrequency.NOT_IMPLEMENTED));
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
    public void regularPaymentsPerYear_NotImplemented() {
        Assertions.assertThrows(NotImplementedException.class, () -> PaymentFrequencyUtil.regularPaymentsPerYear(EPaymentFrequency.NOT_IMPLEMENTED));
    }

    @Test
    public void regularInterestFrequency_Monthly() {
        Assertions.assertEquals(EInterestFrequency.MONTHLY_12_BY_360, PaymentFrequencyUtil.regularInterestFrequency(EPaymentFrequency.MONTHLY));
    }

    @Test
    public void regularInterestFrequency_BiWeekly() {
        Assertions.assertEquals(EInterestFrequency.WEEKLY_52_BY_364, PaymentFrequencyUtil.regularInterestFrequency(EPaymentFrequency.BI_WEEKLY));
    }

    @Test
    public void regularInterestFrequency_NotImplemented() {
        Assertions.assertThrows(NotImplementedException.class, () -> PaymentFrequencyUtil.regularInterestFrequency(EPaymentFrequency.NOT_IMPLEMENTED));
    }

    @ParameterizedTest
    @MethodSource("paymentRateConverterArguments")
    public void paymentRateConverter(final double paymentRate, final EPaymentFrequency original, final EPaymentFrequency newFreq, final double expected) {
        Assertions.assertEquals(
                expected,
                PaymentFrequencyUtil.paymentRateConverter(paymentRate, original, newFreq));
    }
    private static Stream<Arguments> paymentRateConverterArguments() {
        return Stream.of(
                Arguments.of(1200, EPaymentFrequency.BI_WEEKLY, EPaymentFrequency.BI_WEEKLY, 1200),
                Arguments.of(1200, EPaymentFrequency.BI_WEEKLY, EPaymentFrequency.MONTHLY, 2400),
                Arguments.of(1200, EPaymentFrequency.BI_WEEKLY, EPaymentFrequency.SEMI_ANNUALLY, 14400),
                Arguments.of(1200, EPaymentFrequency.BI_WEEKLY, EPaymentFrequency.YEARLY, 31200),
                Arguments.of(1200, EPaymentFrequency.MONTHLY, EPaymentFrequency.BI_WEEKLY, 600),
                Arguments.of(1200, EPaymentFrequency.MONTHLY, EPaymentFrequency.MONTHLY, 1200),
                Arguments.of(1200, EPaymentFrequency.MONTHLY, EPaymentFrequency.SEMI_ANNUALLY, 7200),
                Arguments.of(1200, EPaymentFrequency.MONTHLY, EPaymentFrequency.YEARLY, 14400),
                Arguments.of(1200, EPaymentFrequency.SEMI_ANNUALLY, EPaymentFrequency.BI_WEEKLY, 100),
                Arguments.of(1200, EPaymentFrequency.SEMI_ANNUALLY, EPaymentFrequency.MONTHLY, 200),
                Arguments.of(1200, EPaymentFrequency.SEMI_ANNUALLY, EPaymentFrequency.SEMI_ANNUALLY, 1200),
                Arguments.of(1200, EPaymentFrequency.SEMI_ANNUALLY, EPaymentFrequency.YEARLY, 2400),
                Arguments.of(1200, EPaymentFrequency.YEARLY, EPaymentFrequency.BI_WEEKLY, 46.15),
                Arguments.of(1200, EPaymentFrequency.YEARLY, EPaymentFrequency.MONTHLY, 100),
                Arguments.of(1200, EPaymentFrequency.YEARLY, EPaymentFrequency.SEMI_ANNUALLY, 600),
                Arguments.of(1200, EPaymentFrequency.YEARLY, EPaymentFrequency.YEARLY, 1200)
        );
    }
}
