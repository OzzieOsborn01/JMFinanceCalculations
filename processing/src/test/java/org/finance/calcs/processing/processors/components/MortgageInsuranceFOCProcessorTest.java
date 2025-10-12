package org.finance.calcs.processing.processors.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.finance.calcs.core.factories.MortgageInsuranceFOCFactory;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceFOC;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceTerms;
import org.finance.calcs.core.testingUtils.MakeJMFCCoreFOC;
import org.finance.calcs.processing.model.context.MortgageInsuranceFOCProcessorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class MortgageInsuranceFOCProcessorTest {
    private final static String INSURANCE_FOC_PROCESSOR_OUTPUT_RESULT_FILE = "./src/test/resources/testOutputs/componentProcessors/MortgageInsuranceFOCProcessorOutputs.json";

    private MortgageInsuranceFOCProcessor processorToTest;

    @BeforeEach
    public void setup() {
        processorToTest = new MortgageInsuranceFOCProcessor();
    }

    @Test
    public void processPayment_SingleIteration_PrivateMortgageInsurance() {
        final MortgageInsuranceTerms terms = MakeJMFCCoreFOC.aPrivateMortgageInsuranceTerms();
        terms.setStartDate(LocalDate.of(2025, 9, 1));
        final MortgageInsuranceFOC insurance = MortgageInsuranceFOCFactory.createMortgageInsuranceFOC(terms);

        final MortgageInsuranceFOCProcessorContext expectedContext = MortgageInsuranceFOCProcessorContext.builder()
                .paymentUsed(insurance.getScheduledPayment())
                .payment(insurance.getScheduledPayment())
                .paymentRemaining(0.0)
                .periodBalance(0.0)
                .houseValue(terms.getHouseValue())
                .loanValue(terms.getLoanValue())
                .build();

        final MortgageInsuranceFOCProcessorContext context = MortgageInsuranceFOCProcessorContext.builder()
                .payment(insurance.getScheduledPayment())
                .houseValue(terms.getHouseValue())
                .loanValue(terms.getLoanValue())
                .build();
        processorToTest.processPayment(context, insurance, LocalDate.of(2025, 10, 1));

        Assertions.assertEquals(expectedContext, context);
    }

    @Test
    public void processPayment_InsufficentPayment() {
        final MortgageInsuranceTerms terms = MakeJMFCCoreFOC.aPrivateMortgageInsuranceTerms();
        terms.setStartDate(LocalDate.of(2025, 9, 1));
        final MortgageInsuranceFOC insurance = MortgageInsuranceFOCFactory.createMortgageInsuranceFOC(terms);

        final MortgageInsuranceFOCProcessorContext context = MortgageInsuranceFOCProcessorContext.builder()
                .payment(10.0)
                .houseValue(terms.getHouseValue())
                .loanValue(terms.getLoanValue())
                .build();
        processorToTest.processPayment(context, insurance, LocalDate.of(2025, 10, 1));

        Assertions.assertEquals(90.0, context.getPeriodBalance());
        Assertions.assertEquals(10.0, context.getPaymentUsed());
        Assertions.assertEquals(0.0, context.getPaymentRemaining());
    }

    @Test
    public void process_FullIteration() throws IOException {
        final MortgageInsuranceTerms terms = MakeJMFCCoreFOC.aPrivateMortgageInsuranceTerms();
        terms.setStartDate(LocalDate.of(2025, 9, 1));
        final MortgageInsuranceFOC insurance = MortgageInsuranceFOCFactory.createMortgageInsuranceFOC(terms);

        final MortgageInsuranceFOCProcessorContext context = MortgageInsuranceFOCProcessorContext.builder()
                .payment(insurance.getScheduledPayment())
                .houseValue(terms.getHouseValue())
                .loanValue(terms.getLoanValue())
                .build();

        int iteration = 1;
        LocalDate workingDate = LocalDate.of(2025, 9, 1).plusMonths(1);
        ArrayList<ArrayList<Object>> expectedResults = getFOCProcessorOutputExpectedResult("PMI_100.0_12-Iteration_Plus-4-Skip-Iteration");
        while (iteration <= 12) {
            int workingIter = iteration - 1;
            final MortgageInsuranceFOCProcessorContext expectedContext = MortgageInsuranceFOCProcessorContext.builder()
                    .paymentUsed((Double)expectedResults.get(workingIter).get(1))
                    .payment(insurance.getScheduledPayment())
                    .paymentRemaining(0.0)
                    .periodBalance((Double)expectedResults.get(workingIter).get(0))
                    .houseValue(terms.getHouseValue())
                    .loanValue(terms.getLoanValue())
                    .build();

            processorToTest.processPayment(context, insurance, workingDate);

            Assertions.assertEquals(expectedContext, context, "Iteration " + iteration + " values are incorrect");
            iteration++;
            workingDate = workingDate.plusMonths(1);

            processorToTest.processEndOfPeriod(context, insurance, workingDate);
        }

        context.setHouseValue(500000.0);
        context.setLoanValue(50.0);
        processorToTest.processUpdateMortgageInsuranceConditions(context, insurance);

        while (iteration <= 16) {
            int workingIter = iteration - 1;
            final MortgageInsuranceFOCProcessorContext expectedContext = MortgageInsuranceFOCProcessorContext.builder()
                    .paymentUsed((Double)expectedResults.get(workingIter).get(1))
                    .payment(insurance.getScheduledPayment())
                    .paymentRemaining(insurance.getScheduledPayment())
                    .periodBalance((Double)expectedResults.get(workingIter).get(0))
                    .houseValue(500000.0)
                    .loanValue(50.0)
                    .skipped(true)
                    .build();

            processorToTest.processPayment(context, insurance, workingDate);


            Assertions.assertEquals(expectedContext, context, "Iteration " + iteration + " values are incorrect");
            iteration++;
            workingDate = workingDate.plusMonths(1);
        }
    }

    ArrayList<ArrayList<Object>> getFOCProcessorOutputExpectedResult(final String resultIdentifier) throws IOException {
        File f = new File(INSURANCE_FOC_PROCESSOR_OUTPUT_RESULT_FILE);
        JsonNode root = new ObjectMapper().readTree(f);
        JsonNode results = root.get("results");

        ArrayNode loanResults = results.withArray(resultIdentifier);
        ArrayList<ArrayList<Object>> expectedOutputs = new ArrayList<>();

        loanResults.forEach(node -> {
            ArrayNode arrayNode = (ArrayNode) node;
            expectedOutputs.add(new ArrayList<Object>(Arrays.asList(
                    arrayNode.get(0).asDouble(),
                    arrayNode.get(1).asDouble())));
        });

        return expectedOutputs;
    }

}
