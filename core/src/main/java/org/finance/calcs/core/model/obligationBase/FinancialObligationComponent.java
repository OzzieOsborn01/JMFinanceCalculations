package org.finance.calcs.core.model.obligationBase;

import java.time.LocalDate;

/**
 * FinancialObligationComponent (FOC) represents a generic financial obligation component. These components can represent
 * the totality or a portion of a given {@link FinancialObligation}. Classes that are components are labeled as
 * &ltComponent>FOC (e.g. LoanFOC)
 * <p/>
 * FOCs can either be processed sequentially or manually by the parent Financial Obligation depending on the needs.
 * <p/>
 * Using the example of a mortgage: The mortgage is a {@link FinancialObligation} and the loan, insurance, HOA fees,
 * etc. are all examples of {@link FinancialObligationComponent}
 */
public interface FinancialObligationComponent {

    /**
     * Apply a payment to a given FOC. Generally this decreases the balance on the obligation
     * @param paymentAmount payment amount to apply to the balance of the obligation
     * @return the new FOC balance
     */
    default double applyPayment(double paymentAmount){
        return applyDecreasingBalance(paymentAmount);
    }

    /**
     * Apply a fee to a given FOC. Generally this increases the balance on the obligation
     * @param feeAmount fee to apply to the balance of the obligation
     * @return the new FOC balance
     */
    default double applyFee(double feeAmount) {
        return applyIncreasingBalance(feeAmount);
    }

    /**
     * Apply a decreasing balance to the FOC
     * @param amount to decrease the balance by
     * @return the new balance
     */
    double applyDecreasingBalance(double amount);

    /**
     * Apply an increasing balance to the FOC
     * @param amount to increase the balance by
     * @return the new balance
     */
    double applyIncreasingBalance(double amount);

    /**
     * Get the start date of the next period
     * @return {@link LocalDate}
     */
    abstract public LocalDate getNextPeriodStartDate();
}
