package org.finance.calcs.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.NotImplementedException;
import org.finance.calcs.core.util.DateAdjustment;
import org.finance.calcs.core.util.PaymentFrequencyUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

/**
 * Enum to represent payment frequency. This is composed of a {@link ChronoUnit} and a period duration (Int).
 * This class can be used to evaluate the next payment date, the regular payments per year, and get regular interest
 * frequency. The current supported payment frequencies are:
 * <ul>
 *     <li>Bi-weekly (every 2 weeks)</li>
 *     <li>Monthly</li>
 *     <li>Semi-annually (every 6 months)</li>
 *     <li>Yearly</li>
 * </ul>
 */
@AllArgsConstructor
@Getter
public enum EPaymentFrequency {
    BI_WEEKLY(ChronoUnit.WEEKS, 2),
    MONTHLY(ChronoUnit.MONTHS, 1),
    SEMI_ANNUALLY(ChronoUnit.MONTHS, 6),
    YEARLY(ChronoUnit.YEARS, 1),
    NOT_IMPLEMENTED(null, null);

    final ChronoUnit periodUnit;
    final Integer periodDuration;

    /**
     * Convert the provide payment rate based on the payment frequency
     * @param paymentRate monthly payment amount
     * @return payment amount for the given frequency
     */
    public double paymentRateConverter(final double paymentRate) {
        return PaymentFrequencyUtil.paymentRateConverter(paymentRate, this);
    }

    /**
     * Return the number of regular payments per year
     * @return number of regular payments per year
     */
    public int regularPaymentsPerYear() {
        return PaymentFrequencyUtil.regularPaymentsPerYear(this);
    }

    /**
     * Provides the matching {@link EInterestFrequency} frequency based on standard
     * @return matching {@link EInterestFrequency}
     */
    public EInterestFrequency interestFrequency() {
        return PaymentFrequencyUtil.regularInterestFrequency(this);
    }

    /**
     * Return the next period date provided a given date. General guidelines is to use this function to determine the
     * next start date but can be any date.
     * @param localDate date to get
     * @return next date
     */
    public LocalDate getNextDate(final LocalDate localDate) {
        if (Objects.isNull(periodUnit)) {
            // Not Implemented logic
            throw new NotImplementedException("Not Implemented");
        }

        return localDate.plus(periodDuration, periodUnit);
    }
}
