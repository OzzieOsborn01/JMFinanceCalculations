package org.finance.calcs.core.model.components.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.enums.ESubscriptionDurationType;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.finance.calcs.core.model.obligationBase.FinancialObligationComponent;
import org.finance.calcs.core.util.RoundingUtil;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
public class SubscriptionFOC implements FinancialObligationComponent {
    private String id;
    private String vendor;
    private String description;

    private ESubscriptionDurationType durationType;

    private PaymentCalculation paymentCalculation;

    private LocalDate startDate;

    private LocalDate lastProcessedDate;

    private LocalDate periodEndDate;

    private Double periodBalance;

    private EPaymentFrequency scheduledPaymentFrequency;

    public SubscriptionFOC(final SubscriptionTerms subscription) {
        id = subscription.getId();
        vendor = subscription.getVendor();
        description = subscription.getDescription();
        durationType = subscription.getDurationType();
        paymentCalculation = subscription.getPaymentCalculation();
        startDate = subscription.getStartDate();
        lastProcessedDate = subscription.getStartDate();
        periodBalance = paymentCalculation.getPaymentFlatRate();
        scheduledPaymentFrequency = subscription.getScheduledPaymentFrequency();
        periodEndDate = scheduledPaymentFrequency.getNextDate(startDate).minusDays(1);
    }

    @Override
    public double applyDecreasingBalance(double decreaseAmount) {
        periodBalance = RoundingUtil.roundValue(periodBalance - decreaseAmount);
        return periodBalance;
    }

    @Override
    public double applyIncreasingBalance(double increaseAmount) {
        periodBalance = RoundingUtil.roundValue(periodBalance + increaseAmount);
        return periodBalance;
    }

    public void resetPeriod(final LocalDate lastProcessedDate) {
        // TODO: Handle different last processed days
        this.periodBalance = paymentCalculation.getPaymentFlatRate();
        this.lastProcessedDate = lastProcessedDate;
        this.periodEndDate = scheduledPaymentFrequency.getNextDate(lastProcessedDate);
    }
}
