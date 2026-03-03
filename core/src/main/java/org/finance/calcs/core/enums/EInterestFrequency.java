package org.finance.calcs.core.enums;

import org.apache.commons.lang3.tuple.Pair;
import org.finance.calcs.core.model.components.interest.InterestRatePairList;
import org.finance.calcs.core.util.InterestFrequencyUtil;

import java.time.LocalDate;
import java.util.List;

/**
 * Enum to describe how often interest should be calculated. Current values are:
 * <ul>
 *     <li>MONTHLY_12_BY_360</li>
 *     <li>WEEKLY_52_BY_364</li>
 *     <li>DAILY - interest calculated daily</li>
 * </ul>
 * This class is used to get the interest (rate and value) for a specific date range
 */
public enum EInterestFrequency {
    /**
     * Interest calculated monthly and assumes that all months have same interest (effectively 360 days)
     */
    MONTHLY_12_BY_360,
    /**
     * Interest calculated weekly and assumes that all weeks have same interest (effectively 364 days)
     */
    WEEKLY_52_BY_364,
    /**
     * Interest calculated Daily (either 365 or 366 days)
     */
    DAILY,
    NOT_IMPLEMENTED;

    /**
     * Get the effective daily interest rates based on the provided information
     * @param annualInterestRate
     * @param lastPeriodEnd
     * @param currentPeriodEnd
     * @return the daily interest rate
     */
    public InterestRatePairList getDailyInterestRates(
            final double annualInterestRate,
            final LocalDate lastPeriodEnd,
            final LocalDate currentPeriodEnd
    ) {
        return InterestFrequencyUtil.determineDailyInterestRatesByInterestFrequency(
                this,
                annualInterestRate,
                lastPeriodEnd,
                currentPeriodEnd);
    }

    public Double calculateInterestAdditionForPeriod(
            final double annualInterestRate,
            final double principle,
            final LocalDate lastPeriodEnd,
            final LocalDate currentPeriodEnd
    ) {
        return InterestFrequencyUtil.calculateInterestAdditionForPeriod(
                this,
                annualInterestRate,
                principle,
                lastPeriodEnd,
                currentPeriodEnd);
    }
}
