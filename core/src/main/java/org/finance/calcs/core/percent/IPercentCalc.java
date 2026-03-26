package org.finance.calcs.core.percent;

/**
 * Interface for a percentage calculation. This is an object that has a provided percentage and possible other values,
 * and then the user can call calculatePercentValueOf to get the expected output double value. This is used to allow
 * different percent calculations methods including flat calculation or calculation up to a maximum point.
 */
public interface IPercentCalc {
    /**
     * Return the provided percent of with the provided percent calculation and the provided base value
     * @param baseValue
     * @return
     */
    Double calculatePercentValueOf(final Double baseValue);
}
