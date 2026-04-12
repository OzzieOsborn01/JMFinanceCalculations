package org.finance.calcs.processing.model.context.foContext;

import lombok.*;

import org.finance.calcs.core.util.RoundingUtil;
import org.finance.calcs.processing.model.context.focContext.InsuranceFOCProcessorContext;
import org.finance.calcs.processing.model.context.focContext.LoanFOCProcessorContext;
import org.finance.calcs.processing.model.context.focContext.MortgageInsuranceFOCProcessorContext;
import org.finance.calcs.processing.model.context.focContext.SubscriptionFOCProcessorContext;
import org.finance.calcs.processing.model.obligationPeriod.MortgageFOPeriod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class MortgageFOProcessorContext {
    @Builder.Default
    List<MortgageFOPeriod> mortgagePeriodList = new ArrayList<>();

    @Builder.Default
    Double currentPayment = 0.0;

    @Builder.Default
    Double totalPayments = 0.0;

    @Builder.Default
    Double totalLoanContribution = 0.0;

    @Builder.Default
    Double totalInterestContribution = 0.0;

    @Builder.Default
    Double totalInsuranceContribution = 0.0;

    @Builder.Default
    Double totalMortgageInsuranceContribution = 0.0;

    @Builder.Default
    Double totalHoaContribution = 0.0;

    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    LocalDate lastProcessedDate;

    @Builder.Default
    private Double holdingAccountBalance = 0.0;

    @Builder.Default
    private Integer processUntilPeriod = -1;

    @Builder.Default
    private LocalDate processUntilDate = null;

    public double increaseHoldingAccountBalance(final double balanceDelta) {
        holdingAccountBalance += balanceDelta;
        return holdingAccountBalance;
    }

    public double decreaseHoldingAccountBalance(final double balanceDelta) {
        holdingAccountBalance -= balanceDelta;
        return holdingAccountBalance;
    }

    public int periodsProcessed() {
        return mortgagePeriodList.size();
    }

    public LoanFOCProcessorContext getLoanFOProcessorContext(final double loanContributionPayment) {
        return LoanFOCProcessorContext.builder()
                .payment(loanContributionPayment)
                .build();
    }

    public InsuranceFOCProcessorContext getHomeInsuranceFOCProcessorContext(final double insuranceContributionPayment) {
        return InsuranceFOCProcessorContext.builder()
                .payment(insuranceContributionPayment)
                .build();
    }

    public MortgageInsuranceFOCProcessorContext getMortgageInsuranceFOCProcessorContext(final double insuranceContributionPayment, final double houseValue, final double loanValue) {
        return MortgageInsuranceFOCProcessorContext.builder()
                .payment(insuranceContributionPayment)
                .houseValue(houseValue)
                .loanValue(loanValue)
                .build();
    }

    public SubscriptionFOCProcessorContext getHoaFeeFOCProcessorContext(final double hoaFeeContributionPayment) {
        return SubscriptionFOCProcessorContext.builder()
                .payment(hoaFeeContributionPayment)
                .build();
    }

    public void incrementMortgageContext(final MortgageFOPeriod mortgageFOPeriod) {
        mortgagePeriodList.add(mortgageFOPeriod);
        totalPayments = RoundingUtil.roundValue(totalPayments + mortgageFOPeriod.getPayment());
        totalInterestContribution = RoundingUtil.roundValue(totalInterestContribution + mortgageFOPeriod.getInterestContribution());
        totalLoanContribution = RoundingUtil.roundValue(totalLoanContribution + mortgageFOPeriod.getLoanContribution());
        totalInsuranceContribution = RoundingUtil.roundValue(totalInsuranceContribution + mortgageFOPeriod.getInsuranceContribution());
        totalMortgageInsuranceContribution = RoundingUtil.roundValue(totalMortgageInsuranceContribution + mortgageFOPeriod.getMortgageInsuranceContribution());
        totalHoaContribution = RoundingUtil.roundValue(totalHoaContribution + mortgageFOPeriod.getHoaContribution());
        lastProcessedDate = mortgageFOPeriod.getEndDate();
    }
}
