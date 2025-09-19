package org.finance.calcs.processing.processors.obligations;

import lombok.AllArgsConstructor;
import org.finance.calcs.core.model.components.insurance.InsuranceFOC;
import org.finance.calcs.core.model.components.interest.InterestFOC;
import org.finance.calcs.core.model.components.loan.LoanFOC;
import org.finance.calcs.core.model.obligations.MortgageFO;
import org.finance.calcs.core.util.RoundingUtil;
import org.finance.calcs.processing.common.processor.FOProcessor;
import org.finance.calcs.processing.model.context.InsuranceFOCProcessorContext;
import org.finance.calcs.processing.model.context.LoanFOCProcessorContext;
import org.finance.calcs.processing.model.context.MortgageFOProcessorContext;
import org.finance.calcs.processing.model.obligationPeriod.MortgageFOPeriod;
import org.finance.calcs.processing.processors.components.InsuranceFOCProcessor;
import org.finance.calcs.processing.processors.components.LoanFOCProcessor;

import java.time.LocalDate;

@AllArgsConstructor
public class MortgageFOProcessor implements FOProcessor<MortgageFOProcessorContext, MortgageFO> {

    final LoanFOCProcessor loanFOCProcessor;

    final InsuranceFOCProcessor insuranceFOCProcessor;

    @Override
    public void processToCompletion(final MortgageFOProcessorContext processorContext, final MortgageFO mortgageFO) {
        while (!mortgageFO.getIsMortgageComplete()) {
            final boolean isLastPeriod = isLastPeriod(mortgageFO);
            processNextPeriod(processorContext, mortgageFO);

            if (isLastPeriod) {
                mortgageFO.setIsMortgageComplete(true);
            }
        }
    }

    @Override
    public void processNextPeriod(MortgageFOProcessorContext processorContext, MortgageFO mortgageFO) {
        final int mortgagePeriodNum = processorContext.periodsProcessed() + 1;
        final LocalDate nextProcessDate = mortgageFO.getNextPeriodEndDate();
        final Double requiredPayment = getRequiredPayment(mortgageFO, nextProcessDate);
        // TODO: Collect all payments - Assume each payment is complete
        // TODO: Collect all payment for each day

        processorContext.setCurrentPayment(requiredPayment);
        final InsuranceFOCProcessorContext homeInsuranceContext = processorContext.getHomeInsuranceFOCProcessorContext(requiredPayment);
        insuranceFOCProcessor.process(homeInsuranceContext, mortgageFO.getHomeInsuranceComponent(), nextProcessDate);

        final LoanFOCProcessorContext loanProcessorContext = processorContext.getLoanFOProcessorContext(homeInsuranceContext.getPaymentRemaining());
        loanFOCProcessor.process(loanProcessorContext, mortgageFO.getLoanComponent(), nextProcessDate);

        final MortgageFOPeriod period = MortgageFOPeriod.builder()
                .periodNumber(mortgagePeriodNum)
                .endDate(nextProcessDate)
                .interestContribution(loanProcessorContext.getInterestDelta())
                .loanContribution(loanProcessorContext.getPrincipalDelta())
                .newLoanBalance(loanProcessorContext.getPrincipalBalance())
                .insuranceContribution(homeInsuranceContext.getPaymentUsed())
                .payment(requiredPayment)
                .startDate(mortgageFO.getLastProcessedDate())
                .endDate(nextProcessDate)
                .build();

        processorContext.incrementMortgageContext(period);
        mortgageFO.setLastProcessedDate(period.getEndDate());
    }

    @Override
    public void processPeriodRange(MortgageFOProcessorContext processorContext, MortgageFO mortgageFO, LocalDate endProcessDate) {

    }

    @Override
    public void processNumberOfPeriods(MortgageFOProcessorContext processorContext, MortgageFO mortgageFO, int periodsToProcess) {

        while (shouldProcessPeriod(periodsToProcess, processorContext.periodsProcessed()) && !mortgageFO.getIsMortgageComplete()) {
            final boolean isLastPeriod = isLastPeriod(mortgageFO);
            processNextPeriod(processorContext, mortgageFO);

            if (isLastPeriod) {
                mortgageFO.setIsMortgageComplete(true);
            }
        }
    }

    public double getRequiredPayment(MortgageFO mortgageFO, LocalDate processDate) {
        final LoanFOC loan = mortgageFO.getLoanComponent();
        final InsuranceFOC homeInsurance = mortgageFO.getHomeInsuranceComponent();
        final boolean isLastPeriod = isLastPeriod(mortgageFO);
        double loanPayment = loan.getScheduledPaymentPerPeriod();

        if (isLastPeriod) {
            loanPayment = loan.getLoanCurrentPrinciple() + loanFOCProcessor.getAccruedInterestForPeriod(loan, processDate);
        }

        return RoundingUtil.roundValue(loanPayment + homeInsurance.getScheduledPaymentAmount());
    }

    private boolean isLastPeriod(MortgageFO mortgageFO) {
        final LoanFOC loan = mortgageFO.getLoanComponent();
        return loan.getLoanTermMonthsRemaining() == 1;
    }

    private boolean shouldProcessPeriod(Integer periodsToProcess, Integer periodsProcessed) {
        if (periodsToProcess < 0) {
            return true;
        }
        return periodsToProcess > periodsProcessed;
    }
}
