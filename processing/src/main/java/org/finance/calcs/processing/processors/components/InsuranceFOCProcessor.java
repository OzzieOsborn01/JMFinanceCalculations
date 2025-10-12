package org.finance.calcs.processing.processors.components;

import org.finance.calcs.core.model.components.insurance.InsuranceFOC;
import org.finance.calcs.core.util.RoundingUtil;
import org.finance.calcs.processing.common.processor.FOCProcessor;
import org.finance.calcs.processing.model.context.InsuranceFOCProcessorContext;

import java.time.LocalDate;

public class InsuranceFOCProcessor implements FOCProcessor<InsuranceFOCProcessorContext, InsuranceFOC> {
    // TODO: Implement the following
    //  Assumes that Payment is greater or equal to scheduled payment, add exception logic
    //  Add Late logic
    @Override
    public void processPayment(final InsuranceFOCProcessorContext processorContext, final InsuranceFOC insurance, final LocalDate processDate) {

        final Double payment = processorContext.getPayment();
        final Double scheduledPayment = insurance.getScheduledPaymentAmount();

        final Double newBalance = insurance.applyPayment(payment);

        processorContext.setPaymentUsed(payment);
        processorContext.setPaymentRemaining(Math.max(RoundingUtil.roundValue(payment - scheduledPayment), 0.0));
        processorContext.setPeriodBalance(newBalance);
        processorContext.setPeriodReset(false);

        final LocalDate nextPeriodStartDate = insurance.getNextPeriodStartDate();
        if (nextPeriodStartDate.isBefore(processDate)) {
            insurance.resetPeriod(nextPeriodStartDate);
            processorContext.setPeriodReset(true);
        }
    }

    @Override
    public void processEndOfPeriod(final InsuranceFOCProcessorContext processorContext, final InsuranceFOC insurance, final LocalDate processDate) {

    }
}
