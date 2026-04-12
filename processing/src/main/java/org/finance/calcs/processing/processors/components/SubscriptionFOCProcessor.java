package org.finance.calcs.processing.processors.components;

import org.finance.calcs.core.model.components.loan.LoanFOC;
import org.finance.calcs.core.model.components.subscription.SubscriptionFOC;
import org.finance.calcs.core.util.RoundingUtil;
import org.finance.calcs.processing.common.processor.FOCProcessor;
import org.finance.calcs.processing.model.context.focContext.LoanFOCProcessorContext;
import org.finance.calcs.processing.model.context.focContext.SubscriptionFOCProcessorContext;

import java.time.LocalDate;

public class SubscriptionFOCProcessor implements FOCProcessor<SubscriptionFOCProcessorContext, SubscriptionFOC> {

    @Override
    public void processPayment(
            final SubscriptionFOCProcessorContext processorContext,
            final SubscriptionFOC subscription,
            final LocalDate processDate
    ) {
        final Double payment = processorContext.getPayment();
        final Double scheduledPayment = subscription.getScheduledPayment();
        final Double newBalance = subscription.applyPayment(payment);

        processorContext.setPaymentUsed(payment);
        processorContext.setPaymentRemaining(Math.max(RoundingUtil.roundValue(payment - scheduledPayment), 0.0));
        processorContext.setPeriodBalance(newBalance);
        processorContext.setPeriodReset(false);
    }

    @Override
    public void processEndOfPeriod(SubscriptionFOCProcessorContext processorContext, SubscriptionFOC subscription, LocalDate processDate) {
        final LocalDate nextPeriodStartDate = subscription.getNextPeriodStartDate();
        if (nextPeriodStartDate.isBefore(processDate)) {
            subscription.resetPeriod(nextPeriodStartDate);
            processorContext.setPeriodReset(true);
        }
    }
}
