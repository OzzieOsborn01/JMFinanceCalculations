package org.finance.calcs.core.model.components.loan;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.core.enums.EInterestFrequency;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.percent.Percent;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class LoanTerms {
    @NonNull
    private Double loanInitialAmount;

    @NonNull
    private Percent loanYearlyInterestRate;

    @NonNull
    private Integer loanTermMonths;

    @Builder.Default
    private LocalDate loanTermStartDate = LocalDate.now();

    @Builder.Default
    private EPaymentFrequency scheduledPaymentFrequency = EPaymentFrequency.MONTHLY;

    @Builder.Default
    private EInterestFrequency interestFrequency = EInterestFrequency.MONTHLY_12_BY_360;
}
