package org.finance.calcs.core.testingUtils;

import lombok.Builder;
import lombok.NonNull;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.model.components.loan.LoanTerms;
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
            @NonNull LocalDate startDate
    ) {
        final BiFunction<Object, Object, Object> valueOrDefault = (input, defaultValue) ->
                !Objects.isNull(input) ? input : defaultValue;

        final LoanTerms finalLoanTerms = (LoanTerms) valueOrDefault.apply(loanTerms, MakeJMFCCoreFOC.aLoanTerms());
        finalLoanTerms.setLoanTermStartDate(startDate);

        return new MortgageFO(
                (EPaymentFrequency) valueOrDefault.apply(frequency, EPaymentFrequency.MONTHLY),
                (PersonalDetails) valueOrDefault.apply(personalDetails, MakeJMFCCoreFOC.aPersonalDetails()),
                finalLoanTerms,
                startDate);
    }
}
