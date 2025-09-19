package org.finance.calcs.core.model.components.insurance;

import org.finance.calcs.core.enums.EInsuranceCalcType;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.model.obligationBase.FinancialObligationComponent;
import org.finance.calcs.core.percent.Percent;

import java.time.LocalDate;

public interface InsuranceFOC extends FinancialObligationComponent {
    EInsuranceCalcType getInsuranceCalcType();

    String getInsuranceType();

    String getInsuranceProvider();

    Double getAnnualInsuranceRate();

    Percent getAnnualInsuranceRatePercent();

    Double getScheduledPaymentAmount();

    Double getInsurancePeriodBalance();

    EPaymentFrequency getScheduledPaymentFrequency();

    LocalDate getPeriodEndDate();

    LocalDate getNextPeriodStartDate();

    void updateInterestTerms(InsuranceTerms terms);

    void resetPeriod(final LocalDate startDate);
}
