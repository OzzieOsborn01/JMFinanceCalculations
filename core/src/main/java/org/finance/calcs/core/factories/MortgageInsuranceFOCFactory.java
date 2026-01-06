package org.finance.calcs.core.factories;

import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceFOC;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceTerms;

public class MortgageInsuranceFOCFactory {
    public static MortgageInsuranceFOC createMortgageInsuranceFOC(final MortgageInsuranceTerms terms) {
        return new MortgageInsuranceFOC(terms);
    }
}
