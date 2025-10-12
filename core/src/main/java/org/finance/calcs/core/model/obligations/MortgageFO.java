package org.finance.calcs.core.model.obligations;

import lombok.*;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.factories.InsuranceFOCFactory;
import org.finance.calcs.core.factories.MortgageInsuranceFOCFactory;
import org.finance.calcs.core.model.components.insurance.InsuranceFOC;
import org.finance.calcs.core.model.components.insurance.InsuranceTerms;
import org.finance.calcs.core.model.components.interest.InterestFOC;
import org.finance.calcs.core.model.components.loan.LoanFOC;
import org.finance.calcs.core.model.components.loan.LoanTerms;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceFOC;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceTerms;
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

    private final MortgageInsuranceFOC mortgageInsuranceComponent;

    private final LocalDate startDate;

    LocalDate lastProcessedDate;

    Boolean isMortgageComplete;

    private double houseValue;

    public MortgageFO(
            @NonNull EPaymentFrequency frequency,
            @NonNull PersonalDetails personalDetails,
            @NonNull LoanTerms loanTerms,
            @NonNull InsuranceTerms homeInsuranceTerms,
            @NonNull MortgageInsuranceTerms mortgageInsuranceTerms,
            @NonNull LocalDate startDate,
            double houseValue
    ) {
        this.scheduledPaymentFrequency = frequency;
        this.personalDetails = personalDetails;
        this.loanComponent = new LoanFOC(loanTerms);
        this.homeInsuranceComponent = InsuranceFOCFactory.buildInsuranceFOC(homeInsuranceTerms);
        this.mortgageInsuranceComponent = MortgageInsuranceFOCFactory.createMortgageInsuranceFOC(mortgageInsuranceTerms);
        this.interestComponent = new InterestFOC();
        this.startDate = startDate;
        this.lastProcessedDate = startDate;
        this.isMortgageComplete = false;
        this.houseValue = houseValue;
    }

    public LocalDate getNextPeriodEndDate() {
        return scheduledPaymentFrequency.getNextDate(lastProcessedDate);
    }

    public double getLoanBalance() {
        return loanComponent.getLoanCurrentPrinciple();
    }
}
