package org.finance.calcs.core.model.components.loan;

import org.finance.calcs.core.enums.EInterestFrequency;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.percent.Percent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LoanFOCTests {
    @Test
    public void createLoan() {
        final LoanFOC expectedLoanFOC = LoanFOC.builder()
                .loanInitialPrinciple(800000.0)
                .loanCurrentPrinciple(800000.0)
                .loanYearlyInterestRate(Percent.fromDecimal(0.06125, 5))
                .loanTermMonths(360)
                .loanTermStartDate(LocalDate.of(2025, 9, 1))
                .lastProcessedDate(LocalDate.of(2025, 9, 1))
                .loanTermScheduledProjectedEndDate(LocalDate.of(2055, 9, 1))
                .scheduledPaymentPerPeriod(3298.62)
                .scheduledPaymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .interestFrequency(EInterestFrequency.DAILY)
                .loanTermMonthsRemaining(360)
                .build();

        final LoanTerms loanTerms = LoanTerms.builder()
                .loanInitialAmount(800000.0)
                .loanTermMonths(360)
                .loanYearlyInterestRate(Percent.fromPercent(6.125, 5))
                .scheduledPaymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .interestFrequency(EInterestFrequency.DAILY)
                .loanTermStartDate(LocalDate.of(2025, 9, 1))
                .build();
        final LoanFOC loan = new LoanFOC(loanTerms);
        Assertions.assertEquals(expectedLoanFOC, loan);
    }

    @Test
    public void createLoan_defaultValues() {
        final LocalDate today = LocalDate.now();
        final LoanFOC expectedLoanFOC = LoanFOC.builder()
                .loanInitialPrinciple(800000.0)
                .loanCurrentPrinciple(800000.0)
                .loanYearlyInterestRate(Percent.fromDecimal(0.06125, 5))
                .loanTermMonths(360)
                .loanTermMonthsRemaining(360)
                .loanTermStartDate(today)
                .lastProcessedDate(today)
                .loanTermScheduledProjectedEndDate(today.plus(360, ChronoUnit.MONTHS))
                .scheduledPaymentPerPeriod(4860.88)
                .scheduledPaymentFrequency(EPaymentFrequency.MONTHLY)
                .interestFrequency(EInterestFrequency.MONTHLY_12_BY_360)
                .build();

        final LoanTerms loanTerms = LoanTerms.builder()
                .loanInitialAmount(800000.0)
                .loanTermMonths(360)
                .loanYearlyInterestRate(Percent.fromPercent(6.125, 5))
                .build();
        final LoanFOC loan = new LoanFOC(loanTerms);
        Assertions.assertEquals(expectedLoanFOC, loan);
    }

    @Test
    public void loan_ApplyPayment() {
        final LoanFOC expectedLoanFOC = LoanFOC.builder()
                .loanInitialPrinciple(800000.0)
                .loanCurrentPrinciple(750000.0)
                .loanYearlyInterestRate(Percent.fromDecimal(0.06125, 5))
                .loanTermMonths(360)
                .loanTermMonthsRemaining(360)
                .loanTermStartDate(LocalDate.of(2025, 9, 1))
                .lastProcessedDate(LocalDate.of(2025, 9, 1))
                .loanTermScheduledProjectedEndDate(LocalDate.of(2055, 9, 1))
                .scheduledPaymentPerPeriod(4860.88)
                .scheduledPaymentFrequency(EPaymentFrequency.MONTHLY)
                .interestFrequency(EInterestFrequency.MONTHLY_12_BY_360)
                .build();

        final LoanTerms loanTerms = LoanTerms.builder()
                .loanInitialAmount(800000.0)
                .loanTermMonths(360)
                .loanTermStartDate(LocalDate.of(2025, 9, 1))
                .loanYearlyInterestRate(Percent.fromPercent(6.125, 5))
                .build();
        final LoanFOC loan = new LoanFOC(loanTerms);
        loan.applyPayment(50000.0);
        Assertions.assertEquals(expectedLoanFOC, loan);
    }

    @Test
    public void loan_ApplyFee() {
        final LoanFOC expectedLoanFOC = LoanFOC.builder()
                .loanInitialPrinciple(800000.0)
                .loanCurrentPrinciple(850000.0)
                .loanYearlyInterestRate(Percent.fromDecimal(0.06125, 5))
                .loanTermMonths(360)
                .loanTermMonthsRemaining(360)
                .loanTermStartDate(LocalDate.of(2025, 9, 1))
                .lastProcessedDate(LocalDate.of(2025, 9, 1))
                .loanTermScheduledProjectedEndDate(LocalDate.of(2055, 9, 1))
                .scheduledPaymentPerPeriod(4860.88)
                .scheduledPaymentFrequency(EPaymentFrequency.MONTHLY)
                .interestFrequency(EInterestFrequency.MONTHLY_12_BY_360)
                .build();

        final LoanTerms loanTerms = LoanTerms.builder()
                .loanInitialAmount(800000.0)
                .loanTermMonths(360)
                .loanTermStartDate(LocalDate.of(2025, 9, 1))
                .loanYearlyInterestRate(Percent.fromPercent(6.125, 5))
                .build();
        final LoanFOC loan = new LoanFOC(loanTerms);
        loan.applyFee(50000.0);
        Assertions.assertEquals(expectedLoanFOC, loan);
    }
}
