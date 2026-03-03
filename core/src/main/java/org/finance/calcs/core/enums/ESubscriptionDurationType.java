package org.finance.calcs.core.enums;

/**
 * Enum that represents the subscription duration type - how long the subscription should last. Current values are:
 * <ul>
 *     <li>NO_COMMITMENT - Cancel at any time</li>
 *     <li>EXTERNAL_CONDITIONAL - Dependent upon another external condition</li>
 *     <li>FIXED_BALANCE - The subscription lasts for a specific balance and then ends automatically</li>
 *     <li>FIXED_DURATION - The subscription lasts for a specific duration and then ends automatically</li>
 * </ul>
 */
public enum ESubscriptionDurationType {
    /**
     * Cancel at any time
     */
    NO_COMMITMENT,
    /**
     * Dependent upon another external condition such as home ownership for HOA fees
     */
    EXTERNAL_CONDITIONAL,
    /**
     * The subscription lasts for a specific balance and then ends automatically
     */
    FIXED_BALANCE,
    /**
     * The subscription lasts for a specific duration and then ends automatically
     */
    FIXED_DURATION;
}
