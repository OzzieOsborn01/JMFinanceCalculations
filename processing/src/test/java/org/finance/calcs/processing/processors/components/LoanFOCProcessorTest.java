package org.finance.calcs.processing.processors.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.finance.calcs.core.model.components.loan.LoanFOC;
import org.finance.calcs.core.model.components.loan.LoanTerms;
import org.finance.calcs.core.testingUtils.MakeJMFCCoreFOC;
import org.finance.calcs.processing.model.context.LoanFOCProcessorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class LoanFOCProcessorTest {
    private final static String LOAN_FOC_PROCESSOR_OUTPUT_RESULT_FILE = "./src/test/resources/testOutputs/componentProcessors/LoanFOCProcessorOutputs.json";

    private LoanFOCProcessor loanFOCProcessor;

    @BeforeEach
    public void setup() {
        loanFOCProcessor = new LoanFOCProcessor();
    }

    @Test
    public void process_SingleIteration() {
        final LoanTerms loanTerms = MakeJMFCCoreFOC.aLoanTerms();
        loanTerms.setLoanTermStartDate(LocalDate.of(2025, 9, 1));
        final LoanFOC loan = new LoanFOC(loanTerms);

        final LoanFOCProcessorContext expectedContext = LoanFOCProcessorContext.builder()
                .paymentUsed(loan.getScheduledPaymentPerPeriod())
                .payment(loan.getScheduledPaymentPerPeriod())
                .paymentRemaining(0.0)
                .principalBalance(774246.75)
                .principalDelta(753.25)
                .interestDelta(3955.73)
                .interestRemaining(0.0)
                .build();

        final LoanFOCProcessorContext context = LoanFOCProcessorContext.builder()
                .payment(loan.getScheduledPaymentPerPeriod())
                .build();
        loanFOCProcessor.process(context, loan, LocalDate.of(2025, 10, 1));

        Assertions.assertEquals(expectedContext, context);
    }

    @Test
    public void process_InsufficentPayment() {
        final LoanTerms loanTerms = MakeJMFCCoreFOC.aLoanTerms();
        loanTerms.setLoanTermStartDate(LocalDate.of(2025, 9, 1));

        final LoanFOC loan = new LoanFOC(loanTerms);
        final LoanFOCProcessorContext context = LoanFOCProcessorContext.builder()
                .payment(10.0)
                .build();
        loanFOCProcessor.process(context, loan, LocalDate.of(2025, 10, 1));

        Assertions.assertEquals(775000.0, context.getPrincipalBalance());
        Assertions.assertEquals(0.0, context.getPrincipalDelta());
        Assertions.assertEquals(10.0, context.getInterestDelta());
        Assertions.assertEquals(3945.73, context.getInterestRemaining());
        Assertions.assertEquals(10.0, context.getPaymentUsed());
        Assertions.assertEquals(0.0, context.getPaymentRemaining());
    }

    @Test
    public void process_FullIteration() throws IOException {
        final LoanTerms loanTerms = MakeJMFCCoreFOC.aLoanTerms();
        loanTerms.setLoanTermStartDate(LocalDate.of(2025, 9, 1));

        final LoanFOC loan = new LoanFOC(loanTerms);
        final LoanFOCProcessorContext context = LoanFOCProcessorContext.builder()
                .payment(loan.getScheduledPaymentPerPeriod())
                .build();

        int iteration = 1;
        LocalDate workingDate = LocalDate.of(2025, 9, 1).plusMonths(1);
        ArrayList<ArrayList<Double>> expectedResults = getLoanFOCProcessorOutputExpectedResult(loan);
        while (iteration <= 359) {
            int workingIter = iteration - 1;
            final LoanFOCProcessorContext expectedContext = LoanFOCProcessorContext.builder()
                .paymentUsed(loan.getScheduledPaymentPerPeriod())
                .payment(loan.getScheduledPaymentPerPeriod())
                .paymentRemaining(0.0)
                .principalBalance(expectedResults.get(workingIter).get(0))
                .principalDelta(expectedResults.get(workingIter).get(1))
                .interestDelta(expectedResults.get(workingIter).get(2))
                .interestRemaining(expectedResults.get(workingIter).get(3))
                .build();


            loanFOCProcessor.process(context, loan, workingDate);


            Assertions.assertEquals(expectedContext, context, "Iteration " + iteration + " values are incorrect");
            iteration++;
            workingDate = workingDate.plusMonths(1);
        }

        int workingIter = iteration - 1;
        final double finalPayment = loan.getLoanCurrentPrinciple() +
                loanFOCProcessor.getAccruedInterestForPeriod(loan, workingDate);
        final LoanFOCProcessorContext expectedContext = LoanFOCProcessorContext.builder()
                .payment(finalPayment)
                .paymentUsed(finalPayment)
                .payment(finalPayment)
                .paymentRemaining(0.0)
                .principalBalance(expectedResults.get(workingIter).get(0))
                .principalDelta(expectedResults.get(workingIter).get(1))
                .interestDelta(expectedResults.get(workingIter).get(2))
                .interestRemaining(expectedResults.get(workingIter).get(3))
                .build();

        context.setPayment(finalPayment);

        loanFOCProcessor.process(context, loan, workingDate);
        Assertions.assertEquals(expectedContext, context, "Iteration " + iteration + " values are incorrect");
    }

    ArrayList<ArrayList<Double>> getLoanFOCProcessorOutputExpectedResult(LoanFOC loan) throws IOException {
        File f = new File(LOAN_FOC_PROCESSOR_OUTPUT_RESULT_FILE);
        final String resultName = String.join("_",
                loan.getLoanInitialPrinciple().toString(),
                loan.getLoanYearlyInterestRate().asDouble().toString(),
                ((Double)loan.getScheduledPaymentPerPeriod()).toString());
        JsonNode root = new ObjectMapper().readTree(f);
        JsonNode results = root.get("results");

        ArrayNode loanResults = results.withArray(resultName.toString());
        ArrayList<ArrayList<Double>> expectedOutputs = new ArrayList<>();

        loanResults.forEach(node -> {
            ArrayNode arrayNode = (ArrayNode) node;
            expectedOutputs.add(new ArrayList<>(Arrays.asList(
                    arrayNode.get(0).asDouble(),
                    arrayNode.get(1).asDouble(),
                    arrayNode.get(2).asDouble(),
                    arrayNode.get(3).asDouble())));
        });

        return expectedOutputs;
    }
}
