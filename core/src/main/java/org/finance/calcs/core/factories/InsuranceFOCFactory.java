package org.finance.calcs.core.factories;

import org.apache.commons.lang3.NotImplementedException;
import org.finance.calcs.core.model.components.insurance.InsuranceFOC;
import org.finance.calcs.core.model.components.insurance.InsuranceFlatPaymentFOC;
import org.finance.calcs.core.model.components.insurance.InsuranceTerms;

public final class InsuranceFOCFactory {
    public static InsuranceFOC buildInsuranceFOC(final InsuranceTerms terms) {
        return switch (terms.getInsuranceCalcType()) {
            case FLAT_PAYMENT -> new InsuranceFlatPaymentFOC(terms);
//            case PERCENT_PAYMENT -> null;
            default -> throw new NotImplementedException("Insurance Calculation Type Not Implemented");
        };
    }
}
