package org.finance.calcs.core.model.components.loan;

import lombok.*;
import org.finance.calcs.core.enums.EInterestFrequency;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.model.obligationBase.FinancialObligationComponent;
import org.finance.calcs.core.percent.Percent;
import org.finance.calcs.core.util.PaymentCalculationUtil;
import org.finance.calcs.core.util.PaymentFrequencyUtil;
import org.finance.calcs.core.util.RoundingUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
@ToString
public class LoanFOC implements FinancialObligationComponent {
    // Final values
    private final Double loanInitialPrinciple;

    private final Integer loanTermMonths;

    private LocalDate loanTermStartDate;

    private EPaymentFrequency scheduledPaymentFrequency;

    private EInterestFrequency interestFrequency;

    // Dynamic values

    private Double loanCurrentPrinciple;

    private Percent loanYearlyInterestRate;

    private LocalDate loanTermScheduledProjectedEndDate;

    private double scheduledPaymentPerPeriod;

    private Integer loanTermMonthsRemaining;

    @Setter
    private LocalDate lastProcessedDate;

    public LoanFOC(final LoanTerms loanTerms) {
        loanInitialPrinciple = loanTerms.getLoanInitialAmount();
        loanCurrentPrinciple = loanTerms.getLoanInitialAmount();
        interestFrequency = loanTerms.getInterestFrequency();

        loanTermMonths = loanTerms.getLoanTermMonths();
        loanTermMonthsRemaining = loanTerms.getLoanTermMonths();
        loanTermStartDate = loanTerms.getLoanTermStartDate();
        lastProcessedDate = loanTerms.getLoanTermStartDate();
        loanTermScheduledProjectedEndDate =
            loanTerms.getLoanTermStartDate().plus(loanTerms.getLoanTermMonths(), ChronoUnit.MONTHS);

        adjustScheduledPayment(loanTerms.getScheduledPaymentFrequency(), loanTerms.getLoanYearlyInterestRate());
    }

    @Override
    public double applyDecreasingBalance(final double decreaseAmount) {
        loanCurrentPrinciple = RoundingUtil.roundValue(loanCurrentPrinciple - decreaseAmount);
        return loanCurrentPrinciple;
    }

    @Override
    public double applyIncreasingBalance(final double increaseAmount) {
        loanCurrentPrinciple = RoundingUtil.roundValue(loanCurrentPrinciple + increaseAmount);
        return loanCurrentPrinciple;
    }

    public void adjustYearlyRate(Double base, final Percent percent) {
        adjustScheduledPayment(scheduledPaymentFrequency, percent);
    }

    public int reduceMonthsLeft() {
        loanTermMonthsRemaining = Math.max(loanTermMonthsRemaining - 1, 0);
        return loanTermMonthsRemaining;
    }

    public void setMonthsLeft(final int monthsLeft) {
        loanTermMonthsRemaining = monthsLeft;
    }

    public void adjustScheduledPayment(final EPaymentFrequency scheduledPaymentFrequency, final Percent loanYearlyInterestRate) {
        final Integer scheduledPaymentsPerYear =
                PaymentFrequencyUtil.regularPaymentsPerYear(scheduledPaymentFrequency);
        this.scheduledPaymentFrequency = scheduledPaymentFrequency;
        this.loanYearlyInterestRate = loanYearlyInterestRate;
        this.scheduledPaymentPerPeriod = PaymentCalculationUtil.PMT(
                loanYearlyInterestRate,
                loanTermMonthsRemaining,
                loanCurrentPrinciple,
                scheduledPaymentsPerYear
        );
    }
}
