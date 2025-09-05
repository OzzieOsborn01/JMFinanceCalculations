package org.finance.calcs.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.finance.calcs.core.util.DateAdjustment;
import org.finance.calcs.core.util.PaymentFrequencyUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@AllArgsConstructor
public enum EPaymentFrequency {
    BI_WEEKLY(new DateAdjustment(ChronoUnit.WEEKS, 2), 14),
    MONTHLY(new DateAdjustment(ChronoUnit.MONTHS, 1), 28);

    final DateAdjustment defaultDateAdjustment;

    @Getter
    final Integer payableDaysInPeriod;

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
        TemporalUnit unit = ChronoUnit.DAYS;
        int amountToAdd = 0;

        switch (this) {
            case BI_WEEKLY -> {
                unit = ChronoUnit.WEEKS;
                amountToAdd = 2;
            }
            case MONTHLY -> {
                unit = ChronoUnit.MONTHS;
                amountToAdd = 1;
            }
        }

        return localDate.plus(amountToAdd, unit);
    }

    public LocalDate plusDefaultDateAdjustment(final LocalDate date) {
        final DateAdjustment dateAdj = defaultDateAdjustment;
        return date.plus(dateAdj.getAdjustmentAmount(), dateAdj.getTemporalUnitAdjustment());
    }

    public LocalDate minusDefaultDateAdjustment(final LocalDate date) {
        final DateAdjustment dateAdj = defaultDateAdjustment;
        return date.minus(dateAdj.getAdjustmentAmount(), dateAdj.getTemporalUnitAdjustment());
    }
}
