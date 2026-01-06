package org.finance.calcs.processing.processors.obligations;

import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceTerms;
import org.finance.calcs.core.model.obligations.MortgageFO;
import org.finance.calcs.core.testingUtils.MakeJMFCCoreFOC;
import org.finance.calcs.core.testingUtils.MakeJMFCCoreMortgage;
import org.finance.calcs.processing.model.context.MortgageFOProcessorContext;
import org.finance.calcs.processing.model.obligationPeriod.MortgageFOPeriod;
import org.finance.calcs.processing.processors.components.InsuranceFOCProcessor;
import org.finance.calcs.processing.processors.components.LoanFOCProcessor;
import org.finance.calcs.processing.processors.components.MortgageInsuranceFOCProcessor;
import org.finance.calcs.processing.testing.constructs.MortgageFOProcessorTestContext;
import org.finance.calcs.processing.testing.mappers.MortgageFOProcessorTestContextMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class MortgageFOProcessorTest {

    private MortgageFOProcessor mortgageFOProcessor;

    private final static MortgageFOProcessorTestContextMapper CONTEXT_MAPPER =
            MortgageFOProcessorTestContextMapper.INSTANCE;

    private static MortgageFOProcessorTestContext DEFAULT_EXPECTED_ENDING_CONTEXT = MortgageFOProcessorTestContext.builder()
            .currentPayment(5143.94)
            .totalPayments(1851233.23)
            .totalInterestContribution(920234.43)
            .totalLoanContribution(775000.0)
            .totalInsuranceContribution(119998.8)
            .totalMortgageInsuranceContribution(9700.0)
            .lastProcessedDate(LocalDate.of(2055, 9, 1))
            .build();

    @BeforeEach
    public void setUp() {
        mortgageFOProcessor = new MortgageFOProcessor(
                new LoanFOCProcessor(),
                new InsuranceFOCProcessor(),
                new MortgageInsuranceFOCProcessor()
        );
    }

    @Test
    public void processNextPeriod_first_2_iterations() {
        final LocalDate startDate = LocalDate.of(2025, 9, 1);
        final MortgageFO mortgageFO = MakeJMFCCoreMortgage.mortgageBuilder()
                .startDate(startDate)
                .houseValue(875000)
                .build();

        final MortgageFOPeriod mortgageFOPeriod1 = MortgageFOPeriod.builder()
                .periodNumber(1)
                .payment(5142.31)
                .startDate(startDate)
                .endDate(LocalDate.of(2025, 10, 1))
                .newLoanBalance(774246.75)
                .interestContribution(3955.73)
                .loanContribution(753.25)
                .insuranceContribution(333.33)
                .mortgageInsuranceContribution(100.0)
                .build();
        final MortgageFOPeriod mortgageFOPeriod2 = MortgageFOPeriod.builder()
                .periodNumber(2)
                .payment(5142.31)
                .startDate(LocalDate.of(2025, 10, 1))
                .endDate(LocalDate.of(2025, 11, 1))
                .newLoanBalance(773489.65)
                .interestContribution(3951.88)
                .loanContribution(757.1)
                .insuranceContribution(333.33)
                .mortgageInsuranceContribution(100.0)
                .build();
        final MortgageFOProcessorContext expectedContextIter1 = MortgageFOProcessorContext.builder()
                .currentPayment(5142.31)
                .totalPayments(5142.31)
                .totalInterestContribution(3955.73)
                .totalInsuranceContribution(333.33)
                .totalMortgageInsuranceContribution(100.0)
                .totalLoanContribution(753.25)
                .lastProcessedDate(LocalDate.of(2025, 10, 1))
                .mortgagePeriodList(List.of(mortgageFOPeriod1))
                .build();

        MortgageFOProcessorContext context = MortgageFOProcessorContext.builder()
                .build();

        mortgageFOProcessor.processNextPeriod(context, mortgageFO);
        Assertions.assertEquals(expectedContextIter1, context);

        final MortgageFOProcessorContext expectedContextIter2 = MortgageFOProcessorContext.builder()
                .currentPayment(5142.31)
                .totalPayments(10284.62)
                .totalInterestContribution(7907.61)
                .totalLoanContribution(1510.35)
                .totalInsuranceContribution(666.66)
                .totalMortgageInsuranceContribution(200.0)
                .lastProcessedDate(LocalDate.of(2025, 11, 1))
                .mortgagePeriodList(List.of(mortgageFOPeriod1, mortgageFOPeriod2))
                .build();

        mortgageFOProcessor.processNextPeriod(context, mortgageFO);
        Assertions.assertEquals(expectedContextIter2, context);
    }

    @Test
    public void processToCompletion() {
        final LocalDate startDate = LocalDate.of(2025, 9, 1);
        final MortgageFO mortgageFO = MakeJMFCCoreMortgage.mortgageBuilder()
                .startDate(startDate)
                .houseValue(875000)
                .build();

        MortgageFOProcessorTestContext expectedEndingContext = MortgageFOProcessorTestContext.builder()
                .currentPayment(5143.94)
                .totalPayments(1851233.23)
                .totalInterestContribution(920234.43)
                .totalLoanContribution(775000.0)
                .totalInsuranceContribution(119998.8)
                .totalMortgageInsuranceContribution(9700.0)
                .lastProcessedDate(LocalDate.of(2055, 9, 1))
                .build();

        MortgageFOProcessorContext context = MortgageFOProcessorContext.builder()
                .build();

        mortgageFOProcessor.processToCompletion(context, mortgageFO);
        Assertions.assertEquals(expectedEndingContext, CONTEXT_MAPPER.contextToTestContext(context));
    }

    @Test
    public void processNumberOfPeriods_First10Iterations() {
        final LocalDate startDate = LocalDate.of(2025, 9, 1);
        final MortgageFO mortgageFO = MakeJMFCCoreMortgage.mortgageBuilder()
                .houseValue(875000)
                .startDate(startDate)
                .build();

        MortgageFOProcessorTestContext expectedEndingContext = MortgageFOProcessorTestContext.builder()
                .currentPayment(5142.31)
                .totalPayments(51423.1)
                .totalInterestContribution(39381.9)
                .totalLoanContribution(7707.9)
                .totalInsuranceContribution(3333.3)
                .totalMortgageInsuranceContribution(1000.0)
                .lastProcessedDate(LocalDate.of(2026, 7, 1))
                .build();

        MortgageFOProcessorContext context = MortgageFOProcessorContext.builder()
                .build();

        mortgageFOProcessor.processNumberOfPeriods(context, mortgageFO, 10);
        Assertions.assertEquals(expectedEndingContext, CONTEXT_MAPPER.contextToTestContext(context));
    }

    public static Stream<Arguments> provideMortgageOutputToCompletion() {
        return Stream.of(
                Arguments.of(
                        MakeJMFCCoreMortgage.mortgageBuilder().houseValue(875000),
                        DEFAULT_EXPECTED_ENDING_CONTEXT.toBuilder()),
                Arguments.of(
                        MakeJMFCCoreMortgage.mortgageBuilder()
                                .houseValue(875000)
                                .mortgageInsuranceTerms(MakeJMFCCoreFOC.aMortgageInsurancePremiumEndingDurationTerms()),
                        DEFAULT_EXPECTED_ENDING_CONTEXT.toBuilder()
                                .totalMortgageInsuranceContribution(13300.0)),
                Arguments.of(
                        MakeJMFCCoreMortgage.mortgageBuilder()
                                .houseValue(875000)
                                .mortgageInsuranceTerms(MakeJMFCCoreFOC.aMortgageInsurancePremiumNotEndingDurationTerms()),
                        DEFAULT_EXPECTED_ENDING_CONTEXT.toBuilder()
                                .totalMortgageInsuranceContribution(36000.0))
        );
    }

    @ParameterizedTest
    @MethodSource("provideMortgageOutputToCompletion")
    public void processNumberOfPeriods_ToCompletion(
            MakeJMFCCoreMortgage.MakeAMortgageBuilder mortgageFOBuilder,
            MortgageFOProcessorTestContext.MortgageFOProcessorTestContextBuilder expectedFinalContextBuilder
    ) {
        final LocalDate startDate = LocalDate.of(2025, 9, 1);
        final MortgageFO mortgageFO = mortgageFOBuilder.startDate(startDate).build();

        MortgageFOProcessorTestContext expectedEndingContext = expectedFinalContextBuilder.build();

        MortgageFOProcessorContext context = MortgageFOProcessorContext.builder()
                .build();

        mortgageFOProcessor.processNumberOfPeriods(context, mortgageFO, -1);
        Assertions.assertEquals(expectedEndingContext, CONTEXT_MAPPER.contextToTestContext(context));
    }
}
