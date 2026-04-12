package org.finance.calcs.processing.processors.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.finance.calcs.core.factories.InsuranceFOCFactory;
import org.finance.calcs.core.model.components.insurance.InsuranceFOC;
import org.finance.calcs.core.model.components.insurance.InsuranceTerms;
import org.finance.calcs.core.testingUtils.MakeJMFCCoreFOC;
import org.finance.calcs.processing.model.context.focContext.InsuranceFOCProcessorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

// TODO: Determine if Insurance should be treated as pay down a balance or subscription

public class InsuranceFOCProcessorTest {
    private final static String INSURANCE_FOC_PROCESSOR_OUTPUT_RESULT_FILE = "./src/test/resources/testOutputs/componentProcessors/InsuranceFOCProcessorOutputs.json";

    private InsuranceFOCProcessor processorToTest;

    @BeforeEach
    public void setup() {
        processorToTest = new InsuranceFOCProcessor();
    }

    @Test
    public void processPayment_SingleIteration() {
        final InsuranceTerms terms = MakeJMFCCoreFOC.aFlatPaymentInsuranceTerms();
        terms.setStartDate(LocalDate.of(2025, 9, 1));
        final InsuranceFOC insurance = InsuranceFOCFactory.buildInsuranceFOC(terms);

        final InsuranceFOCProcessorContext expectedContext = InsuranceFOCProcessorContext.builder()
                .paymentUsed(insurance.getScheduledPaymentAmount())
                .payment(insurance.getScheduledPaymentAmount())
                .paymentRemaining(0.0)
                .periodBalance(3666.67)
                .periodReset(false)
                .build();

        final InsuranceFOCProcessorContext context = InsuranceFOCProcessorContext.builder()
                .payment(insurance.getScheduledPaymentAmount())
                .build();
        processorToTest.processPayment(context, insurance, LocalDate.of(2025, 10, 1));

        Assertions.assertEquals(expectedContext, context);
    }

    @Test
    public void process_InsufficentPayment() {
        final InsuranceTerms terms = MakeJMFCCoreFOC.aFlatPaymentInsuranceTerms();
        terms.setStartDate(LocalDate.of(2025, 9, 1));
        final InsuranceFOC insurance = InsuranceFOCFactory.buildInsuranceFOC(terms);

        final InsuranceFOCProcessorContext context = InsuranceFOCProcessorContext.builder()
                .payment(10.0)
                .build();
        processorToTest.processPayment(context, insurance, LocalDate.of(2025, 10, 1));

        Assertions.assertEquals(false, context.isPeriodReset());
        Assertions.assertEquals(3990.0, context.getPeriodBalance());
        Assertions.assertEquals(10.0, context.getPaymentUsed());
        Assertions.assertEquals(0.0, context.getPaymentRemaining());
    }

    @Test
    public void process_FullIteration() throws IOException {
        final InsuranceTerms terms = MakeJMFCCoreFOC.aFlatPaymentInsuranceTerms();
        terms.setStartDate(LocalDate.of(2025, 9, 1));
        final InsuranceFOC insurance = InsuranceFOCFactory.buildInsuranceFOC(terms);

        final InsuranceFOCProcessorContext context = InsuranceFOCProcessorContext.builder()
                .payment(insurance.getScheduledPaymentAmount())
                .build();

        int iteration = 1;
        LocalDate workingDate = LocalDate.of(2025, 9, 1).plusMonths(1);
        ArrayList<ArrayList<Object>> expectedResults = getFOCProcessorOutputExpectedResult(insurance);
        while (iteration <= 12) {
            int workingIter = iteration - 1;
            final InsuranceFOCProcessorContext expectedContext = InsuranceFOCProcessorContext.builder()
                    .paymentUsed(insurance.getScheduledPaymentAmount())
                    .payment(insurance.getScheduledPaymentAmount())
                    .paymentRemaining(0.0)
                    .periodBalance((Double)expectedResults.get(workingIter).get(0))
                    .periodReset((Boolean)expectedResults.get(workingIter).get(1))
                    .build();

            processorToTest.processPayment(context, insurance, workingDate);

            Assertions.assertEquals(expectedContext, context, "Iteration " + iteration + " values are incorrect");
            iteration++;
            workingDate = workingDate.plusMonths(1);
            processorToTest.processEndOfPeriod(context, insurance, workingDate);
        }

        // TODO: Do we treat this as a balance to remove or a regular subscription that can just carry over
    }

    ArrayList<ArrayList<Object>> getFOCProcessorOutputExpectedResult(final InsuranceFOC insuranceFOC) throws IOException {
        File f = new File(INSURANCE_FOC_PROCESSOR_OUTPUT_RESULT_FILE);
        final String resultName = String.join("_",
                insuranceFOC.getAnnualInsuranceRate().toString(),
                insuranceFOC.getInsurancePeriodDuration().toString());
        JsonNode root = new ObjectMapper().readTree(f);
        JsonNode results = root.get("results");

        ArrayNode loanResults = results.withArray(resultName.toString());
        ArrayList<ArrayList<Object>> expectedOutputs = new ArrayList<>();

        loanResults.forEach(node -> {
            ArrayNode arrayNode = (ArrayNode) node;
            expectedOutputs.add(new ArrayList<Object>(Arrays.asList(
                    arrayNode.get(0).asDouble(),
                    arrayNode.get(1).asBoolean())));
        });

        return expectedOutputs;
    }
}
