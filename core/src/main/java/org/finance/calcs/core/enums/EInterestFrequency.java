package org.finance.calcs.core.enums;

import org.apache.commons.lang3.tuple.Pair;
import org.finance.calcs.core.util.InterestFrequencyUtil;

import java.time.LocalDate;
import java.util.List;

public enum EInterestFrequency {
    MONTHLY_12_BY_360,
    WEEKLY_52_BY_364,
    DAILY;

    public List<Pair<Integer, Double>> getDailyInterestRates(
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
