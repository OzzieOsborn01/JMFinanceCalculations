package org.finance.calcs.core.percent;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import org.finance.calcs.core.dataSerializing.PercentSerializer;
import org.finance.calcs.core.util.RoundingUtil;

@EqualsAndHashCode
@JsonSerialize(using = PercentSerializer.class)
public class Percent {

    private final Double percentValue;

    public Percent(final Double value, final Integer digits) {
        percentValue = RoundingUtil.roundValue(value, digits);
    }

    public Percent(final Double value) {
        percentValue = RoundingUtil.roundValue(value);
    }

    public static Percent fromPercent(final Double percent, final Integer digits) {
        return new Percent(percent / 100.0, digits + 2);
    }

    public static Percent fromPercent(final Double percent) {
        return new Percent(percent / 100.0);
    }

    public static Percent fromDecimal(final Double percent, final Integer digits) {
        return new Percent(percent, digits);
    }

    public static Percent fromDecimal(final Double percent) {
        return new Percent(percent);
    }

    public Double asDouble() {
        return percentValue;
    }

    @Override
    public String toString() {
        return percentValue * 100.0 + "%";
    }
}
