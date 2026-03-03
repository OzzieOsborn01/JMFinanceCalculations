package org.finance.calcs.core.util;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.finance.calcs.core.enums.EInterestFrequency;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.model.components.interest.InterestRatePair;
import org.finance.calcs.core.model.components.interest.InterestRatePairList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class InterestFrequencyUtilTest {
    @Test
    public void calculateDailyBasedInterestRate() {
        final InterestRatePairList expectedInterestRates =
                new InterestRatePairList(
                        new InterestRatePair(31, 0.00016780821917808218)
                );

        final InterestRatePairList dailyInterestRate =
                InterestFrequencyUtil.determineDailyInterestRatesByInterestFrequency(
                        EInterestFrequency.DAILY,
                        0.06125,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 2, 1)
                );

        Assertions.assertEquals(expectedInterestRates, dailyInterestRate);
    }

    @Test
    public void calculateDailyBasedInterestRate_NotImplemented() {
        Assertions.assertThrows(NotImplementedException.class, () ->
                InterestFrequencyUtil.determineDailyInterestRatesByInterestFrequency(
                    EInterestFrequency.NOT_IMPLEMENTED,
                    0.06125,
                    LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 2, 1)
        ));
    }

    @Test
    public void calculateDailyBasedInterestRate_LeapYear() {
        final InterestRatePairList expectedInterestRates =
                new InterestRatePairList(
                        new InterestRatePair(31, 0.00016734972677595628)
                );

        final InterestRatePairList dailyInterestRate =
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
        final InterestRatePairList expectedInterestRates =
                new InterestRatePairList(
                        new InterestRatePair(16, 0.00016734972677595628),
                        new InterestRatePair(15, 0.00016780821917808218)
                );

        final InterestRatePairList dailyInterestRate =
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
        final InterestRatePairList expectedInterestRates =
                new InterestRatePairList(
                        new InterestRatePair(16, 0.00016780821917808218),
                        new InterestRatePair(15, 0.00016734972677595628)
                );

        final InterestRatePairList dailyInterestRate =
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
        final InterestRatePairList expectedInterestRates =
                new InterestRatePairList(
                       1, 0.005104166666666667
                );

        final InterestRatePairList dailyInterestRate =
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
        final InterestRatePairList expectedInterestRates =
                new InterestRatePairList(
                        new InterestRatePair(0, 0.005104166666666667),
                        new InterestRatePair(15, 0.0001646505376344086)
                );

        final InterestRatePairList dailyInterestRate =
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
        final InterestRatePairList expectedInterestRates =
                new InterestRatePairList(
                        new InterestRatePair(2, 0.005104166666666667),
                        new InterestRatePair(15, 0.0001646505376344086)
                );

        final InterestRatePairList dailyInterestRate =
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
        final InterestRatePairList expectedInterestRates =
                new InterestRatePairList(
                        74, 0.00016826923076923076
                );

        final InterestRatePairList dailyInterestRate =
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
