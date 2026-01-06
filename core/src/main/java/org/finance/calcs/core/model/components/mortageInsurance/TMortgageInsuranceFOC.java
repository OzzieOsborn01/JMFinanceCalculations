package org.finance.calcs.core.model.components.mortageInsurance;

import org.finance.calcs.core.enums.EMortgageInsuranceType;
import org.finance.calcs.core.model.obligationBase.FinancialObligationComponent;
import org.finance.calcs.core.percent.Percent;

import java.time.LocalDate;

public interface TMortgageInsuranceFOC extends FinancialObligationComponent {
    Double getAnnualInsuranceRate();
    EMortgageInsuranceType getMortgageInsuranceType();
    Boolean isInsuranceComplete();
    Double getScheduledPayment();
    Percent getLoanToValueRatio();
    LocalDate getStartDate();

    Double getUpfrontPremium();

    Boolean isSoftTerminationComplete();

    Boolean isHardTerminationComplete();

    void updateLoanValue(final Double loanValue);

    void updateHouseValue(final Double houseValue);

    void updateLastProcessedDate(final LocalDate lastProcessedDate);

    void updateLoanValueAndHouseValue(final Double loanValue, final Double houseValue);

    void determineBalanceTerminationScore();

    void determineDurationTerminationScore();

    LocalDate getNextPeriodStartDate();

    void resetPeriod(final LocalDate startDate);

    void determineTerminationScore(final Boolean useSoftTerminationStrategy);
}
