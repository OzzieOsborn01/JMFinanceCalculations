package org.finance.calcs.core.model.components.loan;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.core.enums.EInterestFrequency;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.percent.Percent;

import java.time.LocalDate;

/**
 * * This is the initialization detail and terms of Loan. This class is used to provide initial values
 *  * to the {@link LoanFOC} and establish some default values.
 *  * <p/>
 *  * Term values:
 *  * <ul>
 *  *     <li>Loan initial amount</li>
 *  *     <li>Loan yearly interest rate</li>
 *  *     <li>Loan term in months</li>
 *  *     <li>Required payment frequency (defaults to monthly)</li>
 *  *     <li>Interest frequency (defaults to monthly (12 months by 360 days))</li>
 *  *     <li>Loan start date (defaults to now)</li>
 *  * </ul>
 */
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
