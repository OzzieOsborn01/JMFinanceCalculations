package org.finance.calcs.core.model.components.insurance;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.finance.calcs.core.percent.Percent;

import java.time.LocalDate;

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

    private  Double flatRateAnnualInsuranceRate;

    private Percent percentInsuranceRate;

    private  Double percentInterestRateBase;

    @Builder.Default
    private LocalDate startDate = LocalDate.now();
}
