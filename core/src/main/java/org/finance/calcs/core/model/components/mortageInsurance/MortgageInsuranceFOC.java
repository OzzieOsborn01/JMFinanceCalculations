package org.finance.calcs.core.model.components.mortageInsurance;

import lombok.*;
import org.finance.calcs.core.enums.EMortgageInsuranceType;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.enums.ETerminationConditionFactor;
import org.finance.calcs.core.model.metadata.ObligationTerminationStrategy;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.finance.calcs.core.model.obligationBase.FinancialObligationComponent;
import org.finance.calcs.core.percent.Percent;
import org.finance.calcs.core.util.PaymentFrequencyUtil;
import org.finance.calcs.core.util.RoundingUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *  A mortgage insurance {@link FinancialObligationComponent} that represents either a Private Mortgage Insurance (PMI)
 *  or Mortgage Insurance Premium (MIP). This is tied to a mortgage.<p>
 *  Typically this lasts until the loan value is 20% or 22% of the home value or less (PMI) or either 11 years or for
 *  the life of the loan (MIP). This is configured using an {@link ObligationTerminationStrategy} and
 *  {@link ETerminationConditionFactor}.<p>
 *  There can either be a soft termination point (point where insurance can be cancelled but not necessarily -- optional)
 *  or a hard termination point (point where insurance must be cancelled - required). To calculate this effectively.<p>
 *  <b>Construction</b>: MortgageInsuranceFOC is best built using {@link MortgageInsuranceTerms} passed into the
 *  {@link org.finance.calcs.core.factories.MortgageInsuranceFOCFactory MortgageInsuranceFOCFactory} function.<p>
 *  <b>Processing</b>: To properly process the MortgageInsuranceFOC the following functions must be called:
 *  <ul>
 *    <li>resetPeriod - Reset any period balance. Used in part for duration based termination </li>
 *    <li>updateLoanValueAndHouseValue - update loan value and house value. Used in part for
 *      balance based termination </li>
 *    <li>determineTerminationScore - determine if duration and/or balance termination factors are met</li>
 *    <li>isTerminationComplete - if insurance is complete do not process further</li>
 *  </ul>
 *  @see org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceTerms MortgageInsuranceTerms
 *  @see org.finance.calcs.core.factories.MortgageInsuranceFOCFactory MortgageInsuranceFOCFactory
 */
@AllArgsConstructor
@Builder
@Data
public class MortgageInsuranceFOC implements FinancialObligationComponent {
    private EMortgageInsuranceType mortgageInsuranceType;
    @Setter(AccessLevel.NONE)
    private Double houseValue;
    @Setter(AccessLevel.NONE)
    private Double loanValue;
    private Percent loanToHouseValueRatio;
    private PaymentCalculation paymentCalculation;
    @NonNull
    private ETerminationConditionFactor terminationConditionFactor;

    private ObligationTerminationStrategy<Percent> softBalanceTermCondition;
    private ObligationTerminationStrategy<Long> softDurationTermCondition;
    private ObligationTerminationStrategy<Percent> hardBalanceTermCondition;
    private ObligationTerminationStrategy<Long> hardDurationTermCondition;

    private ChronoUnit durationTermUnits;

    private Double upfrontPremium;

    @NonNull
    private EPaymentFrequency paymentFrequency;

    @NonNull
    private Double flatRateDurationInsuranceRate;

    private Double periodBalance;

    private Boolean isInsuranceComplete;

    private LocalDate startDate;

    private LocalDate lastProcessedDate;

    public MortgageInsuranceFOC(final MortgageInsuranceTerms terms) {
        this.mortgageInsuranceType = terms.getMortgageInsuranceType();
        this.paymentFrequency = terms.getPaymentFrequency();
        this.paymentCalculation = terms.getPaymentCalculation();
        this.isInsuranceComplete = terms.getOverridden();
        this.upfrontPremium = terms.getUpfrontPremium();
        updateLoanValueAndHouseValue(terms.getLoanValue(), terms.getHouseValue());
        this.lastProcessedDate = terms.getStartDate();
        this.startDate = terms.getStartDate();
        this.flatRateDurationInsuranceRate = terms.getPaymentCalculation().getPaymentFlatRate();
        this.periodBalance = this.flatRateDurationInsuranceRate;
        this.terminationConditionFactor = terms.getTerminationConditionFactor();

        this.softBalanceTermCondition = terms.getSoftBalanceTermCondition();
        this.softDurationTermCondition = terms.getSoftDurationTermCondition();
        this.hardBalanceTermCondition = terms.getHardBalanceTermCondition();
        this.hardDurationTermCondition = terms.getHardDurationTermCondition();
        this.durationTermUnits = terms.getDurationTermUnits();
    }

    /**
     * Get the annual insurance price (assumes 1 year at the same price)
     * @return insurance price per year
     */
    public Double getAnnualInsurancePrice() {
        return PaymentFrequencyUtil.paymentRateConverter(flatRateDurationInsuranceRate, paymentFrequency, EPaymentFrequency.YEARLY);
    }

    /**
     * Return if the mortgage insurance is complete
     * @return if the insurance is completed usage
     */
    public Boolean isInsuranceComplete() {
        return isInsuranceComplete;
    }

    /**
     * Get the regularly scheduled payment
     * @return the regularly scheduled payment
     */
    public Double getScheduledPayment() {
        return flatRateDurationInsuranceRate;
    }

    /**
     * Get the current loan to value ratio. Used to determine if balance termination point is complete
     * @return the current loan to value ratio
     */
    public Percent getLoanToValueRatio() {
        return Percent.fromDecimal(loanValue/houseValue, 5);
    }

    /**
     * Get if the termination factor is met with the provided balance and duration condition depending on the insurance
     * termination condition
     * @param balanceConditionMet the hard or soft balance termination condition
     * @param durationConditionMet the hard or soft duration termination condition
     * @return boolean if the termination condition is met or not
     */
    private Boolean isTerminationFactorMet(final Boolean balanceConditionMet, final Boolean durationConditionMet) {
        return switch (terminationConditionFactor) {
            case OBLIGATION_COMPLETED -> balanceConditionMet;
            case DURATION_COMPLETED -> durationConditionMet;
            case OBLIGATION_OR_DURATION_COMPLETED -> balanceConditionMet || durationConditionMet;
            default -> false;
        };
    }

    /**
     * Is the soft termination condition met
     * @return boolean
     */
    public Boolean isSoftTerminationComplete() {
        final Boolean softBalanceConditionMet =
                (softBalanceTermCondition != null && softBalanceTermCondition.getTerminationConditionMatched());
        final Boolean softDurationConditionMet =
                (softDurationTermCondition != null && softDurationTermCondition.getTerminationConditionMatched());
        return isTerminationFactorMet(softBalanceConditionMet, softDurationConditionMet);
    }

    /**
     * Is the hard termination condition met
     * @return boolean
     */
    public Boolean isHardTerminationComplete() {
        final Boolean hardBalanceConditionMet =
                (hardBalanceTermCondition != null && hardBalanceTermCondition.getTerminationConditionMatched());
        final Boolean hardDurationConditionMet =
                (hardDurationTermCondition != null && hardDurationTermCondition.getTerminationConditionMatched());
        return isTerminationFactorMet(hardBalanceConditionMet, hardDurationConditionMet);
    }

    /**
     * Function that updates the mortgage insurance completion status. Assumes that the period is reset and the home to
     * loan values have been updated
     * @param useSoftTerminationStrategy should the soft termination strategy be used if applicable
     */
    public void determineTerminationScore(Boolean useSoftTerminationStrategy) {
        determineDurationTerminationScore();
        determineBalanceTerminationScore();

        final boolean softTerminationEnabled = isSoftTerminationComplete() && useSoftTerminationStrategy;
        final boolean hardTerminationEnabled = isHardTerminationComplete();

        if (hardTerminationEnabled || softTerminationEnabled) {
            isInsuranceComplete = true;
        }
    }

    /**
     * Determines if the current balance scores (hard and soft) satisfies the termination criteria
     */
    private void determineBalanceTerminationScore() {
        if (softBalanceTermCondition != null && softBalanceTermCondition.compareToValue(loanToHouseValueRatio)) {
            softBalanceTermCondition.setTerminationConditionMatched(true);
        }
        if (hardBalanceTermCondition != null && hardBalanceTermCondition.compareToValue(loanToHouseValueRatio)) {
            hardBalanceTermCondition.setTerminationConditionMatched(true);
        }
    }

    /**
     * Determines if the current duration score (hard and soft) satisfies the termination criteria
     */
    public void determineDurationTerminationScore() {
        final Long durationProcessed = startDate.until(lastProcessedDate, durationTermUnits);
        if (softDurationTermCondition != null && softDurationTermCondition.compareToValue(durationProcessed)) {
            softDurationTermCondition.setTerminationConditionMatched(true);
        }
        if (hardDurationTermCondition != null && hardDurationTermCondition.compareToValue(durationProcessed)) {
            hardDurationTermCondition.setTerminationConditionMatched(true);
        }
    }

    /**
     * Update the stored loan, house, and loan to house value ratio with the provided values.
     * Used to determine balance termination score
     * @param loanValue
     * @param houseValue
     */
    public void updateLoanValueAndHouseValue(final Double loanValue, final Double houseValue) {
        this.loanValue = RoundingUtil.roundValue(loanValue);
        this.houseValue = RoundingUtil.roundValue(houseValue);
        this.loanToHouseValueRatio = Percent.fromDecimal(loanValue/houseValue, 5);
    }

    /**
     * Update the loan value and the home to loan value ratio
     * @param loanValue
     */
    public void updateLoanValue(final Double loanValue) {
        updateLoanValueAndHouseValue(loanValue, houseValue);
    }

    /**
     * Update the house value and the home to loan value ratio
     * @param houseValue
     */
    public void updateHouseValue(final Double houseValue) {
        updateLoanValueAndHouseValue(loanValue, houseValue);
    }

    /**
     * Update the last processed date
     * @param lastProcessedDate
     */
    public void updateLastProcessedDate(final LocalDate lastProcessedDate) {
        setLastProcessedDate(lastProcessedDate);
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
     * Reset the period with the latest information
     * @param startDate
     */
    public void resetPeriod(final LocalDate startDate) {
        this.periodBalance = flatRateDurationInsuranceRate;
        this.lastProcessedDate = startDate;
    }

    /**
     * Get the next period start date
     * @return
     */
    public LocalDate getNextPeriodStartDate() {
        return paymentFrequency.getNextDate(lastProcessedDate);
    }
}
