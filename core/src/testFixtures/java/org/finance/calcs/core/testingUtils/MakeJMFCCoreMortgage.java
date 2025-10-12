package org.finance.calcs.core.testingUtils;

import lombok.Builder;
import lombok.NonNull;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.model.components.insurance.InsuranceTerms;
import org.finance.calcs.core.model.components.loan.LoanTerms;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceTerms;
import org.finance.calcs.core.model.metadata.PersonalDetails;
import org.finance.calcs.core.model.obligations.MortgageFO;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiFunction;

public class MakeJMFCCoreMortgage {

    @Builder(builderClassName = "MakeAMortgageBuilder", builderMethodName = "mortgageBuilder")
    public static MortgageFO aMortgageFO(
            EPaymentFrequency frequency,
            PersonalDetails personalDetails,
            LoanTerms loanTerms,
            InsuranceTerms insuranceTerms,
            MortgageInsuranceTerms mortgageInsuranceTerms,
            @NonNull LocalDate startDate,
            double houseValue
    ) {
        final BiFunction<Object, Object, Object> valueOrDefault = (input, defaultValue) ->
                !Objects.isNull(input) ? input : defaultValue;

        final LoanTerms finalLoanTerms = (LoanTerms) valueOrDefault.apply(loanTerms, MakeJMFCCoreFOC.aLoanTerms());
        finalLoanTerms.setLoanTermStartDate(startDate);

        final InsuranceTerms finalInsuranceTerms = (InsuranceTerms) valueOrDefault.apply(insuranceTerms, MakeJMFCCoreFOC.aFlatPaymentInsuranceTerms());
        finalInsuranceTerms.setStartDate(startDate);

        final MortgageInsuranceTerms finalMortgageInsuranceTerms = (MortgageInsuranceTerms) valueOrDefault.apply(mortgageInsuranceTerms, MakeJMFCCoreFOC.aPrivateMortgageInsuranceTerms());
        finalMortgageInsuranceTerms.setStartDate(startDate);

        return new MortgageFO(
                (EPaymentFrequency) valueOrDefault.apply(frequency, EPaymentFrequency.MONTHLY),
                (PersonalDetails) valueOrDefault.apply(personalDetails, MakeJMFCCoreFOC.aPersonalDetails()),
                finalLoanTerms,
                finalInsuranceTerms,
                finalMortgageInsuranceTerms,
                startDate,
                houseValue);
    }
}
