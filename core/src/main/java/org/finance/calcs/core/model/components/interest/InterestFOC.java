package org.finance.calcs.core.model.components.interest;

import org.finance.calcs.core.model.obligationBase.FinancialObligationComponent;
import org.finance.calcs.core.percent.Percent;

import java.time.LocalDate;

/**
 * TODO: THIS IS INCOMPLETE AND NOT USED YET
 * <p/>
 * InterestFOC represents the interest component for interest that is gained in an FO
 */
public class InterestFOC implements FinancialObligationComponent {
    private Double currentInterestBalance;

    /**
     * {@inheritDoc}
     */
    @Override
    public double applyDecreasingBalance(double decreaseAmount) {
        currentInterestBalance = currentInterestBalance - decreaseAmount;
        return currentInterestBalance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double applyIncreasingBalance(double increaseAmount) {
        currentInterestBalance = currentInterestBalance + increaseAmount;
        return currentInterestBalance;
    }

    /**
     * {@inheritDoc}
     * @return {@link LocalDate#EPOCH} because any interest is past due
     */
    @Override
    public LocalDate getNextPeriodStartDate() {
        return LocalDate.EPOCH;
    }
}
