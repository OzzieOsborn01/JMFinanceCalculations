package org.finance.calcs.core.util;

import org.apache.commons.lang3.NotImplementedException;
import org.finance.calcs.core.enums.EInterestFrequency;
import org.finance.calcs.core.enums.EPaymentFrequency;

import static org.finance.calcs.core.constants.DateRelatedConstants.BI_WEEKLY_PERIODS_PER_MONTH;
import static org.finance.calcs.core.constants.DateRelatedConstants.MONTHS_PER_YEAR;
import static org.finance.calcs.core.constants.DateRelatedConstants.WEEKS_PER_YEAR;

public final class PaymentFrequencyUtil {

    public static double paymentRateConverter(final double paymentRate, final EPaymentFrequency paymentFrequency) {
        return switch (paymentFrequency) {
            case MONTHLY -> paymentRate;
            case BI_WEEKLY -> paymentRate / BI_WEEKLY_PERIODS_PER_MONTH;
            default -> throw new NotImplementedException("Not Implemented");
        };
    }

    public static int regularPaymentsPerYear(final EPaymentFrequency paymentFrequency) {
        return switch (paymentFrequency) {
            case MONTHLY -> MONTHS_PER_YEAR;
            case BI_WEEKLY -> WEEKS_PER_YEAR / 2;
            default -> throw new NotImplementedException("Not Implemented");
        };
    }

    public static EInterestFrequency regularInterestFrequency(final EPaymentFrequency paymentFrequency) {
        return switch (paymentFrequency) {
            case MONTHLY -> EInterestFrequency.MONTHLY_12_BY_360;
            case BI_WEEKLY -> EInterestFrequency.WEEKLY_52_BY_364;
            default -> throw new NotImplementedException("Not Implemented");
        };
    }
}
