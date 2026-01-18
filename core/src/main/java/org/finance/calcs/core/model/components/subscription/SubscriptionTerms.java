package org.finance.calcs.core.model.components.subscription;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.enums.ESubscriptionDurationType;
import org.finance.calcs.core.model.calculations.PaymentCalculation;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class SubscriptionTerms {

    private String id;
    private String vendor;
    private String description;

    @NonNull
    private ESubscriptionDurationType durationType;

    @NonNull
    private PaymentCalculation paymentCalculation;

    @Builder.Default
    private LocalDate startDate = LocalDate.now();

    @Builder.Default
    private EPaymentFrequency scheduledPaymentFrequency = EPaymentFrequency.MONTHLY;
}
