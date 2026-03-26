package org.finance.calcs.core.model.components.insurance;

import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.factories.InsuranceFOCFactory;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.finance.calcs.core.percent.Percent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class InsuranceFOCTests {
    @Test
    public void createFlatPaymentInsurance() {
        final InsuranceFOC expectedInsuranceFOC = InsuranceFOC.builder()
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .paymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .scheduledPaymentAmount(333.33)
                .insurancePeriodDuration(EPaymentFrequency.SEMI_ANNUALLY)
                .startDate(LocalDate.of(2025, 9, 1))
                .periodStartDate(LocalDate.of(2025, 9, 1))
                .periodEndDate(LocalDate.of(2026, 3, 1))
                .periodBalance(4000.0)
                .build();

        final InsuranceTerms insuranceTerms = InsuranceTerms.builder()
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .insurancePeriodDuration(EPaymentFrequency.SEMI_ANNUALLY)
                .paymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final InsuranceFOC insurance = InsuranceFOCFactory.buildInsuranceFOC(insuranceTerms);
        Assertions.assertEquals(expectedInsuranceFOC, insurance);
    }

    @Test
    public void createFlatPaymentInsurance_DefaultValues() {
        final LocalDate today = LocalDate.now();
        final InsuranceFOC expectedInsuranceFOC = InsuranceFOC.builder()
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .insurancePeriodDuration(EPaymentFrequency.YEARLY)
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .scheduledPaymentAmount(333.33)
                .startDate(today)
                .periodStartDate(today)
                .periodEndDate(today.plusYears(1))
                .periodBalance(4000.0)
                .build();

        final InsuranceTerms insuranceTerms = InsuranceTerms.builder()
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .build();

        final InsuranceFOC insurance = InsuranceFOCFactory.buildInsuranceFOC(insuranceTerms);
        Assertions.assertEquals(expectedInsuranceFOC, insurance);
    }

    @Test
    public void interest_ApplyPayment() {
        final InsuranceFOC expectedInsuranceFOC = InsuranceFOC.builder()
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .paymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .scheduledPaymentAmount(333.33)
                .insurancePeriodDuration(EPaymentFrequency.SEMI_ANNUALLY)
                .startDate(LocalDate.of(2025, 9, 1))
                .periodStartDate(LocalDate.of(2025, 9, 1))
                .periodEndDate(LocalDate.of(2026, 3, 1))
                .periodBalance(3600.0)
                .build();

        final InsuranceTerms insuranceTerms = InsuranceTerms.builder()
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .insurancePeriodDuration(EPaymentFrequency.SEMI_ANNUALLY)
                .paymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final InsuranceFOC insurance = InsuranceFOCFactory.buildInsuranceFOC(insuranceTerms);
        insurance.applyPayment(400.0);
        Assertions.assertEquals(expectedInsuranceFOC, insurance);
    }

    @Test
    public void interest_ApplyFee() {
        final InsuranceFOC expectedInsuranceFOC = InsuranceFOC.builder()
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .paymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .scheduledPaymentAmount(333.33)
                .insurancePeriodDuration(EPaymentFrequency.SEMI_ANNUALLY)
                .startDate(LocalDate.of(2025, 9, 1))
                .periodStartDate(LocalDate.of(2025, 9, 1))
                .periodEndDate(LocalDate.of(2026, 3, 1))
                .periodBalance(4400.0)
                .build();

        final InsuranceTerms insuranceTerms = InsuranceTerms.builder()
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .insurancePeriodDuration(EPaymentFrequency.SEMI_ANNUALLY)
                .paymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final InsuranceFOC insurance = InsuranceFOCFactory.buildInsuranceFOC(insuranceTerms);
        insurance.applyFee(400.0);
        Assertions.assertEquals(expectedInsuranceFOC, insurance);
    }

    @Test
    public void interest_getters() {
        final InsuranceTerms insuranceTerms = InsuranceTerms.builder()
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .insurancePeriodDuration(EPaymentFrequency.SEMI_ANNUALLY)
                .paymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final InsuranceFOC insurance = InsuranceFOCFactory.buildInsuranceFOC(insuranceTerms);
        Assertions.assertEquals(8000.0, insurance.getAnnualInsuranceRate());
        Assertions.assertEquals(Percent.ZERO_PERCENT, insurance.getInsuranceRatePercent());
        Assertions.assertEquals(4000.0, insurance.getInsurancePeriodBalance());
        Assertions.assertEquals(333.33, insurance.getScheduledPaymentAmount());
        Assertions.assertEquals(
                LocalDate.of(2025, 9, 1).plusMonths(6),
                insurance.getNextPeriodStartDate());
    }

    @Test
    public void interest_adjustScheduledPayment() {
        final InsuranceFOC expectedInsuranceFOC = InsuranceFOC.builder()
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .scheduledPaymentAmount(333.33)
                .insurancePeriodDuration(EPaymentFrequency.YEARLY)
                .startDate(LocalDate.of(2025, 9, 1))
                .periodStartDate(LocalDate.of(2025, 9, 1))
                .periodEndDate(LocalDate.of(2026, 9, 1))
                .periodBalance(4000.0)
                .build();

        final InsuranceTerms insuranceTerms = InsuranceTerms.builder()
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .insurancePeriodDuration(EPaymentFrequency.SEMI_ANNUALLY)
                .paymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final InsuranceFOC insurance = InsuranceFOCFactory.buildInsuranceFOC(insuranceTerms);
        insurance.adjustScheduledPayment(EPaymentFrequency.MONTHLY, EPaymentFrequency.YEARLY, new PaymentCalculation(4000.0));
        Assertions.assertEquals(expectedInsuranceFOC, insurance);
    }

    @Test
    public void interest_resetPeriod() {
        final InsuranceFOC expectedInsuranceFOC = InsuranceFOC.builder()
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .paymentFrequency(EPaymentFrequency.MONTHLY)
                .scheduledPaymentAmount(333.33)
                .insurancePeriodDuration(EPaymentFrequency.YEARLY)
                .startDate(LocalDate.of(2025, 9, 1))
                .periodStartDate(LocalDate.of(2025, 9, 1))
                .periodEndDate(LocalDate.of(2026, 9, 1))
                .periodBalance(4000.0)
                .build();

        final InsuranceTerms insuranceTerms = InsuranceTerms.builder()
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .insurancePeriodDuration(EPaymentFrequency.SEMI_ANNUALLY)
                .paymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final InsuranceFOC insurance = InsuranceFOCFactory.buildInsuranceFOC(insuranceTerms);
        insurance.adjustScheduledPayment(EPaymentFrequency.MONTHLY, EPaymentFrequency.YEARLY, new PaymentCalculation(4000.0));
        Assertions.assertEquals(expectedInsuranceFOC, insurance);
    }

    @Test
    public void nullTerms_Exception() {
        Assertions.assertThrows(NullPointerException.class, () -> InsuranceFOCFactory.buildInsuranceFOC(null));
    }

    @Test
    public void resetPeriod_NullDate_Exception() {
        final InsuranceTerms insuranceTerms = InsuranceTerms.builder()
                .startDate(LocalDate.of(2025, 9, 1))
                .paymentCalculation(new PaymentCalculation(4000.0))
                .insuranceType("home")
                .insuranceProvider("garbage company")
                .insurancePeriodDuration(EPaymentFrequency.SEMI_ANNUALLY)
                .paymentFrequency(EPaymentFrequency.BI_WEEKLY)
                .build();

        final InsuranceFOC insurance = InsuranceFOCFactory.buildInsuranceFOC(insuranceTerms);
        Assertions.assertThrows(NullPointerException.class, () -> insurance.resetPeriod(null));
    }
}
