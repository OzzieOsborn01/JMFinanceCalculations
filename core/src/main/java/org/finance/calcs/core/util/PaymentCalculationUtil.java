package org.finance.calcs.core.util;


import org.finance.calcs.core.percent.Percent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public final class PaymentCalculationUtil {
    public static double PMT(
            final double yearlyInterestRate,
            final int totalPayments,
            final double principle,
            final int paymentsPerYear
    ) {
        final double adjustedRate = yearlyInterestRate / paymentsPerYear;
        final double rateNumerator = adjustedRate * Math.pow((1 + adjustedRate), totalPayments);
        final double rateDenominator = Math.pow(1.0 + adjustedRate, totalPayments) - 1.0;
        return RoundingUtil.roundValue((principle * rateNumerator) / rateDenominator);
    }

    public static double PMT(
            final Percent yearlyInterestRate,
            final int totalPayments,
            final double principle,
            final int paymentsPerYear
    ) {
        return PMT(yearlyInterestRate.asDouble(), totalPayments, principle, paymentsPerYear);
    }

    public static Double TaxPMT(
            final double annualTaxRate,
            final double homeValue,
            final int paymentsPerYear
    ) {
        Function<Double, Double> roundValueUp = (Double value) -> new BigDecimal(value).setScale(2, RoundingMode.UP).doubleValue();
        final Double annualTaxAmount = homeValue * annualTaxRate;
        return roundValueUp.apply(annualTaxAmount / paymentsPerYear);
    }

    public static Double TaxPMT(
            final Percent annualTaxRate,
            final double homeValue,
            final int paymentsPerYear
    ) {
        return TaxPMT(annualTaxRate.asDouble(), homeValue, paymentsPerYear);
    }
}
