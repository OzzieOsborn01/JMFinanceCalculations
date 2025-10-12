package org.finance.calcs.core.model.components.mortageInsurance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.core.enums.EMortgageInsuranceType;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.model.metadata.ObligationTerminationStrategy;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.finance.calcs.core.percent.Percent;
import org.finance.calcs.core.util.PaymentFrequencyUtil;
import org.finance.calcs.core.util.RoundingUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Builder
@Data
public class PrivateMortgageInsuranceFOC implements MortgageInsuranceFOC {
    private EMortgageInsuranceType mortgageInsuranceType;
    private Double houseValue;
    private Double loanValue;
    private Percent loanToHouseValueRatio;
    private PaymentCalculation paymentCalculation;

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

    public PrivateMortgageInsuranceFOC(final MortgageInsuranceTerms terms) {
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

        this.softBalanceTermCondition = terms.getSoftBalanceTermCondition();
        this.softDurationTermCondition = terms.getSoftDurationTermCondition();
        this.hardBalanceTermCondition = terms.getHardBalanceTermCondition();
        this.hardDurationTermCondition = terms.getHardDurationTermCondition();
        this.durationTermUnits = terms.getDurationTermUnits();
    }

    @Override
    public Double getAnnualInsuranceRate() {
        return PaymentFrequencyUtil.paymentRateConverter(flatRateDurationInsuranceRate, paymentFrequency, EPaymentFrequency.YEARLY);
    }

    @Override
    public Boolean isInsuranceComplete() {
        return isInsuranceComplete;
    }

    @Override
    public Double getScheduledPayment() {
        return flatRateDurationInsuranceRate;
    }

    @Override
    public Percent getLoanToValueRatio() {
        return Percent.fromDecimal(loanValue/houseValue, 5);
    }

    @Override
    public Double getUpfrontPremium() {
        return upfrontPremium;
    }

    @Override
    public Boolean isSoftTerminationComplete() {
        return (softBalanceTermCondition != null && softBalanceTermCondition.getTerminationConditionMatched()) ||
                (softDurationTermCondition != null && softDurationTermCondition.getTerminationConditionMatched());
    }

    @Override
    public Boolean isHardTerminationComplete() {
        return (hardBalanceTermCondition != null && hardBalanceTermCondition.getTerminationConditionMatched()) ||
                (hardDurationTermCondition != null && hardDurationTermCondition.getTerminationConditionMatched());
    }

    @Override
    public void determineTerminationScore(Boolean useSoftTerminationStrategy) {
        determineDurationTerminationScore();
        determineBalanceTerminationScore();

        final boolean softTerminationEnabled = isSoftTerminationComplete() && useSoftTerminationStrategy;
        final boolean hardTerminationEnabled = isHardTerminationComplete();

        if (hardTerminationEnabled || softTerminationEnabled) {
            isInsuranceComplete = true;
        }
    }

    @Override
    public void determineBalanceTerminationScore() {
        if (softBalanceTermCondition != null && softBalanceTermCondition.compareToValue(loanToHouseValueRatio)) {
            softBalanceTermCondition.setTerminationConditionMatched(true);
        }
        if (hardBalanceTermCondition != null && hardBalanceTermCondition.compareToValue(loanToHouseValueRatio)) {
            hardBalanceTermCondition.setTerminationConditionMatched(true);
        }
    }

    @Override
    public void determineDurationTerminationScore() {
        final Long durationProcessed = startDate.until(lastProcessedDate, durationTermUnits);
        if (softDurationTermCondition != null && softDurationTermCondition.compareToValue(durationProcessed)) {
            softDurationTermCondition.setTerminationConditionMatched(true);
        }
        if (hardDurationTermCondition != null && hardDurationTermCondition.compareToValue(durationProcessed)) {
            hardDurationTermCondition.setTerminationConditionMatched(true);
        }
    }

    @Override
    public void updateLoanValueAndHouseValue(final Double loanValue, final Double houseValue) {
        this.loanValue = RoundingUtil.roundValue(loanValue);
        this.houseValue = RoundingUtil.roundValue(houseValue);
        this.loanToHouseValueRatio = Percent.fromDecimal(loanValue/houseValue, 5);
    }

    @Override
    public void updateLoanValue(final Double loanValue) {
        updateLoanValueAndHouseValue(loanValue, houseValue);
    }

    @Override
    public void updateHouseValue(final Double houseValue) {
        updateLoanValueAndHouseValue(loanValue, houseValue);
    }

    @Override
    public void updateLastProcessedDate(final LocalDate lastProcessedDate) {
        setLastProcessedDate(lastProcessedDate);
    }

    @Override
    public double applyDecreasingBalance(double amount) {
        periodBalance = RoundingUtil.roundValue(periodBalance - amount);
        return periodBalance;
    }

    @Override
    public double applyIncreasingBalance(double amount) {
        periodBalance = RoundingUtil.roundValue(periodBalance + amount);
        return periodBalance;
    }

    @Override
    public void adjustYearlyRate(Double base, Percent percent) {

    }

    public void resetPeriod(final LocalDate startDate) {
        this.periodBalance = flatRateDurationInsuranceRate;
        this.lastProcessedDate = startDate;
    }

    public LocalDate getNextPeriodStartDate() {
        return paymentFrequency.getNextDate(lastProcessedDate);
    }
}
