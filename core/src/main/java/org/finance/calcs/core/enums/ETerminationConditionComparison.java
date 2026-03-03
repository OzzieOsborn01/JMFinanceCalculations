package org.finance.calcs.core.enums;

/**
 * Enum that represents a terminal condition criteria. Used in conjunction with
 * {@link org.finance.calcs.core.model.metadata.ObligationTerminationStrategy ObligationTerminationStrategy}. The
 * conditions are:
 * <ul>
 *     <li>EQUAL_TO</li>
 *     <li>NOT_EQUAL_TO</li>
 *     <li>GREATER_THAN</li>
 *     <li>GREATER_THAN_OR_EQUAL_TO</li>
 *     <li>LESS_THAN</li>
 *     <li>LESS_THAN_OR_EQUAL_TO</li>
 * </ul>
 */
public enum ETerminationConditionComparison {
    EQUAL_TO,
    NOT_EQUAL_TO,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL_TO,
    LESS_THAN,
    LESS_THAN_OR_EQUAL_TO;
}
