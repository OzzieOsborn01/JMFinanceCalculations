package org.finance.calcs.core.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class HomeDetails {
    private final double homePurchasedValue;

    @Builder.Default
    private final double homeAppraisedValue = 0.0;

    @Builder.Default
    private final double homeTaxableValue = 0.0;
}
