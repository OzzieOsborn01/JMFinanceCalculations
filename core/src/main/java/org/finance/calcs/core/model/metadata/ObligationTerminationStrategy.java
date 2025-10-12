package org.finance.calcs.core.model.metadata;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.core.enums.ETerminationConditionComparison;
import org.finance.calcs.core.enums.ETerminationConditionFactor;

@Data
@Builder
public class ObligationTerminationStrategy <T extends Comparable<T>> {
    @NonNull
    private ETerminationConditionFactor terminationConditionFactor;

    @NonNull
    private T terminationConditionValue;

    @NonNull
    private ETerminationConditionComparison comparisonMethod;

    private String terminationConditionDescription;

    @Builder.Default
    private Boolean terminationConditionMatched = false;

    public Boolean compareToValue(T value) {
        final Integer compResult = value.compareTo(terminationConditionValue);
        return switch (comparisonMethod) {
            case EQUAL_TO -> compResult == 0;
            case NOT_EQUAL_TO -> compResult != 0;
            case LESS_THAN -> compResult < 0;
            case GREATER_THAN -> compResult > 0;
            case LESS_THAN_OR_EQUAL_TO -> compResult <= 0;
            case GREATER_THAN_OR_EQUAL_TO -> compResult >= 0;
            default -> throw new IllegalArgumentException("Invalid comparison method");
        };
    }

    public Boolean compareToValueAndApplyMatched(T value) {
        final Boolean compResult = compareToValue(value);
        if (compResult == true) {
            terminationConditionMatched = true;
        }
        return compResult;
    }
}
