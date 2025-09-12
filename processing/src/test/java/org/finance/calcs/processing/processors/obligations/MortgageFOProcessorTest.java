package org.finance.calcs.processing.processors.obligations;

import org.finance.calcs.core.model.obligations.MortgageFO;
import org.finance.calcs.core.testingUtils.MakeJMFCCoreMortgage;
import org.finance.calcs.processing.model.context.MortgageFOProcessorContext;
import org.finance.calcs.processing.model.obligationPeriod.MortgageFOPeriod;
import org.finance.calcs.processing.processors.components.LoanFOCProcessor;
import org.finance.calcs.processing.testing.constructs.MortgageFOProcessorTestContext;
import org.finance.calcs.processing.testing.mappers.MortgageFOProcessorTestContextMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class MortgageFOProcessorTest {

    private MortgageFOProcessor mortgageFOProcessor;

    private final static MortgageFOProcessorTestContextMapper CONTEXT_MAPPER =
            MortgageFOProcessorTestContextMapper.INSTANCE;

    @BeforeEach
    public void setUp() {
        mortgageFOProcessor = new MortgageFOProcessor(
                new LoanFOCProcessor()
        );
    }

    @Test
    public void processNextPeriod_first_2_iterations() {
        final LocalDate startDate = LocalDate.of(2025, 9, 1);
        final MortgageFO mortgageFO = MakeJMFCCoreMortgage.mortgageBuilder()
                .startDate(startDate)
                .build();

        final MortgageFOPeriod mortgageFOPeriod1 = MortgageFOPeriod.builder()
                .periodNumber(1)
                .payment(4708.98)
                .startDate(startDate)
                .endDate(LocalDate.of(2025, 10, 1))
                .newLoanBalance(774246.75)
                .interestContribution(3955.73)
                .loanContribution(753.25)
                .build();
        final MortgageFOPeriod mortgageFOPeriod2 = MortgageFOPeriod.builder()
                .periodNumber(2)
                .payment(4708.98)
                .startDate(LocalDate.of(2025, 10, 1))
                .endDate(LocalDate.of(2025, 11, 1))
                .newLoanBalance(773489.65)
                .interestContribution(3951.88)
                .loanContribution(757.1)
                .build();
        MortgageFOProcessorContext expectedContextIter1 = MortgageFOProcessorContext.builder()
                .currentPayment(4708.98)
                .totalPayments(4708.98)
                .totalInterestContribution(3955.73)
                .totalLoanContribution(753.25)
                .lastProcessedDate(LocalDate.of(2025, 10, 1))
                .mortgagePeriodList(List.of(mortgageFOPeriod1))
                .build();

        MortgageFOProcessorContext context = MortgageFOProcessorContext.builder()
                .build();

        mortgageFOProcessor.processNextPeriod(context, mortgageFO);
        Assertions.assertEquals(expectedContextIter1, context);

        final MortgageFOProcessorContext expectedContextIter2 = MortgageFOProcessorContext.builder()
                .currentPayment(4708.98)
                .totalPayments(9417.96)
                .totalInterestContribution(7907.61)
                .totalLoanContribution(1510.35)
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
                .build();

        MortgageFOProcessorTestContext expectedEndingContext = MortgageFOProcessorTestContext.builder()
                .currentPayment(4710.61)
                .totalPayments(1695234.43)
                .totalInterestContribution(920234.43)
                .totalLoanContribution(775000.0)
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
                .startDate(startDate)
                .build();

        MortgageFOProcessorTestContext expectedEndingContext = MortgageFOProcessorTestContext.builder()
                .currentPayment(4708.98)
                .totalPayments(47089.8)
                .totalInterestContribution(39381.9)
                .totalLoanContribution(7707.9)
                .lastProcessedDate(LocalDate.of(2026, 7, 1))
                .build();

        MortgageFOProcessorContext context = MortgageFOProcessorContext.builder()
                .build();

        mortgageFOProcessor.processNumberOfPeriods(context, mortgageFO, 10);
        Assertions.assertEquals(expectedEndingContext, CONTEXT_MAPPER.contextToTestContext(context));
    }

    @Test
    public void processNumberOfPeriods_ToCompletion() {
        final LocalDate startDate = LocalDate.of(2025, 9, 1);
        final MortgageFO mortgageFO = MakeJMFCCoreMortgage.mortgageBuilder()
                .startDate(startDate)
                .build();

        MortgageFOProcessorTestContext expectedEndingContext = MortgageFOProcessorTestContext.builder()
                .currentPayment(4710.61)
                .totalPayments(1695234.43)
                .totalInterestContribution(920234.43)
                .totalLoanContribution(775000.0)
                .lastProcessedDate(LocalDate.of(2055, 9, 1))
                .build();

        MortgageFOProcessorContext context = MortgageFOProcessorContext.builder()
                .build();

        mortgageFOProcessor.processNumberOfPeriods(context, mortgageFO, -1);
        Assertions.assertEquals(expectedEndingContext, CONTEXT_MAPPER.contextToTestContext(context));
    }

}
