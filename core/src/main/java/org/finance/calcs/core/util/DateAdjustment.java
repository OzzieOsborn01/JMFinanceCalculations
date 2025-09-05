package org.finance.calcs.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.temporal.TemporalUnit;

@Data
@AllArgsConstructor
public class DateAdjustment {
    TemporalUnit temporalUnitAdjustment;
    Integer adjustmentAmount;
}
