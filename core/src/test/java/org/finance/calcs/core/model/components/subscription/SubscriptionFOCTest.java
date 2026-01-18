package org.finance.calcs.core.model.components.subscription;

import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.enums.ESubscriptionDurationType;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class SubscriptionFOCTest {
    @Test
    public void createSubscription() {
        final SubscriptionFOC expectedSubscription = SubscriptionFOC.builder()
                .id("subscription")
                .vendor("vendor")
                .description("description")
                .durationType(ESubscriptionDurationType.EXTERNAL_CONDITIONAL)
                .paymentCalculation(new PaymentCalculation(98.0))
                .startDate(LocalDate.of(2025, 9, 1))
                .periodEndDate(LocalDate.of(2025, 9, 14))
                .lastProcessedDate(LocalDate.of(2025, 9, 1))
                .periodBalance(98.0)
                .scheduledPaymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final SubscriptionTerms subscriptionTerms = SubscriptionTerms.builder()
                .id("subscription")
                .vendor("vendor")
                .description("description")
                .durationType(ESubscriptionDurationType.EXTERNAL_CONDITIONAL)
                .paymentCalculation(new PaymentCalculation(98.0))
                .startDate(LocalDate.of(2025, 9, 1))
                .scheduledPaymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final SubscriptionFOC subscriptionFOC = new SubscriptionFOC(subscriptionTerms);
        Assertions.assertEquals(expectedSubscription, subscriptionFOC);
    }

    @Test
    public void createSubscription_DefaultValues() {
        final SubscriptionFOC expectedSubscription = SubscriptionFOC.builder()
                .id("subscription")
                .vendor("vendor")
                .description("description")
                .durationType(ESubscriptionDurationType.EXTERNAL_CONDITIONAL)
                .paymentCalculation(new PaymentCalculation(98.0))
                .startDate(LocalDate.of(2025, 9, 1))
                .periodEndDate(LocalDate.of(2025, 9, 30))
                .lastProcessedDate(LocalDate.of(2025, 9, 1))
                .periodBalance(98.0)
                .scheduledPaymentFrequency(EPaymentFrequency.MONTHLY)
                .build();

        final SubscriptionTerms subscriptionTerms = SubscriptionTerms.builder()
                .id("subscription")
                .vendor("vendor")
                .description("description")
                .durationType(ESubscriptionDurationType.EXTERNAL_CONDITIONAL)
                .paymentCalculation(new PaymentCalculation(98.0))
                .startDate(LocalDate.of(2025, 9, 1))
                .build();

        final SubscriptionFOC subscriptionFOC = new SubscriptionFOC(subscriptionTerms);
        Assertions.assertEquals(expectedSubscription, subscriptionFOC);
    }

    @Test
    public void subscription_applyPayment() {
        final SubscriptionFOC expectedSubscription = SubscriptionFOC.builder()
                .id("subscription")
                .vendor("vendor")
                .description("description")
                .durationType(ESubscriptionDurationType.EXTERNAL_CONDITIONAL)
                .paymentCalculation(new PaymentCalculation(98.0))
                .startDate(LocalDate.of(2025, 9, 1))
                .periodEndDate(LocalDate.of(2025, 9, 14))
                .lastProcessedDate(LocalDate.of(2025, 9, 1))
                .periodBalance(78.0)
                .scheduledPaymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final SubscriptionTerms subscriptionTerms = SubscriptionTerms.builder()
                .id("subscription")
                .vendor("vendor")
                .description("description")
                .durationType(ESubscriptionDurationType.EXTERNAL_CONDITIONAL)
                .paymentCalculation(new PaymentCalculation(98.0))
                .startDate(LocalDate.of(2025, 9, 1))
                .scheduledPaymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final SubscriptionFOC subscriptionFOC = new SubscriptionFOC(subscriptionTerms);
        subscriptionFOC.applyPayment(20.0);
        Assertions.assertEquals(expectedSubscription, subscriptionFOC);
    }

    @Test
    public void subscription_applyFee() {
        final SubscriptionFOC expectedSubscription = SubscriptionFOC.builder()
                .id("subscription")
                .vendor("vendor")
                .description("description")
                .durationType(ESubscriptionDurationType.EXTERNAL_CONDITIONAL)
                .paymentCalculation(new PaymentCalculation(98.0))
                .startDate(LocalDate.of(2025, 9, 1))
                .periodEndDate(LocalDate.of(2025, 9, 14))
                .lastProcessedDate(LocalDate.of(2025, 9, 1))
                .periodBalance(108.0)
                .scheduledPaymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final SubscriptionTerms subscriptionTerms = SubscriptionTerms.builder()
                .id("subscription")
                .vendor("vendor")
                .description("description")
                .durationType(ESubscriptionDurationType.EXTERNAL_CONDITIONAL)
                .paymentCalculation(new PaymentCalculation(98.0))
                .startDate(LocalDate.of(2025, 9, 1))
                .scheduledPaymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final SubscriptionFOC subscriptionFOC = new SubscriptionFOC(subscriptionTerms);
        subscriptionFOC.applyFee(10.0);
        Assertions.assertEquals(expectedSubscription, subscriptionFOC);
    }

    @Test
    public void subscription_resetPeriod() {
        final SubscriptionFOC expectedSubscription = SubscriptionFOC.builder()
                .id("subscription")
                .vendor("vendor")
                .description("description")
                .durationType(ESubscriptionDurationType.EXTERNAL_CONDITIONAL)
                .paymentCalculation(new PaymentCalculation(108.0))
                .startDate(LocalDate.of(2025, 9, 1))
                .periodEndDate(LocalDate.of(2025, 10, 1))
                .lastProcessedDate(LocalDate.of(2025, 9, 1))
                .periodBalance(108.0)
                .scheduledPaymentFrequency(EPaymentFrequency.MONTHLY)
                .build();

        final SubscriptionTerms subscriptionTerms = SubscriptionTerms.builder()
                .id("subscription")
                .vendor("vendor")
                .description("description")
                .durationType(ESubscriptionDurationType.EXTERNAL_CONDITIONAL)
                .paymentCalculation(new PaymentCalculation(108.0))
                .startDate(LocalDate.of(2025, 9, 1))
                .build();

        final SubscriptionFOC subscriptionFOC = new SubscriptionFOC(subscriptionTerms);
        subscriptionFOC.applyPayment(20.0);
        subscriptionFOC.resetPeriod(LocalDate.of(2025, 9, 1));
        Assertions.assertEquals(expectedSubscription, subscriptionFOC);
    }
}
