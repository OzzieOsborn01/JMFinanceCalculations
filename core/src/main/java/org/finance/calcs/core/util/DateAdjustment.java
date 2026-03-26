package org.finance.calcs.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.temporal.TemporalUnit;

/**
 * Date adjustment data class that contains a temporal unit (date or time) and an adjustment amount (integer)
 */
@Data
@AllArgsConstructor
public class DateAdjustment {
    TemporalUnit temporalUnitAdjustment;
    Integer adjustmentAmount;
}
