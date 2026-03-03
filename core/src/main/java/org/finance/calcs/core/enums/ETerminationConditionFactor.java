package org.finance.calcs.core.enums;

/**
 * Enum representing a termination condition factor. This represents what conditions should be used to terminate an
 * obligation. The conditions are:
 * <ul>
 *     <li>OBLIGATION_COMPLETED - the financial balance obligation is completed</li>
 *     <li>DURATION_COMPLETED - a duration obligation is completed</li>
 *     <li>OBLIGATION_OR_DURATION_COMPLETED - either the financial balance or duration obligation is completed</li>
 *     <li>NOT_ENDING - the obligation does not end</li>
 * </ul>
 */
public enum ETerminationConditionFactor {
    OBLIGATION_COMPLETED,
    DURATION_COMPLETED,
    OBLIGATION_OR_DURATION_COMPLETED,
    NOT_ENDING;
}
