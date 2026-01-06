package org.finance.calcs.core.model.obligationBase;

public interface FinancialObligationComponent {

    default double applyPayment(double paymentAmount){
        return applyDecreasingBalance(paymentAmount);
    }

    default double applyFee(double feeAmount) {
        return applyIncreasingBalance(feeAmount);
    }

    double applyDecreasingBalance(double amount);

    double applyIncreasingBalance(double amount);
}
