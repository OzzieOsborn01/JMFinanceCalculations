package org.finance.calcs.core.enums;

import org.apache.commons.lang3.tuple.Pair;
import org.finance.calcs.core.model.components.interest.InterestRatePair;
import org.finance.calcs.core.model.components.interest.InterestRatePairList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class EInterestFrequencyTest {

    @Test
    public void getDailyInterestRates() {
        Assertions.assertEquals(
                new InterestRatePairList(31, 0.00016780821917808218),
                EInterestFrequency.DAILY.getDailyInterestRates(
                        0.06125,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 2, 1)
                )
        );
        Assertions.assertEquals(
                new InterestRatePairList(
                        new InterestRatePair(0, 0.005104166666666667),
                        new InterestRatePair(15, 0.0001646505376344086)),
                EInterestFrequency.MONTHLY_12_BY_360.getDailyInterestRates(
                        0.06125,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 1, 16)
                )
        );
        Assertions.assertEquals(
                new InterestRatePairList(74, 0.00016826923076923076),
                EInterestFrequency.WEEKLY_52_BY_364.getDailyInterestRates(
                        0.06125,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 3, 16)
                )
        );
    }

    @Test
    public void calculateInterestAdditionForPeriod() {
        Assertions.assertEquals(
                9825.52,
                EInterestFrequency.MONTHLY_12_BY_360.calculateInterestAdditionForPeriod(
                        0.06125,
                        775000.0,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 3, 16)
                )
        );
    }
}
