package org.finance.calcs.core.enums;

public enum ESubscriptionDurationType {
    NO_COMMITMENT, // Cancel at any time
    EXTERNAL_CONDITIONAL, //Dependent upon another external condition such as home ownership for HOA fees
    FIXED_BALANCE,
    FIXED_DURATION;
}
