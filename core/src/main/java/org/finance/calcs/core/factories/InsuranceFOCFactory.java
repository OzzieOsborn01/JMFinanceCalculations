package org.finance.calcs.core.factories;

import org.apache.commons.lang3.NotImplementedException;
import org.finance.calcs.core.model.components.insurance.InsuranceFOC;
import org.finance.calcs.core.model.components.insurance.InsuranceTerms;

/**
 * Factory class that will build an {@link InsuranceFOC} based on the provided {@link InsuranceTerms}
 */
public final class InsuranceFOCFactory {

    /**
     * Build the {@link InsuranceFOC} based on the provided {@link InsuranceTerms}
     * @param terms {@link InsuranceTerms} that determine the insurance that will be made
     * @return {@link InsuranceFOC}
     */
    public static InsuranceFOC buildInsuranceFOC(final InsuranceTerms terms) {
        return new InsuranceFOC(terms);
    }
}
