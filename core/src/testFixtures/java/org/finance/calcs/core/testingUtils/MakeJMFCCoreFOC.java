package org.finance.calcs.core.testingUtils;

import org.finance.calcs.core.enums.*;
import org.finance.calcs.core.factories.InsuranceFOCFactory;
import org.finance.calcs.core.factories.MortgageInsuranceFOCFactory;
import org.finance.calcs.core.model.components.insurance.InsuranceFOC;
import org.finance.calcs.core.model.components.insurance.InsuranceTerms;
import org.finance.calcs.core.model.components.interest.InterestFOC;
import org.finance.calcs.core.model.components.loan.LoanFOC;
import org.finance.calcs.core.model.components.loan.LoanTerms;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceFOC;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceTerms;
import org.finance.calcs.core.model.components.subscription.SubscriptionFOC;
import org.finance.calcs.core.model.components.subscription.SubscriptionTerms;
import org.finance.calcs.core.model.metadata.Debt;
import org.finance.calcs.core.model.metadata.ObligationTerminationStrategy;
import org.finance.calcs.core.model.metadata.PersonalDetails;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.finance.calcs.core.percent.Percent;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

public final class MakeJMFCCoreFOC {
    final static Random random = new Random();

    public static LoanFOC aLoanFOC(final double loanAmount, final Percent interest, final int loanMonths) {
        return new LoanFOC(aLoanTerms(loanAmount, interest, loanMonths));
    }
    public static LoanFOC aRandomLoanFOC() {
        return new LoanFOC(aRandomLoanTerms());
    }
    public static LoanFOC aLoanFOC() {
        return new LoanFOC(aLoanTerms());
    }
    public static LoanTerms aLoanTerms(final double loanAmount, final Percent interest, final int loanMonths) {
        return LoanTerms.builder()
                .loanInitialAmount(loanAmount)
                .loanYearlyInterestRate(interest)
                .loanTermMonths(loanMonths)
                .build();
    }
    public static LoanTerms aRandomLoanTerms() {
        return LoanTerms.builder()
                .loanInitialAmount((double)random.nextInt(50000, 975001))
                .loanYearlyInterestRate(Percent.fromDecimal(random.nextDouble(0.01, 0.15)))
                .loanTermMonths(random.nextInt(12, 360))
                .build();
    }
    public static LoanTerms aLoanTerms() {
        return LoanTerms.builder()
                .loanInitialAmount(775000.0)
                .loanYearlyInterestRate(Percent.fromPercent(6.125, 5))
                .loanTermMonths(360)
                .build();
    }

    public static PersonalDetails aPersonalDetails(final double grossIncome, final double netIncome, final List<Debt> debts) {
        return PersonalDetails.builder()
                .grossIncome(grossIncome)
                .netIncome(netIncome)
                .debts(debts)
                .build();
    }

    public static PersonalDetails aPersonalDetails() {
        return PersonalDetails.builder()
                .grossIncome(200000.0)
                .netIncome(150000.0)
                .debts(List.of())
                .build();
    }

    public static InterestFOC aInterestFOC() {
        return new InterestFOC();
    }

    public static InsuranceFOC aFlatPaymentInsuranceFOC(
            final String insuranceType,
            final String insuranceProvider,
            final EPaymentFrequency paymentFrequency,
            final EPaymentFrequency periodFrequency,
            final Double paymentRate
    ) {
        return InsuranceFOCFactory.buildInsuranceFOC(aFlatPaymentInsuranceTerms(
                insuranceType,
                insuranceProvider,
                paymentFrequency,
                periodFrequency,
                paymentRate));
    }

    public static InsuranceFOC aRandomFlatPaymentInsuranceFOC() {
        return InsuranceFOCFactory.buildInsuranceFOC(aRandomFlatPaymentInsuranceTerms());
    }

    public static InsuranceFOC aFlatPaymentInsuranceFOC() {
        return InsuranceFOCFactory.buildInsuranceFOC(aFlatPaymentInsuranceTerms());
    }

    public static InsuranceTerms aFlatPaymentInsuranceTerms(
            final String insuranceType,
            final String insuranceProvider,
            final EPaymentFrequency paymentFrequency,
            final EPaymentFrequency periodFrequency,
            final Double paymentRate
    ) {
        return InsuranceTerms.builder()
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType(insuranceType)
                .insuranceProvider(insuranceProvider)
                .insurancePeriodDuration(periodFrequency)
                .paymentFrequency(paymentFrequency)
                .build();
    }
    public static InsuranceTerms aRandomFlatPaymentInsuranceTerms() {
        return InsuranceTerms.builder()
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .insurancePeriodDuration(EPaymentFrequency.YEARLY)
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .build();
    }
    public static InsuranceTerms aFlatPaymentInsuranceTerms() {
        return InsuranceTerms.builder()
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .insurancePeriodDuration(EPaymentFrequency.YEARLY)
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .build();
    }

    public static MortgageInsuranceFOC aPrivateMortgageInsuranceFOC(
            final Double houseValue,
            final Double loanValue,
            final PaymentCalculation paymentCalculation,
            final ObligationTerminationStrategy<Percent> softBalanceTermCondition,
            final ObligationTerminationStrategy<Percent> hardBalanceTermCondition
    ) {
        return MortgageInsuranceFOCFactory.createMortgageInsuranceFOC(aPrivateMortgageInsuranceTerms(houseValue, loanValue, paymentCalculation, softBalanceTermCondition, hardBalanceTermCondition));
    }

    public static MortgageInsuranceFOC aPrivateMortgageInsuranceFOC() {
        return MortgageInsuranceFOCFactory.createMortgageInsuranceFOC(aPrivateMortgageInsuranceTerms());
    }

    public static MortgageInsuranceTerms aPrivateMortgageInsuranceTerms(
            final Double houseValue,
            final Double loanValue,
            final PaymentCalculation paymentCalculation,
            final ObligationTerminationStrategy<Percent> softBalanceTermCondition,
            final ObligationTerminationStrategy<Percent> hardBalanceTermCondition
    ) {
        return MortgageInsuranceTerms.builder()
                .mortgageInsuranceType(EMortgageInsuranceType.PRIVATE_MORTGAGE_INSURANCE)
                .houseValue(houseValue)
                .loanValue(loanValue)
                .paymentCalculation(paymentCalculation)
                .softBalanceTermCondition(softBalanceTermCondition)
                .hardBalanceTermCondition(hardBalanceTermCondition)
                .build();
    }
    public static MortgageInsuranceTerms aPrivateMortgageInsuranceTerms() {
        return MortgageInsuranceTerms.builder()
                .mortgageInsuranceType(EMortgageInsuranceType.PRIVATE_MORTGAGE_INSURANCE)
                .houseValue(875000.0)
                .loanValue(775000.0)
                .paymentCalculation(new PaymentCalculation(100.0))
                .terminationConditionFactor(ETerminationConditionFactor.OBLIGATION_COMPLETED)
                .softBalanceTermCondition(ObligationTerminationStrategy.<Percent>builder()
                        .comparisonMethod(ETerminationConditionComparison.LESS_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(Percent.fromPercent(20.0, 4).decreaseReversePercentage())
                        .terminationConditionDescription("Soft Balance Condition")
                        .build())
                .hardBalanceTermCondition(ObligationTerminationStrategy.<Percent>builder()
                        .comparisonMethod(ETerminationConditionComparison.LESS_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(Percent.fromPercent(22.0, 4).decreaseReversePercentage())
                        .terminationConditionDescription("Hard Balance Condition")
                        .build())
                .build();
    }
    public static MortgageInsuranceTerms aMortgageInsurancePremiumEndingDurationTerms() {
        return MortgageInsuranceTerms.builder()
                .mortgageInsuranceType(EMortgageInsuranceType.MORTGAGE_INSURANCE_PREMIUMS)
                .houseValue(875000.0)
                .loanValue(775000.0)
                .paymentCalculation(new PaymentCalculation(100.0))
                .terminationConditionFactor(ETerminationConditionFactor.DURATION_COMPLETED)
                .softDurationTermCondition(ObligationTerminationStrategy.<Long>builder()
                        .comparisonMethod(ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(9L)
                        .terminationConditionDescription("Soft Duration Condition")
                        .build())
                .hardDurationTermCondition(ObligationTerminationStrategy.<Long>builder()
                        .comparisonMethod(ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(11L)
                        .terminationConditionDescription("Hard Duration Condition")
                        .build())
                .build();
    }
    public static MortgageInsuranceTerms aMortgageInsurancePremiumNotEndingDurationTerms() {
        return MortgageInsuranceTerms.builder()
                .mortgageInsuranceType(EMortgageInsuranceType.MORTGAGE_INSURANCE_PREMIUMS)
                .houseValue(875000.0)
                .loanValue(775000.0)
                .paymentCalculation(new PaymentCalculation(100.0))
                .terminationConditionFactor(ETerminationConditionFactor.NOT_ENDING)
                .softDurationTermCondition(ObligationTerminationStrategy.<Long>builder()
                        .comparisonMethod(ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(10L)
                        .terminationConditionDescription("Soft Duration Condition")
                        .build())
                .hardDurationTermCondition(ObligationTerminationStrategy.<Long>builder()
                        .comparisonMethod(ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(15L)
                        .terminationConditionDescription("Hard Duration Condition")
                        .build())
                .build();
    }

    public static SubscriptionFOC aHoaSubscription(final PaymentCalculation calculation) {
        return new SubscriptionFOC(aHoaSubscriptionTerms(calculation));
    }

    public static SubscriptionFOC aHoaSubscription() {
        return new SubscriptionFOC(aHoaSubscriptionTerms());
    }

    public static SubscriptionTerms aHoaSubscriptionTerms(final PaymentCalculation calculation) {
        return SubscriptionTerms.builder()
                .id("HOA Subscription")
                .vendor("HOA")
                .description("HOA Payment")
                .durationType(ESubscriptionDurationType.EXTERNAL_CONDITIONAL)
                .paymentCalculation(calculation)
                .build();
    }

    public static SubscriptionTerms aHoaSubscriptionTerms() {
        return SubscriptionTerms.builder()
                .id("HOA Subscription")
                .vendor("HOA")
                .description("HOA Payment")
                .durationType(ESubscriptionDurationType.EXTERNAL_CONDITIONAL)
                .paymentCalculation(PaymentCalculation.flatRateBuilder().paymentFlatRate(99.0).build())
                .build();
    }
}
