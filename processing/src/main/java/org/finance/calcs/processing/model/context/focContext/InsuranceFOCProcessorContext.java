package org.finance.calcs.processing.model.context.focContext;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import org.finance.calcs.processing.common.model.FOCProcessorContext;
import org.jspecify.annotations.Nullable;

@Data
@Builder
public class InsuranceFOCProcessorContext implements FOCProcessorContext {
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
    private Double periodBalance = 0.0;

    @Nullable
    @Builder.Default
    @Getter()
    private boolean periodReset = false;
}
