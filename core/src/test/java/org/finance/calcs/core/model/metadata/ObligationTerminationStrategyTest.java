package org.finance.calcs.core.model.metadata;

import org.finance.calcs.core.enums.ETerminationConditionComparison;
import org.finance.calcs.core.enums.ETerminationConditionFactor;
import org.finance.calcs.core.percent.Percent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

public class ObligationTerminationStrategyTest {

    private static Stream<Arguments> obligationTerminationStrategyArguments() {
        return Stream.of(
                Arguments.argumentSet("Compare (Greater Than or Equal To) is false", false, Percent.ONE_HUNDRED_PERCENT, Percent.ZERO_PERCENT, ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO),
                Arguments.argumentSet("Compare (Greater Than or Equal To) is false", true, Percent.ZERO_PERCENT, Percent.ONE_HUNDRED_PERCENT, ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO),
                Arguments.argumentSet("Compare (Greater Than or Equal To) is true", true, Percent.ONE_HUNDRED_PERCENT, Percent.ONE_HUNDRED_PERCENT, ETerminationConditionComparison.GREATER_THAN_OR_EQUAL_TO),
                Arguments.argumentSet("Compare (Greater Than) is false", false, LocalDate.of(2025, 9, 1), LocalDate.of(2025, 8, 1), ETerminationConditionComparison.GREATER_THAN),
                Arguments.argumentSet("Compare (Greater Than) is true", true, LocalDate.of(2025, 8, 1), LocalDate.of(2025, 9, 1), ETerminationConditionComparison.GREATER_THAN),
                Arguments.argumentSet("Compare (Greater Than) is false", false, LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 1), ETerminationConditionComparison.GREATER_THAN),
                Arguments.argumentSet("Compare (Equal To) is false", false, 5, 6, ETerminationConditionComparison.EQUAL_TO),
                Arguments.argumentSet("Compare (Equal To) is false", false, 6, 5, ETerminationConditionComparison.EQUAL_TO),
                Arguments.argumentSet("Compare (Equal To) is true", true, 5, 5, ETerminationConditionComparison.EQUAL_TO),
                Arguments.argumentSet("Compare (Not Equal To) is true", true, 5L, 6L, ETerminationConditionComparison.NOT_EQUAL_TO),
                Arguments.argumentSet("Compare (Not Equal To) is true", true, 6L, 5L, ETerminationConditionComparison.NOT_EQUAL_TO),
                Arguments.argumentSet("Compare (Not Equal To) is false", false, 5L, 5L, ETerminationConditionComparison.NOT_EQUAL_TO),
                Arguments.argumentSet("Compare (Less Than) is true", true, 6.0, 5.0, ETerminationConditionComparison.LESS_THAN),
                Arguments.argumentSet("Compare (Less Than) is false", false, 5.0, 6.0, ETerminationConditionComparison.LESS_THAN),
                Arguments.argumentSet("Compare (Less Than) is false", false, 5.0, 5.0, ETerminationConditionComparison.LESS_THAN),
                Arguments.argumentSet("Compare (Less Than or Equal To) is true", true, 6.0, 5.0, ETerminationConditionComparison.LESS_THAN_OR_EQUAL_TO),
                Arguments.argumentSet("Compare (Less Than or Equal To) is false", false, 5.0, 6.0, ETerminationConditionComparison.LESS_THAN_OR_EQUAL_TO),
                Arguments.argumentSet("Compare (Less Than or Equal To) is true", true, 5.0, 5.0, ETerminationConditionComparison.LESS_THAN_OR_EQUAL_TO)
        );
    }

    @ParameterizedTest
    @MethodSource("obligationTerminationStrategyArguments")
    public <T extends Comparable<T>> void obligationTerminationStrategyArguments(
            boolean expectedResult,
            T value1,
            T value2,
            ETerminationConditionComparison comparisonMethod
    ) {
        ObligationTerminationStrategy<T> strategyToTest = ObligationTerminationStrategy.<T>builder()
                .comparisonMethod(comparisonMethod)
                .terminationConditionFactor(ETerminationConditionFactor.OBLIGATION_COMPLETED)
                .terminationConditionValue(value1)
                .build();

        Assertions.assertEquals(expectedResult, strategyToTest.compareToValue(value2));
        Assertions.assertEquals(false, strategyToTest.getTerminationConditionMatched());

        strategyToTest.compareToValueAndApplyMatched(value2);
        Assertions.assertEquals(expectedResult, strategyToTest.getTerminationConditionMatched());
    }
}
