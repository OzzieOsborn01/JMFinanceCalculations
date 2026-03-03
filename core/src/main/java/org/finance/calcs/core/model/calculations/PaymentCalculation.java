package org.finance.calcs.core.model.calculations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.core.enums.EPaymentCalcType;
import org.finance.calcs.core.percent.Percent;
import org.finance.calcs.core.util.RoundingUtil;

/**
 * PercentCalculation represents how much a payment should be. This can be generated from a flat rate amount or taken
 * from a percent rate.
 */
@Data
@AllArgsConstructor
@Builder
public class PaymentCalculation {
    @NonNull
    private EPaymentCalcType paymentCalcType;

    // Flat Payment Rate values
    @NonNull
    private Double paymentFlatRate;

    // Percent Payment Rate
    private Percent paymentPercentRate;
    private Double paymentPercentBase;

    /**
     * Builds a payment calculation from a flat rate input
     * @param paymentFlatRate flat rate input
     */
    @Builder(builderClassName = "FlatPaymentCalculationBuilder", builderMethodName = "flatRateBuilder")
    public PaymentCalculation(Double paymentFlatRate) {
        this.paymentPercentBase = 0.0;
        this.paymentPercentRate = Percent.ZERO_PERCENT;
        this.paymentFlatRate = RoundingUtil.roundValue(paymentFlatRate);
        this.paymentCalcType = EPaymentCalcType.FLAT_PAYMENT;
    }


    /**
     * Builds a payment calculation from a percent rate inputs
     * @param paymentPercentBase the base the percent should be taken from
     * @param paymentPercentRate the percent that should be used to make the flat rate amount
     */
    @Builder(builderClassName = "PercentPaymentCalculationBuilder", builderMethodName = "percentRateBuilder")
    public PaymentCalculation(Double paymentPercentBase, Percent paymentPercentRate) {
        this.paymentPercentBase = paymentPercentBase;
        this.paymentPercentRate = paymentPercentRate;
        this.paymentFlatRate = RoundingUtil.roundValue(paymentPercentRate.asDouble() * paymentPercentBase);
        this.paymentCalcType = EPaymentCalcType.PERCENT_PAYMENT;
    }
}
