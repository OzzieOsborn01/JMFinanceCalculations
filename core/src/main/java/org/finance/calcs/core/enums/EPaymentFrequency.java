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

    public double paymentRateConverter(final double paymentRate) {
        return PaymentFrequencyUtil.paymentRateConverter(paymentRate, this);
    }

    public int regularPaymentsPerYear() {
        return PaymentFrequencyUtil.regularPaymentsPerYear(this);
    }

    public EInterestFrequency interestFrequency() {
        return PaymentFrequencyUtil.regularInterestFrequency(this);
    }

    public LocalDate getNextDate(final LocalDate localDate) {
        if (Objects.isNull(periodUnit)) {
            // Not Implemented logic
            throw new NotImplementedException("Not Implemented");
        }

        return localDate.plus(periodDuration, periodUnit);
    }
}
