package org.finance.calcs.core.model.components.insurance;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.finance.calcs.core.percent.Percent;

import java.time.LocalDate;

/**
 * This is the initialization detail and terms of Insurance. This class is used to provide initial values
 * to the {@link InsuranceFOC} and establish some default values.
 * <p/>
 * Term values:
 * <ul>
 *     <li>Payment Calculation</li>
 *     <li>Insurance Type</li>
 *     <li>Insurance Provider</li>
 *     <li>Period Duration (defaults to yearly)</li>
 *     <li>Payment Frequency (defaults to monthly)</li>
 *     <li>Start Date (defaults to now)</li>
 * </ul>
 */
@Data
@Builder(toBuilder = true)
public class InsuranceTerms {
    @NonNull
    private PaymentCalculation paymentCalculation;

    @NonNull
    private String insuranceType;

    @NonNull
    private String insuranceProvider;

    @NonNull
    @Builder.Default
    private final EPaymentFrequency insurancePeriodDuration = EPaymentFrequency.YEARLY;

    @NonNull
    @Builder.Default
    private EPaymentFrequency paymentFrequency = EPaymentFrequency.MONTHLY;

    @Builder.Default
    private LocalDate startDate = LocalDate.now();
}
