package org.finance.calcs.core.factories;

import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceFOC;
import org.finance.calcs.core.model.components.mortageInsurance.MortgageInsuranceTerms;

/**
 * Factory class that will build an {@link MortgageInsuranceFOC} based on the provided {@link MortgageInsuranceTerms}
 */
public class MortgageInsuranceFOCFactory {
    /**
     * Build the {@link MortgageInsuranceFOC} based on the provided {@link MortgageInsuranceTerms}
     * @param terms {@link MortgageInsuranceTerms} that determine the mortgage insurance that will be made
     * @return {@link MortgageInsuranceFOC}
     */
    public static MortgageInsuranceFOC createMortgageInsuranceFOC(final MortgageInsuranceTerms terms) {
        return new MortgageInsuranceFOC(terms);
    }
}
