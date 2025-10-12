package org.finance.calcs.core.factories;

import org.apache.commons.lang3.NotImplementedException;
import org.finance.calcs.core.model.components.insurance.InsuranceFOC;
import org.finance.calcs.core.model.components.insurance.InsuranceTerms;

public final class InsuranceFOCFactory {
    public static InsuranceFOC buildInsuranceFOC(final InsuranceTerms terms) {
        return new InsuranceFOC(terms);
    }
}
