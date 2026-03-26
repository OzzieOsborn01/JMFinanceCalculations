package org.finance.calcs.core.util;

import org.apache.commons.lang3.NotImplementedException;
import org.finance.calcs.core.enums.EInterestFrequency;
import org.finance.calcs.core.model.components.interest.InterestRatePair;
import org.finance.calcs.core.model.components.interest.InterestRatePairList;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.finance.calcs.core.constants.DateRelatedConstants.*;

/**
 * Utility class that helps with the calculation of interest including:
 * <ul>
 *     <li>Calculating interest addition amount for a given date period</li>
 *     <li>Determining daily interest rate by {@link EInterestFrequency}</li>
 * </ul>
 * @see EInterestFrequency
 */
public final class InterestFrequencyUtil {
    /**
     * Calculate the amount of interest to add for a given period of time
     * @param interestFrequency how often the interest is calculated
     * @param annualInterestRate decimal value of the annual interest rate (example 0.25 not 25.0%)
     * @param principle the principle that the interest amount should be based on
     * @param lastPeriodEnd the last period that was fully completed. Serves as the starting point
     * @param currentPeriodEnd the current period expected end date. Serves as the ending point
     * @return Interest that should be added as a double value. Value is already rounded to hundredths place.
     */
    public static Double calculateInterestAdditionForPeriod(
            final EInterestFrequency interestFrequency,
            final double annualInterestRate,
            final double principle,
            final LocalDate lastPeriodEnd,
            final LocalDate currentPeriodEnd
    ) {
        final InterestRatePairList interestRates =
                determineDailyInterestRatesByInterestFrequency(
                        interestFrequency, annualInterestRate, lastPeriodEnd, currentPeriodEnd);
        return interestRates.simplifyAndCalculateInterestAmountRounded(principle);
    }

    /**
     * Determines the daily interest rate
     * @param interestFrequency {@link EInterestFrequency} that interest should be calculated for
     * @param annualInterestRate decimal value of the annual interest rate (example 0.25 not 25.0%)
     * @param lastPeriodEnd last time that interest was calculated
     * @param currentPeriodEnd last point that interest should be calculated for
     * @return {@link InterestRatePairList}
     * @throws NotImplementedException if the interest frequency is not implemented yet
     */
    public static InterestRatePairList determineDailyInterestRatesByInterestFrequency(
        final EInterestFrequency interestFrequency,
        final double annualInterestRate,
        final LocalDate lastPeriodEnd,
        final LocalDate currentPeriodEnd
    ) {
        switch (interestFrequency) {
            case MONTHLY_12_BY_360 -> {
                return determineDailyByMonthlyInterestRate(
                        annualInterestRate,
                        lastPeriodEnd,
                        currentPeriodEnd
                );
            }
            case WEEKLY_52_BY_364 -> {
               return determineDailyByWeeklyInterestRate(
                        annualInterestRate,
                        lastPeriodEnd,
                        currentPeriodEnd
                );
            }
            case DAILY -> {
                return determineDailyInterestRate(
                        annualInterestRate,
                        lastPeriodEnd,
                        currentPeriodEnd
                );
            }
            default -> {
                throw new NotImplementedException("Not Implemented");
            }
        }
    }

    /**
     * Determine the daily interest rate assuming 365 (or 366 for leap years) days in a year
     * @param annualInterestRate decimal value of the annual interest rate (example 0.25 not 25.0%)
     * @param lastPeriodEnd last time that interest was calculated
     * @param currentPeriodEnd last point that interest should be calculated for
     * @return {@link InterestRatePairList}
     */
    public static InterestRatePairList determineDailyInterestRate(
            final Double annualInterestRate,
            final LocalDate lastPeriodEnd,
            final LocalDate currentPeriodEnd
    ) {
        final LocalDate workingStartDate = lastPeriodEnd.plusDays(1L);
        final LocalDate workingEndDate = currentPeriodEnd.plusDays(1L);

        final boolean startingDateIsLeapYear = workingStartDate.plusDays(1L).isLeapYear();
        final boolean endingDateIsLeapYear = workingEndDate.plusDays(1L).isLeapYear();

        final double regularYearInterestRate = annualInterestRate / DAYS_PER_YEAR;
        final double leapYearInterestRate = annualInterestRate / DAYS_PER_LEAP_YEAR;

        if (startingDateIsLeapYear == endingDateIsLeapYear) {
            final long durationBetween = ChronoUnit.DAYS.between(workingStartDate, workingEndDate);
            double dailyInterestRate = startingDateIsLeapYear ? leapYearInterestRate : regularYearInterestRate;
            return new InterestRatePairList((int)durationBetween, dailyInterestRate);
        }

        final long startingPeriodDays =
                ChronoUnit.DAYS.between(workingStartDate, LocalDate.of(workingStartDate.getYear() + 1, 1, 1));
        final long endingPeriodDays =
                ChronoUnit.DAYS.between(LocalDate.of(workingStartDate.getYear() + 1, 1, 1), workingEndDate);

        if (startingDateIsLeapYear) {
            return new InterestRatePairList(
                    new InterestRatePair((int) startingPeriodDays, leapYearInterestRate),
                    new InterestRatePair((int) endingPeriodDays, regularYearInterestRate)
            );
        } else {
            return new InterestRatePairList(
                    new InterestRatePair((int) startingPeriodDays, regularYearInterestRate),
                    new InterestRatePair((int) endingPeriodDays, leapYearInterestRate)
            );
        }
    }

    /**
     * Determine the daily interest rate assuming 364 days in a year (7 days per week)
     * @param annualInterestRate decimal value of the annual interest rate (example 0.25 not 25.0%)
     * @param lastPeriodEnd last time that interest was calculated
     * @param currentPeriodEnd last point that interest should be calculated for
     * @return {@link InterestRatePairList}
     */
    public static InterestRatePairList determineDailyByWeeklyInterestRate(
            final Double annualInterestRate,
            final LocalDate lastPeriodEnd,
            final LocalDate currentPeriodEnd
    ) {
        final LocalDate workingStartDate = lastPeriodEnd.plusDays(1L);
        final LocalDate workingEndDate = currentPeriodEnd.plusDays(1L);

        final double interestRate = annualInterestRate / (WEEKS_PER_YEAR * DAYS_PER_WEEK);

        final long durationBetween = ChronoUnit.DAYS.between(workingStartDate, workingEndDate);

        return new InterestRatePairList((int)durationBetween, interestRate);
    }

    /**
     * Determine the daily interest rate assuming 360 days in a year (30 days per month)
     * @param annualInterestRate decimal value of the annual interest rate (example 0.25 not 25.0%)
     * @param lastPeriodEnd last time that interest was calculated
     * @param currentPeriodEnd last point that interest should be calculated for
     * @return {@link InterestRatePairList}
     */public static InterestRatePairList determineDailyByMonthlyInterestRate(
            final Double annualInterestRate,
            final LocalDate lastPeriodEnd,
            final LocalDate currentPeriodEnd
    ) {
        final LocalDate workingStartDate = lastPeriodEnd.plusDays(1L);
        final LocalDate workingEndDate = currentPeriodEnd.plusDays(1L);

        final double monthlyInterest = annualInterestRate / MONTHS_PER_YEAR;

        final int monthsBetween = (int)ChronoUnit.MONTHS.between(workingStartDate, workingEndDate);
        final boolean matchingMonths = workingStartDate.plusMonths(monthsBetween).isEqual(workingEndDate);
        if (matchingMonths) {
            return new InterestRatePairList(monthsBetween, monthlyInterest);
        } else {
            final int betweenDays = (int)ChronoUnit.DAYS.between(
                    workingStartDate.plusMonths(monthsBetween),
                    workingEndDate
            );
            final long daysUntilNextMonth = ChronoUnit.DAYS.between(
                    workingStartDate.plusMonths(monthsBetween),
                    workingStartDate.plusMonths(monthsBetween + 1L)
            );
            final double adjustedDailyInterest = monthlyInterest / daysUntilNextMonth;
            return new InterestRatePairList(
                    new InterestRatePair(monthsBetween, monthlyInterest),
                    new InterestRatePair(betweenDays, adjustedDailyInterest)
            );
        }
    }
}
