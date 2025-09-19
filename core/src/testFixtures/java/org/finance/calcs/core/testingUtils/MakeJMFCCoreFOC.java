package org.finance.calcs.core.testingUtils;

import org.finance.calcs.core.enums.EInsuranceCalcType;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.factories.InsuranceFOCFactory;
import org.finance.calcs.core.model.components.insurance.InsuranceFOC;
import org.finance.calcs.core.model.components.insurance.InsuranceTerms;
import org.finance.calcs.core.model.components.interest.InterestFOC;
import org.finance.calcs.core.model.components.loan.LoanFOC;
import org.finance.calcs.core.model.components.loan.LoanTerms;
import org.finance.calcs.core.model.metadata.Debt;
import org.finance.calcs.core.model.metadata.PersonalDetails;
import org.finance.calcs.core.percent.Percent;

import java.util.List;
import java.util.Random;

public final class MakeJMFCCoreFOC {
    final static Random random = new Random();

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

    public static InsuranceFOC aFlatPaymentInsuranceFOC(
            final String insuranceType,
            final String insuranceProvider,
            final EPaymentFrequency paymentFrequency,
            final EPaymentFrequency periodFrequency,
            final Double paymentRate
    ) {
        return InsuranceFOCFactory.buildInsuranceFOC(aFlatPaymentInsuranceTerms(insuranceType, insuranceProvider, paymentFrequency, periodFrequency, paymentRate));
    }

    public static InsuranceFOC aRandomFlatPaymentInsuranceFOC() {
        return InsuranceFOCFactory.buildInsuranceFOC(aRandomFlatPaymentInsuranceTerms());
    }

    public static InsuranceFOC aFlatPaymentInsuranceFOC() {
        return InsuranceFOCFactory.buildInsuranceFOC(aFlatPaymentInsuranceTerms());
    }

    public static InsuranceTerms aFlatPaymentInsuranceTerms(
            final String insuranceType,
            final String insuranceProvider,
            final EPaymentFrequency paymentFrequency,
            final EPaymentFrequency periodFrequency,
            final Double paymentRate
    ) {
        return InsuranceTerms.builder()
                .insuranceCalcType(EInsuranceCalcType.FLAT_PAYMENT)
                .insuranceType(insuranceType)
                .insuranceProvider(insuranceProvider)
                .insurancePeriodDuration(periodFrequency)
                .paymentFrequency(paymentFrequency)
                .flatRateAnnualInsuranceRate(paymentRate)
                .build();
    }
    public static InsuranceTerms aRandomFlatPaymentInsuranceTerms() {
        return InsuranceTerms.builder()
                .insuranceCalcType(EInsuranceCalcType.FLAT_PAYMENT)
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .insurancePeriodDuration(EPaymentFrequency.YEARLY)
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .flatRateAnnualInsuranceRate((double)random.nextInt(50, 75000))
                .build();
    }
    public static InsuranceTerms aFlatPaymentInsuranceTerms() {
        return InsuranceTerms.builder()
                .insuranceCalcType(EInsuranceCalcType.FLAT_PAYMENT)
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .insurancePeriodDuration(EPaymentFrequency.YEARLY)
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .flatRateAnnualInsuranceRate(4000.0)
                .build();
    }
}
