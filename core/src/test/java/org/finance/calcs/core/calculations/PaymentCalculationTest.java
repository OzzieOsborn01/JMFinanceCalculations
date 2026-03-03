package org.finance.calcs.core.calculations;

import org.finance.calcs.core.enums.EPaymentCalcType;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.finance.calcs.core.percent.Percent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PaymentCalculationTest {
    @Test
    public void flatPaymentCalculation() {
        final PaymentCalculation calc = PaymentCalculation.flatRateBuilder().paymentFlatRate(100.0).build();
        final PaymentCalculation expected = PaymentCalculation.builder()
                .paymentPercentBase(0.0)
                .paymentPercentRate(Percent.ZERO_PERCENT)
                .paymentFlatRate(100.0)
                .paymentCalcType(EPaymentCalcType.FLAT_PAYMENT)
                .build();
        Assertions.assertEquals(expected, calc);
    }

    @Test
    public void percentPaymentCalculation() {
        final PaymentCalculation calc = PaymentCalculation.percentRateBuilder()
                .paymentPercentBase(1000.0)
                .paymentPercentRate(Percent.fromPercent(25.0))
                .build();
        final PaymentCalculation expected = PaymentCalculation.builder()
                .paymentPercentBase(1000.0)
                .paymentPercentRate(Percent.fromPercent(25.0))
                .paymentFlatRate(250.0)
                .paymentCalcType(EPaymentCalcType.PERCENT_PAYMENT)
                .build();
        Assertions.assertEquals(expected, calc);
    }
}
