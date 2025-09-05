package org.finance.calcs.core.model.components.interest;

import org.finance.calcs.core.model.obligationBase.FinancialObligationComponent;
import org.finance.calcs.core.percent.Percent;

public class InterestFOC implements FinancialObligationComponent {
    private Double currentInterestBalance;

    @Override
    public double applyDecreasingBalance(double decreaseAmount) {
        currentInterestBalance = currentInterestBalance - decreaseAmount;
        return currentInterestBalance;
    }

    @Override
    public double applyIncreasingBalance(double increaseAmount) {
        currentInterestBalance = currentInterestBalance + increaseAmount;
        return currentInterestBalance;
    }

    @Override
    public void adjustYearlyRate(Percent percent) {
        // No Op
        return;
    }
}
