package org.finance.calcs.core.model.components.interest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.finance.calcs.core.util.RoundingUtil;

import java.util.Arrays;
import java.util.List;

/**
 * This class contains a list of {@link InterestRatePair} and contains methods related to this including a reduce
 * function to calculate the applied interest for a given range of dates and methods to round this value.
 */
@Data
@AllArgsConstructor
public class InterestRatePairList {
    List<InterestRatePair> interestRatePairs;

    /**
     * Constructor that takes a collection of interest rate pairs
     * @param interestRatePairs {@link InterestRatePair} to build the list with
     */
    public InterestRatePairList(final InterestRatePair ... interestRatePairs){
        this.interestRatePairs = Arrays.asList(interestRatePairs);
    }

    /**
     * Constructor that takes a singular interest rate pair and produces a list
     * @param interestRatePair {@link InterestRatePair}
     */
    public InterestRatePairList(final InterestRatePair interestRatePair){
        interestRatePairs = Arrays.asList(interestRatePair);
    }

    /**
     * Constructor that takes the individual components of an interest rate pair and produces a one-element interest
     * rate pair list
     * @param numberOfDays number of days the interest rate should be applied for
     * @param interestRate interest rate that should be applied for a given number of days
     */
    public InterestRatePairList(final Integer numberOfDays, final Double interestRate){
        interestRatePairs = Arrays.asList(new InterestRatePair(numberOfDays, interestRate));
    }

    /**
     * Determine the interest amount to be applied given the provided principle amount.
     * @param principle balance to get the interest amount from
     * @return the interest amount for the given interest rates based on the provided principle
     */
    public Double simplifyAndCalculateInterestAmount(Double principle) {
        return interestRatePairs.stream()
                .reduce(0.0,
                        (sum, pair) -> sum + (principle * pair.getInterestRate() * pair.getNumberOfDays()),
                        Double::sum);
    }

    /**
     * Determine the interest amount to be applied given the provided principle amount.
     * @param principle balance to get the interest amount from
     * @param roundingDigits number of decimal digit places to round to
     * @return the interest amount for the given interest rates based on the provided principle (rounded to X digit)
     */
    public Double simplifyAndCalculateInterestAmountRounded(Double principle, Integer roundingDigits) {
        return RoundingUtil.roundValue(simplifyAndCalculateInterestAmount(principle), roundingDigits);
    }

    /**
     * Determine the interest amount to be applied given the provided principle amount. Rounded to nearest 2 digit places
     * @param principle balance to get the interest amount from
     * @return the interest amount for the given interest rates based on the provided principle (rounded to 2 digit)
     */
    public Double simplifyAndCalculateInterestAmountRounded(Double principle) {
        return RoundingUtil.roundValue(simplifyAndCalculateInterestAmount(principle));
    }
}
