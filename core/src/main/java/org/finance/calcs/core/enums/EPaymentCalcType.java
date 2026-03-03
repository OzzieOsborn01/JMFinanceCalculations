package org.finance.calcs.core.enums;

/**
 * Enum representing the Payment Calculation type. Current types are:
 * <ul>
 *     <li>FLAT_PAYMENT - a flat sum payment (e.g. $500)</li>
 *     <li>PERCENT_PAYMENT - a percent of another value (e.g 0.5%)</li>
 * </ul>
 */
public enum EPaymentCalcType {
    FLAT_PAYMENT,
    PERCENT_PAYMENT
}
