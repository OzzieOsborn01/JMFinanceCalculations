package org.finance.calcs.processing.processors.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.finance.calcs.core.model.components.insurance.InsuranceFOC;
import org.finance.calcs.core.model.components.subscription.SubscriptionFOC;
import org.finance.calcs.core.model.components.subscription.SubscriptionTerms;
import org.finance.calcs.core.testingUtils.MakeJMFCCoreFOC;
import org.finance.calcs.processing.model.context.focContext.SubscriptionFOCProcessorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class SubscriptionFOCProcessorTest {
    private final static String SUBSCRIPTION_FOC_PROCESSOR_OUTPUT_RESULT_FILE = "./src/test/resources/testOutputs/componentProcessors/SubscriptionFOCProcessorOutputs.json";

    private SubscriptionFOCProcessor processorToTest;

    @BeforeEach
    public void setUp(){
        processorToTest = new SubscriptionFOCProcessor();
    }

    @Test
    public void processPayment_SingleIteration() {
        final SubscriptionTerms terms = MakeJMFCCoreFOC.aHoaSubscriptionTerms();
        terms.setStartDate(LocalDate.of(2025, 9, 1));
        final SubscriptionFOC subscriptionFOC = new SubscriptionFOC(terms);

        final SubscriptionFOCProcessorContext expectedContext = SubscriptionFOCProcessorContext.builder()
                .payment(subscriptionFOC.getScheduledPayment())
                .paymentUsed(subscriptionFOC.getScheduledPayment())
                .periodBalance(0.0)
                .paymentRemaining(0.0)
                .periodReset(false)
                .build();

        final SubscriptionFOCProcessorContext context = SubscriptionFOCProcessorContext.builder()
                .payment(subscriptionFOC.getScheduledPayment())
                .build();
        processorToTest.processPayment(context, subscriptionFOC, LocalDate.of(2025, 10, 1));

        Assertions.assertEquals(expectedContext, context);
    }

    @Test
    public void process_InsufficientPayment() {
        final SubscriptionTerms terms = MakeJMFCCoreFOC.aHoaSubscriptionTerms();
        terms.setStartDate(LocalDate.of(2025, 9, 1));
        final SubscriptionFOC subscriptionFOC = new SubscriptionFOC(terms);
        final Double paymentUsed = 10.0;

        final SubscriptionFOCProcessorContext expectedContext = SubscriptionFOCProcessorContext.builder()
                .payment(paymentUsed)
                .paymentUsed(paymentUsed)
                .periodBalance(89.0)
                .paymentRemaining(0.0)
                .periodReset(false)
                .build();

        final SubscriptionFOCProcessorContext context = SubscriptionFOCProcessorContext.builder()
                .payment(paymentUsed)
                .build();
        processorToTest.processPayment(context, subscriptionFOC, LocalDate.of(2025, 10, 1));

        Assertions.assertEquals(expectedContext, context);
    }

    @Test
    public void process_FullIteration() throws IOException {
        final SubscriptionTerms terms = MakeJMFCCoreFOC.aHoaSubscriptionTerms();
        terms.setStartDate(LocalDate.of(2025, 9, 1));
        final SubscriptionFOC subscriptionFOC = new SubscriptionFOC(terms);

        final SubscriptionFOCProcessorContext context = SubscriptionFOCProcessorContext.builder()
                .payment(subscriptionFOC.getScheduledPayment())
                .build();

        int iteration = 1;
        LocalDate workingDate = LocalDate.of(2025, 10, 1).plusMonths(1);
        ArrayList<ArrayList<Object>> expectedResults = getFOCProcessorOutputExpectedResult(subscriptionFOC);
        while (iteration <= 12) {
            int workingIter = iteration - 1;
            final SubscriptionFOCProcessorContext expectedContext = SubscriptionFOCProcessorContext.builder()
                    .payment(subscriptionFOC.getScheduledPayment())
                    .paymentUsed(subscriptionFOC.getScheduledPayment())
                    .paymentRemaining(0.0)
                    .periodBalance(0.0)
                    .periodReset((Boolean)expectedResults.get(workingIter).get(1))
                    .build();

            processorToTest.processPayment(context, subscriptionFOC, workingDate);

            Assertions.assertEquals(expectedContext, context, "Iteration " + iteration + " values are incorrect");

            iteration++;
            workingDate = workingDate.plusMonths(1);
            processorToTest.processEndOfPeriod(context, subscriptionFOC, workingDate);
        }

        // TODO: Do we treat this as a balance to remove or a regular subscription that can just carry over
    }

    ArrayList<ArrayList<Object>> getFOCProcessorOutputExpectedResult(final SubscriptionFOC subscriptionFOC) throws IOException {
        File f = new File(SUBSCRIPTION_FOC_PROCESSOR_OUTPUT_RESULT_FILE);
        final String resultName = String.join("_",
                subscriptionFOC.getPeriodBalance().toString(),
                subscriptionFOC.getScheduledPaymentFrequency().toString());
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
