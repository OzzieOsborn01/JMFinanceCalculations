package org.finance.calcs.core.model.components.interest;

import org.finance.calcs.core.model.obligationBase.FinancialObligationComponent;
import org.finance.calcs.core.percent.Percent;

/**
 * TODO: THIS IS INCOMPLETE AND NOT USED YET
 * <p/>
 * InterestFOC represents the interest component for interest that is gained in an FO
 */
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
}
