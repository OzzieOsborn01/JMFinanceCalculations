package org.finance.calcs.processing.processors.obligations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.finance.calcs.core.model.components.insurance.InsuranceFOC;
import org.finance.calcs.core.model.components.loan.LoanFOC;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceFOC;
import org.finance.calcs.core.model.components.subscription.SubscriptionFOC;
import org.finance.calcs.core.model.obligations.MortgageFO;
import org.finance.calcs.core.util.RoundingUtil;
import org.finance.calcs.processing.common.processor.FOProcessor;
import org.finance.calcs.processing.model.context.focContext.InsuranceFOCProcessorContext;
import org.finance.calcs.processing.model.context.focContext.LoanFOCProcessorContext;
import org.finance.calcs.processing.model.context.foContext.MortgageFOProcessorContext;
import org.finance.calcs.processing.model.context.focContext.MortgageInsuranceFOCProcessorContext;
import org.finance.calcs.processing.model.context.focContext.SubscriptionFOCProcessorContext;
import org.finance.calcs.processing.model.obligationPeriod.MortgageFOPeriod;
import org.finance.calcs.processing.processors.components.InsuranceFOCProcessor;
import org.finance.calcs.processing.processors.components.LoanFOCProcessor;
import org.finance.calcs.processing.processors.components.MortgageInsuranceFOCProcessor;
import org.finance.calcs.processing.processors.components.SubscriptionFOCProcessor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class MortgageFOProcessor implements FOProcessor<MortgageFOProcessorContext, MortgageFO> {

    @NonNull
    final LoanFOCProcessor loanFOCProcessor;

    @NonNull
    final InsuranceFOCProcessor insuranceFOCProcessor;

    @NonNull
    final MortgageInsuranceFOCProcessor mortgageInsuranceFOCProcessor;

    @NonNull
    final SubscriptionFOCProcessor subscriptionFOCProcessor;

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
        final PaymentAndRemainingBalance homeInsurancePRB = calculateHomeInsuranceBalancePayment(
                requiredPayment,
                mortgageFO.getHomeInsuranceComponent());
        final InsuranceFOCProcessorContext homeInsuranceContext = processorContext.getHomeInsuranceFOCProcessorContext(
                homeInsurancePRB.obligationPayment);
        insuranceFOCProcessor.processPayment(homeInsuranceContext, mortgageFO.getHomeInsuranceComponent(), nextProcessDate);

        final PaymentAndRemainingBalance hoaFeePRB = calculateHOAFeeBalancePayment(
                homeInsurancePRB.remainingPayment,
                mortgageFO.getHoaFeesComponent());

        final SubscriptionFOCProcessorContext hoaFeeContext = processorContext.getHoaFeeFOCProcessorContext(hoaFeePRB.obligationPayment);
        if (mortgageFO.getHoaFeesComponent().isPresent()) {
            subscriptionFOCProcessor.processPayment(hoaFeeContext, mortgageFO.getHoaFeesComponent().get(), nextProcessDate);
        }

        final PaymentAndRemainingBalance mortgageInsurancePRB = calculateMortgageInsuranceBalancePayment(homeInsurancePRB.remainingPayment, mortgageFO.getMortgageInsuranceComponent());
        final MortgageInsuranceFOCProcessorContext mortgageInsuranceFOCProcessorContext =
                processorContext.getMortgageInsuranceFOCProcessorContext(
                        mortgageInsurancePRB.obligationPayment,
                        mortgageFO.getHouseValue(),
                        mortgageFO.getLoanBalance()
                );
        mortgageInsuranceFOCProcessor.processPayment(mortgageInsuranceFOCProcessorContext, mortgageFO.getMortgageInsuranceComponent(), nextProcessDate);

        final boolean isLastPeriod = isLastPeriod(mortgageFO);
        final PaymentAndRemainingBalance loanPRB = calculateLoanBalancePayment(mortgageInsurancePRB.remainingPayment, mortgageFO.getLoanComponent(), isLastPeriod, nextProcessDate);
        final LoanFOCProcessorContext loanProcessorContext = processorContext.getLoanFOProcessorContext(loanPRB.obligationPayment);
        loanFOCProcessor.processPayment(loanProcessorContext, mortgageFO.getLoanComponent(), nextProcessDate);

        final MortgageFOPeriod period = MortgageFOPeriod.builder()
                .periodNumber(mortgagePeriodNum)
                .endDate(nextProcessDate)
                .interestContribution(loanProcessorContext.getInterestDelta())
                .loanContribution(loanProcessorContext.getPrincipalDelta())
                .newLoanBalance(loanProcessorContext.getPrincipalBalance())
                .insuranceContribution(homeInsuranceContext.getPaymentUsed())
                .mortgageInsuranceContribution(mortgageInsuranceFOCProcessorContext.getPaymentUsed())
                .hoaContribution(hoaFeeContext.getPaymentUsed())
                .payment(requiredPayment)
                .startDate(mortgageFO.getLastProcessedDate())
                .endDate(nextProcessDate)
                .build();

        processorContext.incrementMortgageContext(period);
        mortgageFO.setLastProcessedDate(period.getEndDate());

        // Implement End of process
        loanFOCProcessor.processEndOfPeriod(loanProcessorContext, mortgageFO.getLoanComponent(), nextProcessDate);
        insuranceFOCProcessor.processEndOfPeriod(homeInsuranceContext, mortgageFO.getHomeInsuranceComponent(), nextProcessDate);
        mortgageInsuranceFOCProcessor.processEndOfPeriod(mortgageInsuranceFOCProcessorContext, mortgageFO.getMortgageInsuranceComponent(), nextProcessDate);
        mortgageFO.getHoaFeesComponent()
                .ifPresent(foc -> subscriptionFOCProcessor.processEndOfPeriod(hoaFeeContext, foc, nextProcessDate));
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
        final boolean isLastPeriod = isLastPeriod(mortgageFO);
        double loanPayment = loan.getScheduledPaymentPerPeriod();

        if (isLastPeriod) {
            loanPayment = loan.getLoanCurrentPrinciple() + loanFOCProcessor.getAccruedInterestForPeriod(loan, processDate);
        }

        List<Double> paymentList = List.of(
                loanPayment,
                mortgageFO.getHomeInsuranceRegularPayment(),
                mortgageFO.getMortgageInsuranceRegularPayment(),
                mortgageFO.getHoaFeesRegularPayment()
            );

        return RoundingUtil.roundValue(paymentList.stream().reduce(0.0, Double::sum));
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

    @AllArgsConstructor
    @Getter
    class PaymentAndRemainingBalance {
        private final double obligationPayment;
        private final double remainingPayment;
    }

    private PaymentAndRemainingBalance calculateLoanBalancePayment(final Double remainingPayment, final LoanFOC loanFOC, final boolean isLastPeriod, final LocalDate processDate) {
        double loanPayment = loanFOC.getScheduledPaymentPerPeriod();

        if (isLastPeriod) {
            loanPayment = loanFOC.getLoanCurrentPrinciple() + loanFOCProcessor.getAccruedInterestForPeriod(loanFOC, processDate);
        }

        final double obligationPayment = RoundingUtil.roundValue(loanPayment);
        final double newRemainingPayment = RoundingUtil.roundValueWithMinCap(remainingPayment - obligationPayment, 0.0);
        return new PaymentAndRemainingBalance(obligationPayment, newRemainingPayment);
    }

    private PaymentAndRemainingBalance calculateMortgageInsuranceBalancePayment(final Double remainingPayment, final MortgageInsuranceFOC insuranceFOC) {
        final double obligationPayment = Math.min(insuranceFOC.getScheduledPayment(), remainingPayment);
        final double newRemainingPayment = RoundingUtil.roundValueWithMinCap(remainingPayment - obligationPayment, 0.0);
        return new PaymentAndRemainingBalance(obligationPayment, newRemainingPayment);
    }

    private PaymentAndRemainingBalance calculateHomeInsuranceBalancePayment(final Double remainingPayment, final InsuranceFOC insuranceFOC) {
        final double obligationPayment = Math.min(insuranceFOC.getScheduledPaymentAmount(), remainingPayment);
        final double newRemainingPayment = RoundingUtil.roundValueWithMinCap(remainingPayment - obligationPayment, 0.0);
        return new PaymentAndRemainingBalance(obligationPayment, newRemainingPayment);
    }

    private PaymentAndRemainingBalance calculateHOAFeeBalancePayment(final Double remainingPayment, final Optional<SubscriptionFOC> hoaFee) {
        if (hoaFee.isEmpty()) {
            return new PaymentAndRemainingBalance(0.0, remainingPayment);
        }

        final double obligationPayment = Math.min(hoaFee.get().getScheduledPayment(), remainingPayment);
        final double newRemainingPayment = RoundingUtil.roundValueWithMinCap(remainingPayment - obligationPayment, 0.0);
        return new PaymentAndRemainingBalance(obligationPayment, newRemainingPayment);
    }
}
