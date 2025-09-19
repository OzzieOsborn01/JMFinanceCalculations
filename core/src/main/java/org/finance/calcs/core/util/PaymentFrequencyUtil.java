package org.finance.calcs.core.util;

import org.apache.commons.lang3.NotImplementedException;
import org.finance.calcs.core.enums.EInterestFrequency;
import org.finance.calcs.core.enums.EPaymentFrequency;

import static org.finance.calcs.core.constants.DateRelatedConstants.*;

public final class PaymentFrequencyUtil {

    public static double paymentRateConverter(final double paymentRate, final EPaymentFrequency paymentFrequency) {
        return switch (paymentFrequency) {
            case MONTHLY -> paymentRate;
            case BI_WEEKLY -> paymentRate / BI_WEEKLY_PERIODS_PER_MONTH;
            case SEMI_ANNUALLY -> paymentRate * MONTHS_PER_YEAR / 2;
            case YEARLY -> paymentRate * MONTHS_PER_YEAR;
            default -> throw new NotImplementedException("Not Implemented");
        };
    }

    public static double paymentRateConverter(
            final double paymentRate,
            final EPaymentFrequency originalFrequency,
            final EPaymentFrequency paymentFrequency
    ) {
        if (paymentFrequency == EPaymentFrequency.NOT_IMPLEMENTED || paymentFrequency == EPaymentFrequency.NOT_IMPLEMENTED) {
            throw new NotImplementedException("Not Implemented");
        } else if (originalFrequency == paymentFrequency) {
            return paymentRate;
        }

        final String frequencyUnitConversion = originalFrequency.getPeriodUnit() + "_" + paymentFrequency.getPeriodUnit();
        final Double originalDuration = (double) originalFrequency.getPeriodDuration();
        final Double newDuration = (double) paymentFrequency.getPeriodDuration();
        final Double unitMatchDuration = (newDuration / originalDuration);

        final Double convertedRate = paymentRate * switch (frequencyUnitConversion) {
            case "Weeks_Weeks" -> unitMatchDuration;
            case "Weeks_Months" -> (WEEKS_PER_MONTH * newDuration) / originalDuration;
            case "Weeks_Years" -> (WEEKS_PER_YEAR * newDuration) / originalDuration;
            case "Months_Weeks" -> newDuration / (originalDuration * WEEKS_PER_MONTH);
            case "Months_Months" -> unitMatchDuration;
            case "Months_Years" -> (MONTHS_PER_YEAR * newDuration) / originalDuration;
            case "Years_Weeks" -> newDuration / (originalDuration * WEEKS_PER_YEAR);
            case "Years_Months" -> newDuration / (originalDuration * MONTHS_PER_YEAR);
            case "Years_Years" -> unitMatchDuration;
            default -> throw new NotImplementedException(frequencyUnitConversion + " combination not implemented");
        };

        return RoundingUtil.roundValue(convertedRate);
    }

    public static int regularPaymentsPerYear(final EPaymentFrequency paymentFrequency) {
        return switch (paymentFrequency) {
            case MONTHLY -> MONTHS_PER_YEAR;
            case BI_WEEKLY -> WEEKS_PER_YEAR / 2;
            case SEMI_ANNUALLY -> MONTHS_PER_YEAR / 2;
            case YEARLY -> 1;
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
