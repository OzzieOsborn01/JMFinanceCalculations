package org.finance.calcs.core.util;

import org.apache.commons.lang3.tuple.Pair;
import org.finance.calcs.core.enums.EInterestFrequency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class InterestFrequencyUtilTest {
    @Test
    public void calculateDailyBasedInterestRate() {
        final List<Pair<Integer, Double>> expectedInterestRates =
                List.of(
                        Pair.of(31, 0.00016780821917808218)
                );

        final List<Pair<Integer, Double>> dailyInterestRate =
                InterestFrequencyUtil.determineDailyInterestRatesByInterestFrequency(
                        EInterestFrequency.DAILY,
                        0.06125,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 2, 1)
                );

        Assertions.assertEquals(expectedInterestRates, dailyInterestRate);
    }

    @Test
    public void calculateDailyBasedInterestRate_LeapYear() {
        final List<Pair<Integer, Double>> expectedInterestRates =
                List.of(
                        Pair.of(31, 0.00016734972677595628)
                );

        final List<Pair<Integer, Double>> dailyInterestRate =
                InterestFrequencyUtil.determineDailyInterestRatesByInterestFrequency(
                        EInterestFrequency.DAILY,
                        0.06125,
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 2, 1)
                );

        Assertions.assertEquals(expectedInterestRates, dailyInterestRate);
    }

    @Test
    public void calculateDailyBasedInterestRate_LeapYearToRegularYear() {
        final List<Pair<Integer, Double>> expectedInterestRates =
                List.of(
                        Pair.of(16, 0.00016734972677595628),
                        Pair.of(15, 0.00016780821917808218)
                );

        final List<Pair<Integer, Double>> dailyInterestRate =
                InterestFrequencyUtil.determineDailyInterestRatesByInterestFrequency(
                        EInterestFrequency.DAILY,
                        0.06125,
                        LocalDate.of(2024, 12, 15),
                        LocalDate.of(2025, 1, 15)
                );

        Assertions.assertEquals(expectedInterestRates, dailyInterestRate);
    }

    @Test
    public void calculateDailyBasedInterestRate_RegularYearToLeapYear() {
        final List<Pair<Integer, Double>> expectedInterestRates =
                List.of(
                        Pair.of(16, 0.00016780821917808218),
                        Pair.of(15, 0.00016734972677595628)
                );

        final List<Pair<Integer, Double>> dailyInterestRate =
                InterestFrequencyUtil.determineDailyInterestRatesByInterestFrequency(
                        EInterestFrequency.DAILY,
                        0.06125,
                        LocalDate.of(2023, 12, 15),
                        LocalDate.of(2024, 1, 15)
                );

        Assertions.assertEquals(expectedInterestRates, dailyInterestRate);
    }

    @Test
    public void calculateMonthlyBasedInterestRate_MonthMatch() {
        final List<Pair<Integer, Double>> expectedInterestRates =
                List.of(
                        Pair.of(1, 0.005104166666666667)
                );

        final List<Pair<Integer, Double>> dailyInterestRate =
                InterestFrequencyUtil.determineDailyInterestRatesByInterestFrequency(
                        EInterestFrequency.MONTHLY_12_BY_360,
                        0.06125,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 2, 1)
                );

        Assertions.assertEquals(expectedInterestRates, dailyInterestRate);
    }

    @Test
    public void calculateMonthlyBasedInterestRate_IntraMonth() {
        final List<Pair<Integer, Double>> expectedInterestRates =
                List.of(
                        Pair.of(0, 0.005104166666666667),
                        Pair.of(15, 0.0001646505376344086)
                );

        final List<Pair<Integer, Double>> dailyInterestRate =
                InterestFrequencyUtil.determineDailyInterestRatesByInterestFrequency(
                        EInterestFrequency.MONTHLY_12_BY_360,
                        0.06125,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 1, 16)
                );

        Assertions.assertEquals(expectedInterestRates, dailyInterestRate);
    }

    @Test
    public void calculateMonthlyBasedInterestRate_InterMonth() {
        final List<Pair<Integer, Double>> expectedInterestRates =
                List.of(
                        Pair.of(2, 0.005104166666666667),
                        Pair.of(15, 0.0001646505376344086)
                );

        final List<Pair<Integer, Double>> dailyInterestRate =
                InterestFrequencyUtil.determineDailyInterestRatesByInterestFrequency(
                        EInterestFrequency.MONTHLY_12_BY_360,
                        0.06125,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 3, 16)
                );

        Assertions.assertEquals(expectedInterestRates, dailyInterestRate);
    }

    @Test
    public void calculateWeeklyBasedInterestRate() {
        final List<Pair<Integer, Double>> expectedInterestRates =
                List.of(
                        Pair.of(74, 0.00016826923076923076)
                );

        final List<Pair<Integer, Double>> dailyInterestRate =
                InterestFrequencyUtil.determineDailyInterestRatesByInterestFrequency(
                        EInterestFrequency.WEEKLY_52_BY_364,
                        0.06125,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 3, 16)
                );

        Assertions.assertEquals(expectedInterestRates, dailyInterestRate);
    }

    @Test
    public void calculateInterestAdditionForPeriod() {
        final Double interestAddition =
                InterestFrequencyUtil.calculateInterestAdditionForPeriod(
                        EInterestFrequency.MONTHLY_12_BY_360,
                        0.06125,
                        775000.0,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 3, 16)
                );
        Assertions.assertEquals(9825.52, interestAddition);
    }
}
