package org.finance.calcs.core.util;


import org.finance.calcs.core.percent.Percent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

/**
 * Payment calculation related utility functions. This includes functions like:
 * <ul>
 *     <li>PMT function to get the regular payment including interest for a given FOC</li>
 *     <li>TaxPMT function to get the regular tax payment based off a given property value for a given FOC</li>
 * </ul>
 */
public final class PaymentCalculationUtil {
    /**
     * Calculates the periodic payment for a loan with a fixed interest rate and constant payments
     * @param annualInterestRate decimal value of the annual interest rate (example 0.25 not 25.0%)
     * @param totalPayments total number of payments for the lifetime of the loan
     * @param principle initial principle value
     * @param paymentsPerYear regular payments per year (usually 12 for monthly)
     * @return regular payment
     */
    public static double PMT(
            final double annualInterestRate,
            final int totalPayments,
            final double principle,
            final int paymentsPerYear
    ) {
        final double adjustedRate = annualInterestRate / paymentsPerYear;
        final double rateNumerator = adjustedRate * Math.pow((1 + adjustedRate), totalPayments);
        final double rateDenominator = Math.pow(1.0 + adjustedRate, totalPayments) - 1.0;
        return RoundingUtil.roundValue((principle * rateNumerator) / rateDenominator);
    }

    /**
     * Calculates the periodic payment for a loan with a fixed interest rate and constant payments
     * @param annualInterestRate percent value of the annual interest rate (example 25%)
     * @param totalPayments total number of payments for the lifetime of the loan
     * @param principle initial principle value
     * @param paymentsPerYear regular payments per year (usually 12 for monthly)
     * @return regular payment
     */
    public static double PMT(
            final Percent annualInterestRate,
            final int totalPayments,
            final double principle,
            final int paymentsPerYear
    ) {
        return PMT(annualInterestRate.asDouble(), totalPayments, principle, paymentsPerYear);
    }

    /**
     * Calculates the periodic tax payment for a loan with a fixed tax rate and constant payments
     * @param annualTaxRate decimal value of the annual tax rate (example 0.25 not 25.0%)
     * @param propertyValue total property value
     * @param paymentsPerYear regular payments per year (usually 12 for monthly)
     * @return regular payment
     */
    public static Double TaxPMT(
            final double annualTaxRate,
            final double propertyValue,
            final int paymentsPerYear
    ) {
        Function<Double, Double> roundValueUp = (Double value) -> new BigDecimal(value).setScale(2, RoundingMode.UP).doubleValue();
        final Double annualTaxAmount = propertyValue * annualTaxRate;
        return roundValueUp.apply(annualTaxAmount / paymentsPerYear);
    }

    /**
     * Calculates the periodic tax payment for a loan with a fixed tax rate and constant payments
     * @param annualTaxRate percent value of the annual tax rate (example 25%)
     * @param propertyValue total property value
     * @param paymentsPerYear regular payments per year (usually 12 for monthly)
     * @return regular payment
     */
    public static Double TaxPMT(
            final Percent annualTaxRate,
            final double propertyValue,
            final int paymentsPerYear
    ) {
        return TaxPMT(annualTaxRate.asDouble(), propertyValue, paymentsPerYear);
    }
}
