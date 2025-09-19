package org.finance.calcs.core.model.obligations;

import lombok.*;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.factories.InsuranceFOCFactory;
import org.finance.calcs.core.model.components.insurance.InsuranceFOC;
import org.finance.calcs.core.model.components.insurance.InsuranceTerms;
import org.finance.calcs.core.model.components.interest.InterestFOC;
import org.finance.calcs.core.model.components.loan.LoanFOC;
import org.finance.calcs.core.model.components.loan.LoanTerms;
import org.finance.calcs.core.model.metadata.PersonalDetails;
import org.finance.calcs.core.model.obligationBase.FinancialObligation;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class MortgageFO implements FinancialObligation {

    private EPaymentFrequency scheduledPaymentFrequency;

    private final PersonalDetails personalDetails;

    private final LoanFOC loanComponent;

    private final InterestFOC interestComponent;

    private final InsuranceFOC homeInsuranceComponent;

    private final LocalDate startDate;

    LocalDate lastProcessedDate;

    Boolean isMortgageComplete;

    public MortgageFO(
            @NonNull EPaymentFrequency frequency,
            @NonNull PersonalDetails personalDetails,
            @NonNull LoanTerms loanTerms,
            @NonNull InsuranceTerms homeInsuranceTerms,
            @NonNull LocalDate startDate
    ) {
        this.scheduledPaymentFrequency = frequency;
        this.personalDetails = personalDetails;
        this.loanComponent = new LoanFOC(loanTerms);
        this.homeInsuranceComponent = InsuranceFOCFactory.buildInsuranceFOC(homeInsuranceTerms);
        this.interestComponent = new InterestFOC();
        this.startDate = startDate;
        this.lastProcessedDate = startDate;
        this.isMortgageComplete = false;
    }

    public LocalDate getNextPeriodEndDate() {
        return scheduledPaymentFrequency.getNextDate(lastProcessedDate);
    }

    public double getLoanBalance() {
        return loanComponent.getLoanCurrentPrinciple();
    }
}
