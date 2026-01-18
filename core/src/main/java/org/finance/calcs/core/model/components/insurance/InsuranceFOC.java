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

@Data
@Builder
@AllArgsConstructor
public class InsuranceFOC implements FinancialObligationComponent {
    @NonNull
    private final PaymentCalculation paymentCalculation;

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

    public InsuranceFOC(final InsuranceTerms terms) {
        this.insuranceType = terms.getInsuranceType();
        this.insuranceProvider = terms.getInsuranceProvider();
        this.paymentCalculation = terms.getPaymentCalculation();
        this.startDate = terms.getStartDate();
        this.periodStartDate = startDate;
        this.periodEndDate = terms.getInsurancePeriodDuration().getNextDate(startDate);
        adjustScheduledPayment(terms.getPaymentFrequency(), terms.getInsurancePeriodDuration(), terms.getPaymentCalculation());
        this.periodBalance = flatRateDurationInsuranceRate;
    }

    public Double getAnnualInsuranceRate() {
        return PaymentFrequencyUtil.paymentRateConverter(flatRateDurationInsuranceRate, insurancePeriodDuration, EPaymentFrequency.YEARLY);
    }

    public Percent getInsuranceRatePercent() {
        return paymentCalculation.getPaymentPercentRate();
    }

    public Double getInsurancePeriodBalance() {
        return periodBalance;
    }

    public EPaymentFrequency getScheduledPaymentFrequency() {
        return paymentFrequency;
    }

    public LocalDate getNextPeriodStartDate() {
        return insurancePeriodDuration.getNextDate(periodStartDate);
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

    public void resetPeriod(final LocalDate startDate) {
        this.periodBalance = flatRateDurationInsuranceRate;
        this.periodStartDate = startDate;
        this.periodEndDate = insurancePeriodDuration.getNextDate(startDate);
    }

    void adjustScheduledPayment(
            final EPaymentFrequency scheduledPaymentFrequency,
            final EPaymentFrequency insurancePeriodDuration,
            final PaymentCalculation paymentCalculation
    ) {
        // TODO: Consider a change current period and adjust next period versions
        this.insurancePeriodDuration = insurancePeriodDuration;
        this.paymentFrequency = scheduledPaymentFrequency;
        this.flatRateDurationInsuranceRate = paymentCalculation.getPaymentFlatRate();
        this.scheduledPaymentAmount = RoundingUtil.roundValue(
                PaymentFrequencyUtil.paymentRateConverter(
                        flatRateDurationInsuranceRate, insurancePeriodDuration, paymentFrequency));

        // May cause bugs if performed midway through a period
        resetPeriod(startDate);
    }
}
