package org.finance.calcs.processing.processors.components;

import org.finance.calcs.core.model.components.loan.LoanFOC;
import org.finance.calcs.core.util.InterestFrequencyUtil;
import org.finance.calcs.core.util.RoundingUtil;
import org.finance.calcs.processing.common.processor.FOCProcessor;
import org.finance.calcs.processing.model.context.LoanFOCProcessorContext;

import java.time.LocalDate;

public class LoanFOCProcessor implements FOCProcessor<LoanFOCProcessorContext, LoanFOC> {
    @Override
    public void processPayment(LoanFOCProcessorContext processorContext, LoanFOC loan, LocalDate processDate) {
        // Store local values
        final Double payment = processorContext.getPayment();
        final Double loanBalance = loan.getLoanCurrentPrinciple();

        // Calculate accumulation and adjustments
        final double periodInterestAccumulation = getAccruedInterestForPeriod(loan, processDate);

        if (payment <= periodInterestAccumulation) {
            // TODO handle edge case only partially accounted for
            final double interestDelta = periodInterestAccumulation - payment;
            processorContext.setPrincipalBalance(loanBalance);
            processorContext.setPrincipalDelta(0.0);
            processorContext.setInterestDelta(payment);
            processorContext.setInterestRemaining(interestDelta);
            processorContext.setPaymentRemaining(0.0);
            processorContext.setPaymentUsed(payment);
            return;
        }

        final double paymentAfterInterest = RoundingUtil.roundValue(payment - periodInterestAccumulation);

        Double adjustedLoanBalance = 0.0;
        if (paymentAfterInterest <= loanBalance && paymentAfterInterest > 0.0 && loanBalance > 0.0 && loan.getLoanTermMonthsRemaining() > 1) {
            adjustedLoanBalance = loan.applyPayment(paymentAfterInterest);
        } else if (paymentAfterInterest > 0.0 && loanBalance > 0.0 || loan.getLoanTermMonthsRemaining() <= 1) {
            adjustedLoanBalance = loan.applyPayment(loanBalance);
        } else {
            throw new RuntimeException("Loan Balance or Payment is below zero");
        }

        loan.setLastProcessedDate(processDate);

        double paymentRemaining = Math.max(0.0, paymentAfterInterest - loanBalance);
        processorContext.setPrincipalBalance(adjustedLoanBalance);
        processorContext.setPrincipalDelta(paymentAfterInterest);
        processorContext.setInterestDelta(periodInterestAccumulation);
        processorContext.setInterestRemaining(0.0);
        processorContext.setPaymentRemaining(paymentRemaining);
        processorContext.setPaymentUsed(payment - paymentRemaining);
    }

    @Override
    public void processEndOfPeriod(LoanFOCProcessorContext processorContext, LoanFOC loan, LocalDate processDate) {
        loan.setLastProcessedDate(processDate);
        loan.reduceMonthsLeft();
    }

    public double getAccruedInterestForPeriod(LoanFOC loan, LocalDate processDate) {
        return InterestFrequencyUtil.calculateInterestAdditionForPeriod(
                loan.getInterestFrequency(),
                loan.getLoanYearlyInterestRate().asDouble(),
                loan.getLoanCurrentPrinciple(),
                loan.getLastProcessedDate(),
                processDate
        );
    }
}
