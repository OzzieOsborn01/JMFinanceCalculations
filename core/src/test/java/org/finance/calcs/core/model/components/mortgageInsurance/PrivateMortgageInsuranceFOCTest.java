package org.finance.calcs.core.model.components.mortgageInsurance;

import org.finance.calcs.core.enums.EMortgageInsuranceType;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.enums.ETerminationConditionComparison;
import org.finance.calcs.core.enums.ETerminationConditionFactor;
import org.finance.calcs.core.factories.MortgageInsuranceFOCFactory;
import org.finance.calcs.core.model.components.mortageInsurance.TMortgageInsuranceFOC;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceTerms;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceFOC;
import org.finance.calcs.core.model.metadata.ObligationTerminationStrategy;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.finance.calcs.core.percent.Percent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class PrivateMortgageInsuranceFOCTest {
    @Test
    public void createPMI() {
        final MortgageInsuranceFOC expectedPmiFOC = MortgageInsuranceFOC.builder()
                .mortgageInsuranceType(EMortgageInsuranceType.PRIVATE_MORTGAGE_INSURANCE)
                .houseValue(875000.0)
                .loanValue(775000.0)
                .loanToHouseValueRatio(Percent.fromDecimal(0.8857142, 5))
                .terminationConditionFactor(ETerminationConditionFactor.OBLIGATION_COMPLETED)
                .softBalanceTermCondition(ObligationTerminationStrategy.<Percent>builder()
                        .comparisonMethod(ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(Percent.fromPercent(20.0, 4))
                        .terminationConditionDescription("Soft Balance Condition")
                        .build())
                .hardBalanceTermCondition(ObligationTerminationStrategy.<Percent>builder()
                        .comparisonMethod(ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(Percent.fromPercent(22.0, 4))
                        .terminationConditionDescription("Hard Balance Condition")
                        .build())
                .durationTermUnits(ChronoUnit.YEARS)
                .upfrontPremium(0.0)
                .isInsuranceComplete(false)
                .periodBalance(100.0)
                .startDate(LocalDate.of(2025, 9, 1))
                .lastProcessedDate(LocalDate.of(2025, 9, 1))
                .flatRateDurationInsuranceRate(100.0)
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .paymentCalculation(new PaymentCalculation(100.0))
                .build();

        final MortgageInsuranceTerms terms = MortgageInsuranceTerms.builder()
                .mortgageInsuranceType(EMortgageInsuranceType.PRIVATE_MORTGAGE_INSURANCE)
                .houseValue(875000.0)
                .loanValue(775000.0)
                .paymentCalculation(new PaymentCalculation(100.0))
                .terminationConditionFactor(ETerminationConditionFactor.OBLIGATION_COMPLETED)
                .softBalanceTermCondition(ObligationTerminationStrategy.<Percent>builder()
                        .comparisonMethod(ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(Percent.fromPercent(20.0, 4))
                        .terminationConditionDescription("Soft Balance Condition")
                        .build())
                .hardBalanceTermCondition(ObligationTerminationStrategy.<Percent>builder()
                        .comparisonMethod(ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(Percent.fromPercent(22.0, 4))
                        .terminationConditionDescription("Hard Balance Condition")
                        .build())
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .build();

        final MortgageInsuranceFOC miFoc = MortgageInsuranceFOCFactory
                .createMortgageInsuranceFOC(terms);

        Assertions.assertEquals(expectedPmiFOC, miFoc);
    }

    @Test
    public void pmi_ApplyPayment() {
        final MortgageInsuranceFOC expectedPmiFOC = MortgageInsuranceFOC.builder()
                .mortgageInsuranceType(EMortgageInsuranceType.PRIVATE_MORTGAGE_INSURANCE)
                .houseValue(875000.0)
                .loanValue(775000.0)
                .loanToHouseValueRatio(Percent.fromDecimal(0.8857142, 5))
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
                .durationTermUnits(ChronoUnit.YEARS)
                .upfrontPremium(0.0)
                .isInsuranceComplete(false)
                .periodBalance(50.0)
                .startDate(LocalDate.of(2025, 9, 1))
                .lastProcessedDate(LocalDate.of(2025, 9, 1))
                .flatRateDurationInsuranceRate(100.0)
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .paymentCalculation(new PaymentCalculation(100.0))
                .build();

        final MortgageInsuranceTerms terms = MortgageInsuranceTerms.builder()
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
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .build();

        final MortgageInsuranceFOC miFoc = MortgageInsuranceFOCFactory
                .createMortgageInsuranceFOC(terms);
        miFoc.applyPayment(50.0);
        Assertions.assertEquals(expectedPmiFOC, miFoc);
    }

    @Test
    public void pmi_resetPeriod() {
        final MortgageInsuranceFOC expectedPmiFOC = MortgageInsuranceFOC.builder()
                .mortgageInsuranceType(EMortgageInsuranceType.PRIVATE_MORTGAGE_INSURANCE)
                .houseValue(875000.0)
                .loanValue(775000.0)
                .loanToHouseValueRatio(Percent.fromDecimal(0.8857142, 5))
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
                .durationTermUnits(ChronoUnit.YEARS)
                .upfrontPremium(0.0)
                .isInsuranceComplete(false)
                .periodBalance(100.0)
                .startDate(LocalDate.of(2025, 9, 1))
                .lastProcessedDate(LocalDate.of(2025, 10, 1))
                .flatRateDurationInsuranceRate(100.0)
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .paymentCalculation(new PaymentCalculation(100.0))
                .build();

        final MortgageInsuranceTerms terms = MortgageInsuranceTerms.builder()
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
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .build();

        final MortgageInsuranceFOC miFoc = MortgageInsuranceFOCFactory
                .createMortgageInsuranceFOC(terms);

        Assertions.assertEquals(MortgageInsuranceFOC.class, miFoc.getClass());
        miFoc.applyPayment(50.0);
        miFoc.resetPeriod(LocalDate.of(2025, 10, 1));

        Assertions.assertEquals(expectedPmiFOC, miFoc);
    }

    private static Stream<Arguments> determinationTerminationBalanceScoreArguments() {
        return Stream.of(
                Arguments.argumentSet("Not Terminated", false, false, false, 800000.0, 775000.0),
                Arguments.argumentSet("Soft Balance not used", true, false, false, 800000.0, 630000.0),
                Arguments.argumentSet("Soft Balance used", true, true, false, 800000.0, 630000.0),
                Arguments.argumentSet("Hard Balance Terminated", true, true, true, 800000.0, 500000.0)
        );
    }

    @ParameterizedTest
    @MethodSource("determinationTerminationBalanceScoreArguments")
    public void pmi_determineBalanceTerminationScore(
            boolean softTermination,
            boolean useSoftTermiantion,
            boolean hardTermination,
            Double houseValue,
            Double loanValue
        ) {
        boolean insuranceComplete = hardTermination || (useSoftTermiantion && softTermination);
        final MortgageInsuranceTerms terms = MortgageInsuranceTerms.builder()
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
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .build();

        final MortgageInsuranceFOC miFoc = MortgageInsuranceFOCFactory
                .createMortgageInsuranceFOC(terms);

        miFoc.updateLoanValueAndHouseValue(loanValue, houseValue);
        miFoc.determineTerminationScore(useSoftTermiantion);

        Assertions.assertEquals(softTermination, miFoc.isSoftTerminationComplete(), "Soft termination does not match");
        Assertions.assertEquals(hardTermination, miFoc.isHardTerminationComplete(), "Hard termination does not match");
        Assertions.assertEquals(insuranceComplete, miFoc.isInsuranceComplete(), "Insurance Complete does not match");
    }

    private static Stream<Arguments> determinationTerminationDurationScoreArguments() {
        return Stream.of(
                Arguments.argumentSet("Not Terminated", false, false, false, LocalDate.of(2025, 12, 1)),
                Arguments.argumentSet("Soft Duration not used", true, false, false, LocalDate.of(2030, 12, 1)),
                Arguments.argumentSet("Soft Duration used", true, true, false, LocalDate.of(2030, 12, 1)),
                Arguments.argumentSet("Hard Duration Terminated", true, true, true, LocalDate.of(2040, 12, 1))
        );
    }

    @ParameterizedTest
    @MethodSource("determinationTerminationDurationScoreArguments")
    public void pmi_determineDurationTerminationScore(
            boolean softTermination,
            boolean useSoftTermination,
            boolean hardTermination,
            LocalDate date
    ) {
        boolean insuranceComplete = hardTermination || (useSoftTermination && softTermination);
        final MortgageInsuranceTerms terms = MortgageInsuranceTerms.builder()
                .mortgageInsuranceType(EMortgageInsuranceType.PRIVATE_MORTGAGE_INSURANCE)
                .houseValue(875000.0)
                .loanValue(775000.0)
                .paymentCalculation(new PaymentCalculation(100.0))
                .terminationConditionFactor(ETerminationConditionFactor.DURATION_COMPLETED)
                .softDurationTermCondition(ObligationTerminationStrategy.<Long>builder()
                        .comparisonMethod(ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(5L)
                        .terminationConditionDescription("Soft Duration Condition")
                        .build())
                .hardDurationTermCondition(ObligationTerminationStrategy.<Long>builder()
                        .comparisonMethod(ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(11L)
                        .terminationConditionDescription("Hard Duration Condition")
                        .build())
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .build();

        final MortgageInsuranceFOC miFoc = MortgageInsuranceFOCFactory
                .createMortgageInsuranceFOC(terms);

        miFoc.updateLastProcessedDate(date);
        miFoc.determineTerminationScore(useSoftTermination);

        Assertions.assertEquals(softTermination, miFoc.isSoftTerminationComplete(), "Soft termination does not match");
        Assertions.assertEquals(hardTermination, miFoc.isHardTerminationComplete(), "Hard termination does not match");
        Assertions.assertEquals(insuranceComplete, miFoc.isInsuranceComplete(), "Insurance Complete does not match");
    }

    private static Stream<Arguments> determinationTerminationObligationOrDurationScoreArguments() {
        return Stream.of(
                Arguments.argumentSet("Not Terminated", false, false, false, LocalDate.of(2025, 12, 1), 800000.0, 775000.0),
                Arguments.argumentSet("Soft Duration not used", true, false, false, LocalDate.of(2030, 12, 1), 800000.0, 630000.0),
                Arguments.argumentSet("Soft Duration used", true, true, false, LocalDate.of(2030, 12, 1), 800000.0, 630000.0),
                Arguments.argumentSet("Soft Balance not used", true, false, false, LocalDate.of(2025, 12, 1), 800000.0, 630000.0),
                Arguments.argumentSet("Soft Balance used", true, true, false, LocalDate.of(2025, 12, 1), 800000.0, 630000.0),
                Arguments.argumentSet("Hard Balance Terminated", true, true, true, LocalDate.of(2025, 12, 1),  800000.0, 500000.0),
                Arguments.argumentSet("Hard Duration Terminated", true, true, true, LocalDate.of(2040, 12, 1), 800000.0, 775000.0)
        );
    }

    @ParameterizedTest
    @MethodSource("determinationTerminationObligationOrDurationScoreArguments")
    public void pmi_determineDurationOrBalanceTerminationScore(
            boolean softTermination,
            boolean useSoftTermination,
            boolean hardTermination,
            LocalDate date,
            Double houseValue,
            Double loanValue
    ) {
        boolean insuranceComplete = hardTermination || (useSoftTermination && softTermination);
        final MortgageInsuranceTerms terms = MortgageInsuranceTerms.builder()
                .mortgageInsuranceType(EMortgageInsuranceType.PRIVATE_MORTGAGE_INSURANCE)
                .houseValue(houseValue)
                .loanValue(loanValue)
                .paymentCalculation(new PaymentCalculation(100.0))
                .terminationConditionFactor(ETerminationConditionFactor.OBLIGATION_OR_DURATION_COMPLETED)
                .softDurationTermCondition(ObligationTerminationStrategy.<Long>builder()
                        .comparisonMethod(ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(5L)
                        .terminationConditionDescription("Soft Duration Condition")
                        .build())
                .hardDurationTermCondition(ObligationTerminationStrategy.<Long>builder()
                        .comparisonMethod(ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO)
                        .terminationConditionValue(11L)
                        .terminationConditionDescription("Hard Duration Condition")
                        .build())
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
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .build();

        final MortgageInsuranceFOC miFoc = MortgageInsuranceFOCFactory
                .createMortgageInsuranceFOC(terms);

        miFoc.updateLastProcessedDate(date);
        miFoc.determineTerminationScore(useSoftTermination);

        Assertions.assertEquals(softTermination, miFoc.isSoftTerminationComplete(), "Soft termination does not match");
        Assertions.assertEquals(hardTermination, miFoc.isHardTerminationComplete(), "Hard termination does not match");
        Assertions.assertEquals(insuranceComplete, miFoc.isInsuranceComplete(), "Insurance Complete does not match");
    }
}
