package org.finance.calcs.core.model.calculations;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.core.enums.EPaymentCalcType;
import org.finance.calcs.core.percent.Percent;
import org.finance.calcs.core.util.RoundingUtil;

@Data
public class PaymentCalculation {
    @NonNull
    private EPaymentCalcType paymentCalcType;

    // Flat Payment Rate values
    @NonNull
    private Double paymentFlatRate;

    // Percent Payment Rate
    private Percent paymentPercentRate;

    private Double paymentPercentBase;

    @Builder(builderClassName = "FlatPaymentCalculationBuilder", builderMethodName = "flatRateBuilder")
    public PaymentCalculation(Double paymentFlatRate) {
        this.paymentPercentBase = 0.0;
        this.paymentPercentRate = Percent.ZERO_PERCENT;
        this.paymentFlatRate = RoundingUtil.roundValue(paymentFlatRate);
        this.paymentCalcType = EPaymentCalcType.FLAT_PAYMENT;
    }

    @Builder(builderClassName = "PercentPaymentCalculationBuilder", builderMethodName = "percentRateBuilder")
    public PaymentCalculation(Double paymentPercentBase, Percent paymentPercentRate) {
        this.paymentPercentBase = paymentPercentBase;
        this.paymentPercentRate = paymentPercentRate;
        this.paymentFlatRate = RoundingUtil.roundValue(paymentPercentRate.asDouble() * paymentPercentBase);
        this.paymentCalcType = EPaymentCalcType.PERCENT_PAYMENT;
    }
}
