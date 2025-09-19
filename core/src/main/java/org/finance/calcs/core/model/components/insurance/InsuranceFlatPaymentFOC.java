package org.finance.calcs.core.model.components.insurance;

import lombok.*;
import org.finance.calcs.core.enums.EInsuranceCalcType;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.percent.Percent;
import org.finance.calcs.core.util.PaymentFrequencyUtil;
import org.finance.calcs.core.util.RoundingUtil;

import java.time.LocalDate;

@Data
@Getter
@Builder
@AllArgsConstructor
public class InsuranceFlatPaymentFOC implements InsuranceFOC {
    @NonNull
    private final EInsuranceCalcType insuranceCalcType;

    @NonNull
    private final String insuranceType;

    @NonNull
    private final String insuranceProvider;

    @NonNull
    private EPaymentFrequency paymentFrequency;

    @NonNull
    private Double scheduledPaymentAmount;

    @NonNull
    private EPaymentFrequency insurancePeriodDuration;

    @NonNull
    private Double flatRateDurationInsuranceRate;

    @NonNull
    private final LocalDate startDate;

    private LocalDate periodStartDate;

    private LocalDate periodEndDate;

    private Double periodBalance;

    public InsuranceFlatPaymentFOC(final InsuranceTerms terms) {
        this.insuranceType = terms.getInsuranceType();
        this.insuranceProvider = terms.getInsuranceProvider();
        this.insuranceCalcType = terms.getInsuranceCalcType();
        this.startDate = terms.getStartDate();
        this.periodStartDate = startDate;
        this.periodEndDate = terms.getInsurancePeriodDuration().getNextDate(startDate);
        adjustScheduledPayment(
                terms.getPaymentFrequency(),
                terms.getInsurancePeriodDuration(),
                terms.getFlatRateAnnualInsuranceRate());
        this.periodBalance = flatRateDurationInsuranceRate;
    }

    @Override
    public Double getAnnualInsuranceRate() {
        return PaymentFrequencyUtil.paymentRateConverter(flatRateDurationInsuranceRate, insurancePeriodDuration, EPaymentFrequency.YEARLY);
    }

    @Override
    public Percent getAnnualInsuranceRatePercent() {
        return Percent.ZERO_PERCENT;
    }

    @Override
    public Double getInsurancePeriodBalance() {
        return periodBalance;
    }

    @Override
    public EPaymentFrequency getScheduledPaymentFrequency() {
        return paymentFrequency;
    }

    @Override
    public LocalDate getNextPeriodStartDate() {
        return insurancePeriodDuration.getNextDate(periodStartDate);
    }

    @Override
    public void updateInterestTerms(InsuranceTerms terms) {
        if (terms.getInsuranceCalcType() != this.insuranceCalcType) {
            throw new IllegalArgumentException("Mismatched insurance calculation type");
        }
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
    public void resetPeriod(final LocalDate startDate) {
        this.periodBalance = flatRateDurationInsuranceRate;
        this.periodStartDate = startDate;
        this.periodEndDate = insurancePeriodDuration.getNextDate(startDate);
    }

    @Override
    public void adjustYearlyRate(Double base, Percent percent) {
        adjustScheduledPayment(this.insurancePeriodDuration, this.paymentFrequency, base);
    }

    void adjustScheduledPayment(
            final EPaymentFrequency scheduledPaymentFrequency,
            final EPaymentFrequency insurancePeriodDuration,
            final Double insurancePeriodAmount
    ) {
        // TODO: Consider a change current period and adjust next period versions
        this.insurancePeriodDuration = insurancePeriodDuration;
        this.paymentFrequency = scheduledPaymentFrequency;
        this.flatRateDurationInsuranceRate = insurancePeriodAmount;
        this.scheduledPaymentAmount = RoundingUtil.roundValue(
                PaymentFrequencyUtil.paymentRateConverter(
                        flatRateDurationInsuranceRate, insurancePeriodDuration, paymentFrequency));

        // May cause bugs if performed midway through a period
        resetPeriod(startDate);
    }
}
