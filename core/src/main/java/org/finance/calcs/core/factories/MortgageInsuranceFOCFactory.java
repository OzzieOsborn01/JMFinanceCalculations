package org.finance.calcs.core.factories;

import org.apache.commons.lang3.NotImplementedException;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceFOC;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceTerms;
import org.finance.calcs.core.model.components.mortageInsurance.PrivateMortgageInsuranceFOC;

public class MortgageInsuranceFOCFactory {
    public static MortgageInsuranceFOC createMortgageInsuranceFOC(final MortgageInsuranceTerms terms) {
        return switch (terms.getMortgageInsuranceType()) {
            case PRIVATE_MORTGAGE_INSURANCE -> new PrivateMortgageInsuranceFOC(terms);
//            case MORTGAGE_INSURANCE_PREMIUMS -> null;
            default -> throw new NotImplementedException("Insurance Calculation Type Not Implemented");
        };
    }
}
