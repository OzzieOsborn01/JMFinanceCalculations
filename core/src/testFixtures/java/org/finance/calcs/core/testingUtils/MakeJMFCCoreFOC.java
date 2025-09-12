package org.finance.calcs.core.testingUtils;

import org.finance.calcs.core.model.components.interest.InterestFOC;
import org.finance.calcs.core.model.components.loan.LoanFOC;
import org.finance.calcs.core.model.components.loan.LoanTerms;
import org.finance.calcs.core.model.metadata.Debt;
import org.finance.calcs.core.model.metadata.PersonalDetails;
import org.finance.calcs.core.percent.Percent;

import java.util.List;
import java.util.Random;

public final class MakeJMFCCoreFOC {
    public static LoanFOC aLoanFOC(final double loanAmount, final Percent interest, final int loanMonths) {
        return new LoanFOC(
            LoanTerms.builder()
                .loanInitialAmount(loanAmount)
                .loanYearlyInterestRate(interest)
                .loanTermMonths(loanMonths)
                .build()
        );
    }
    public static LoanFOC aRandomLoanFOC() {
        final Random random = new Random();
        return new LoanFOC(
                LoanTerms.builder()
                        .loanInitialAmount((double)random.nextInt(50000, 975001))
                        .loanYearlyInterestRate(Percent.fromDecimal(random.nextDouble(0.01, 0.15)))
                        .loanTermMonths(random.nextInt(12, 360))
                        .build()
        );
    }
    public static LoanFOC aLoanFOC() {
        return new LoanFOC(
                LoanTerms.builder()
                        .loanInitialAmount(775000.0)
                        .loanYearlyInterestRate(Percent.fromPercent(6.125, 5))
                        .loanTermMonths(360)
                        .build()
        );
    }
    public static LoanTerms aLoanTerms(final double loanAmount, final Percent interest, final int loanMonths) {
        return LoanTerms.builder()
                .loanInitialAmount(loanAmount)
                .loanYearlyInterestRate(interest)
                .loanTermMonths(loanMonths)
                .build();
    }
    public static LoanTerms aRandomLoanTerms() {
        final Random random = new Random();
        return LoanTerms.builder()
                .loanInitialAmount((double)random.nextInt(50000, 975001))
                .loanYearlyInterestRate(Percent.fromDecimal(random.nextDouble(0.01, 0.15)))
                .loanTermMonths(random.nextInt(12, 360))
                .build();
    }
    public static LoanTerms aLoanTerms() {
        return LoanTerms.builder()
                .loanInitialAmount(775000.0)
                .loanYearlyInterestRate(Percent.fromPercent(6.125, 5))
                .loanTermMonths(360)
                .build();
    }
    public static PersonalDetails aPersonalDetails(final double grossIncome, final double netIncome, final List<Debt> debts) {
        return PersonalDetails.builder()
                .grossIncome(grossIncome)
                .netIncome(netIncome)
                .debts(debts)
                .build();
    }
    public static PersonalDetails aPersonalDetails() {
        return PersonalDetails.builder()
                .grossIncome(200000.0)
                .netIncome(150000.0)
                .debts(List.of())
                .build();
    }
    public static InterestFOC aInterestFOC() {
        return new InterestFOC();
    }
}
