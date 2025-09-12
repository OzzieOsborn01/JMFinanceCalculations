package org.finance.calcs.processing.testing.constructs;

import lombok.*;
import org.finance.calcs.processing.model.obligationPeriod.MortgageFOPeriod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class MortgageFOProcessorTestContext {
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    List<MortgageFOPeriod> mortgagePeriodList = new ArrayList<>();

    @Builder.Default
    Double currentPayment = 0.0;

    @Builder.Default
    Double totalLoanContribution = 0.0;

    @Builder.Default
    Double totalInterestContribution = 0.0;

    @Builder.Default
    Double totalPayments = 0.0;

    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    LocalDate lastProcessedDate;

    @Builder.Default
    private Double holdingAccountBalance = 0.0;
}
