package org.finance.calcs.core.util;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.finance.calcs.core.enums.EInterestFrequency;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.finance.calcs.core.constants.DateRelatedConstants.*;


public final class InterestFrequencyUtil {
    public static Double calculateInterestAdditionForPeriod(
            final EInterestFrequency interestFrequency,
            final double annualInterestRate,
            final double principle,
            final LocalDate lastPeriodEnd,
            final LocalDate currentPeriodEnd
    ) {
        final List<Pair<Integer, Double>> interestRates =
                determineDailyInterestRatesByInterestFrequency(
                        interestFrequency, annualInterestRate, lastPeriodEnd, currentPeriodEnd);

        final double interestAddition = interestRates.stream()
                .reduce(0.0, (sum, pair) -> sum + (principle * pair.getLeft() * pair.getRight()), Double::sum);

        return RoundingUtil.roundValue(interestAddition);
    }

    public static List<Pair<Integer, Double>> determineDailyInterestRatesByInterestFrequency(
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

    public static List<Pair<Integer, Double>> determineDailyInterestRate(
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
            return List.of(Pair.of((int)durationBetween, dailyInterestRate));
        }

        final long startingPeriodDays =
                ChronoUnit.DAYS.between(workingStartDate, LocalDate.of(workingStartDate.getYear() + 1, 1, 1));
        final long endingPeriodDays =
                ChronoUnit.DAYS.between(LocalDate.of(workingStartDate.getYear() + 1, 1, 1), workingEndDate);

        if (startingDateIsLeapYear) {
            return List.of(
                    Pair.of((int) startingPeriodDays, leapYearInterestRate),
                    Pair.of((int) endingPeriodDays, regularYearInterestRate)
            );
        } else {
            return List.of(
                    Pair.of((int) startingPeriodDays, regularYearInterestRate),
                    Pair.of((int) endingPeriodDays, leapYearInterestRate)
            );
        }
    }

    public static List<Pair<Integer, Double>> determineDailyByWeeklyInterestRate(
            final Double annualInterestRate,
            final LocalDate lastPeriodEnd,
            final LocalDate currentPeriodEnd
    ) {
        final LocalDate workingStartDate = lastPeriodEnd.plusDays(1L);
        final LocalDate workingEndDate = currentPeriodEnd.plusDays(1L);

        final double interestRate = annualInterestRate / (WEEKS_PER_YEAR * DAYS_PER_WEEK);

        final long durationBetween = ChronoUnit.DAYS.between(workingStartDate, workingEndDate);
        return List.of(Pair.of((int)durationBetween, interestRate));
    }


    public static List<Pair<Integer, Double>> determineDailyByMonthlyInterestRate(
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
            return List.of(Pair.of(monthsBetween, monthlyInterest));
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
            return List.of(Pair.of(monthsBetween, monthlyInterest), Pair.of(betweenDays, adjustedDailyInterest));
        }
    }
}
