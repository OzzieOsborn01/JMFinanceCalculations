package org.finance.calcs.core.model.components.insurance;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.core.enums.EInsuranceCalcType;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.percent.Percent;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class InsuranceTerms {
    @NonNull
    EInsuranceCalcType insuranceCalcType;

    @NonNull
    String insuranceType;

    @NonNull
    String insuranceProvider;

    @NonNull
    @Builder.Default
    private final EPaymentFrequency insurancePeriodDuration = EPaymentFrequency.YEARLY;

    @NonNull
    @Builder.Default
    EPaymentFrequency paymentFrequency = EPaymentFrequency.MONTHLY;

    Double flatRateAnnualInsuranceRate;

    Percent percentInsuranceRate;

    Double percentInterestRateBase;

    @Builder.Default
    LocalDate startDate = LocalDate.now();
}
