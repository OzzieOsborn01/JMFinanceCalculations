package org.finance.calcs.core.model.components.mortageInsurance;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.core.enums.EMortgageInsuranceType;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.enums.ETerminationConditionFactor;
import org.finance.calcs.core.model.metadata.ObligationTerminationStrategy;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.finance.calcs.core.percent.Percent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Builder(toBuilder = true)
public class MortgageInsuranceTerms {
    @NonNull
    private EMortgageInsuranceType mortgageInsuranceType;

    @NonNull
    private Double houseValue;

    @NonNull
    private Double loanValue;

    @NonNull
    private ETerminationConditionFactor terminationConditionFactor;

    @NonNull
    @Builder.Default
    private EPaymentFrequency paymentFrequency = EPaymentFrequency.MONTHLY;

    private PaymentCalculation paymentCalculation;

    @Builder.Default
    private Double upfrontPremium = 0.0;

    @Builder.Default
    private LocalDate startDate = LocalDate.now();

    @Builder.Default
    private Boolean overridden = false;

    // Handle overriding values

    private ObligationTerminationStrategy<Percent> softBalanceTermCondition;
    private ObligationTerminationStrategy<Long> softDurationTermCondition;
    private ObligationTerminationStrategy<Percent> hardBalanceTermCondition;
    private ObligationTerminationStrategy<Long> hardDurationTermCondition;

    @Builder.Default
    private ChronoUnit durationTermUnits = ChronoUnit.YEARS;
}
