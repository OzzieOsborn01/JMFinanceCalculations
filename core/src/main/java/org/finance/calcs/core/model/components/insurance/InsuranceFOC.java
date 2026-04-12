package org.finance.calcs.core.model.components.insurance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.model.obligationBase.FinancialObligationComponent;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.finance.calcs.core.percent.Percent;
import org.finance.calcs.core.util.PaymentFrequencyUtil;
import org.finance.calcs.core.util.RoundingUtil;

import java.time.LocalDate;

/**
 * InsuranceFOC is a {@link FinancialObligationComponent} that represents a general insurance obligation that needs to
 * be paid. Examples include Home Insurance, Auto Insurance, etc.
 */
@Data
@Builder
@AllArgsConstructor
public class InsuranceFOC implements FinancialObligationComponent {
    /**
     * Calculates the regular payment
     */
    @NonNull
    private final PaymentCalculation paymentCalculation;

    /**
     * What is the type of insurance
     */
    @NonNull
    private final String insuranceType;

    /**
     * Who provides the insurance
     */
    @NonNull
    private final String insuranceProvider;

    /**
     * How often is the regular payment to be made
     */
    @NonNull
    private EPaymentFrequency paymentFrequency;

    /**
     * The scheduled minimum payment
     */
    @NonNull
    private Double scheduledPaymentAmount;

    /**
     * The regular period of the insurance
     */
    @NonNull
    private EPaymentFrequency insurancePeriodDuration;

    /**
     * The insurance obligation start date
     */
    @NonNull
    private final LocalDate startDate;

    /**
     * The current period start date
     */
    private LocalDate periodStartDate;

    /**
     * The current period end date
     */
    private LocalDate periodEndDate;

    /**
     * The current period balance
     */
    private Double periodBalance;

    /**
     * Constructor that builds insurance FOC based on the provided terms
     * @param terms insurance terms
     */
    public InsuranceFOC(@NonNull final InsuranceTerms terms) {
        this.insuranceType = terms.getInsuranceType();
        this.insuranceProvider = terms.getInsuranceProvider();
        this.paymentCalculation = terms.getPaymentCalculation();
        this.startDate = terms.getStartDate();
        this.periodStartDate = startDate;
        this.periodEndDate = terms.getInsurancePeriodDuration().getNextDate(startDate);
        adjustScheduledPayment(terms.getPaymentFrequency(), terms.getInsurancePeriodDuration(), terms.getPaymentCalculation());
        this.periodBalance = getFlatRateDurationInsuranceRate();
    }

    /**
     * Get the current annual insurance rate (total insurance for the year)
     * @return annualized payment amount
     */
    public Double getAnnualInsuranceRate() {
        return PaymentFrequencyUtil.paymentRateConverter(
                getFlatRateDurationInsuranceRate(),
                insurancePeriodDuration,
                EPaymentFrequency.YEARLY);
    }

    /**
     * Get the insurance rate percent.
     * @return
     */
    public Percent getInsuranceRatePercent() {
        return paymentCalculation.getPaymentPercentRate();
    }

    /**
     * Get the current insurance period balance
     * @return
     */
    public Double getInsurancePeriodBalance() {
        return periodBalance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getNextPeriodStartDate() {
        return insurancePeriodDuration.getNextDate(periodStartDate);
    }

    /**
     * Provide the payment flat rate
     * @return the amount owed for all periods
     */
    public Double getFlatRateDurationInsuranceRate() {
        return paymentCalculation.getPaymentFlatRate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double applyDecreasingBalance(double amount) {
        periodBalance = RoundingUtil.roundValue(periodBalance - amount);
        return periodBalance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double applyIncreasingBalance(double amount) {
        periodBalance = RoundingUtil.roundValue(periodBalance + amount);
        return periodBalance;
    }

    /**
     * Adjust the insurance payment amounts and frequency
     * <p/>
     * Only run this function at the beginning of a period or this will introduce errors
     * <p/>
     * TODO: This function could introduce bugs if performed incorrectly. Add safeguards and better protections.
     * @param scheduledPaymentFrequency
     * @param insurancePeriodDuration
     * @param paymentCalculation
     */
    void adjustScheduledPayment(
            final EPaymentFrequency scheduledPaymentFrequency,
            final EPaymentFrequency insurancePeriodDuration,
            final PaymentCalculation paymentCalculation
    ) {
        // TODO: Consider a change current period and adjust next period versions
        this.insurancePeriodDuration = insurancePeriodDuration;
        this.paymentFrequency = scheduledPaymentFrequency;
        this.scheduledPaymentAmount = RoundingUtil.roundValue(
                PaymentFrequencyUtil.paymentRateConverter(
                        getFlatRateDurationInsuranceRate(), insurancePeriodDuration, paymentFrequency));

        // May cause bugs if performed midway through a period
        resetPeriod(startDate);
    }

    /**
     * Reset the period to start at the provided period start date and adjust values accordingly
     * @param startDate next date that should be the period start date
     */
    public void resetPeriod(@NonNull final LocalDate startDate) {
        this.periodBalance = getFlatRateDurationInsuranceRate();
        this.periodStartDate = startDate;
        this.periodEndDate = insurancePeriodDuration.getNextDate(startDate);
    }
}
