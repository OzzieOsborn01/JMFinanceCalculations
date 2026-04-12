package org.finance.calcs.processing.processors.components;

import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceFOC;
import org.finance.calcs.core.util.RoundingUtil;
import org.finance.calcs.processing.common.processor.FOCProcessor;
import org.finance.calcs.processing.model.context.focContext.MortgageInsuranceFOCProcessorContext;

import java.time.LocalDate;

public class MortgageInsuranceFOCProcessor implements FOCProcessor<MortgageInsuranceFOCProcessorContext, MortgageInsuranceFOC> {
    @Override
    public void processPayment(MortgageInsuranceFOCProcessorContext processorContext, MortgageInsuranceFOC mortgageInsurance, LocalDate processDate) {
        final Double payment = processorContext.getPayment();
        if (mortgageInsurance.isInsuranceComplete()) {
            processorContext.setPaymentUsed(0.0);
            processorContext.setPaymentRemaining(payment);
            processorContext.setSkipped(true);
            return;
        }


        final Double scheduledPayment = mortgageInsurance.getScheduledPayment();

        final Double newBalance = mortgageInsurance.applyPayment(payment);

        processorContext.setPaymentUsed(payment);
        processorContext.setPaymentRemaining(Math.max(RoundingUtil.roundValue(payment - scheduledPayment), 0.0));
        processorContext.setPeriodBalance(newBalance);
        processorContext.setSkipped(false);
    }

    @Override
    public void processEndOfPeriod(MortgageInsuranceFOCProcessorContext processorContext, MortgageInsuranceFOC mortgageInsurance, LocalDate processDate) {
        final LocalDate nextPeriodStartDate = mortgageInsurance.getNextPeriodStartDate();
        if (nextPeriodStartDate.isBefore(processDate)) {
            mortgageInsurance.resetPeriod(nextPeriodStartDate);
            mortgageInsurance.determineTerminationScore(processorContext.getUseSoftTerminationStrategy());
        }
        processUpdateMortgageInsuranceConditions(processorContext, mortgageInsurance);
    }

    public void processUpdateMortgageInsuranceConditions(MortgageInsuranceFOCProcessorContext processorContext, MortgageInsuranceFOC mortgageInsurance) {
        mortgageInsurance.updateLoanValueAndHouseValue(processorContext.getLoanValue(), processorContext.getHouseValue());
        mortgageInsurance.determineTerminationScore(processorContext.getUseSoftTerminationStrategy());
    }

}
