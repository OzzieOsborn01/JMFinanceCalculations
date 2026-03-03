package org.finance.calcs.core.model.components.interest;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Stores the number of days (numberOfDays) that a given interest rate (interestRate) should be applied to
 */
@Value
@AllArgsConstructor
public class InterestRatePair {
    private Integer numberOfDays;
    private Double interestRate;
}
