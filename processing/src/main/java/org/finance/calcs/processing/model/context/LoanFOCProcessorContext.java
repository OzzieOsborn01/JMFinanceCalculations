package org.finance.calcs.processing.model.context;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.processing.common.model.FOCProcessorContext;
import org.jspecify.annotations.Nullable;

@Data
@Builder
public class LoanFOCProcessorContext implements FOCProcessorContext {
    @NonNull
    private Double payment;

    @Nullable
    @Builder.Default
    private Double paymentUsed = 0.0;

    @Nullable
    @Builder.Default
    private Double paymentRemaining = 0.0;

    @Nullable
    @Builder.Default
    private Double principalBalance = 0.0;

    @Nullable
    @Builder.Default
    private Double principalDelta = 0.0;

    @Nullable
    @Builder.Default
    private Double interestDelta = 0.0;

    @Nullable
    @Builder.Default
    private Double interestRemaining = 0.0;
}
