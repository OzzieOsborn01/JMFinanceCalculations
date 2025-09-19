package org.finance.calcs.core.model.obligationBase;

import org.finance.calcs.core.percent.Percent;

public interface FinancialObligationComponent {

    default double applyPayment(double paymentAmount){
        return applyDecreasingBalance(paymentAmount);
    }

    default double applyFee(double feeAmount) {
        return applyIncreasingBalance(feeAmount);
    }

    double applyDecreasingBalance(double amount);

    double applyIncreasingBalance(double amount);

    void adjustYearlyRate(Double base, Percent percent);
}
