package org.finance.calcs.processing.model.context;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.processing.common.model.FOCProcessorContext;
import org.jspecify.annotations.Nullable;

@Data
@Builder
public class MortgageInsuranceFOCProcessorContext implements FOCProcessorContext {
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

    @NonNull
    private Double houseValue;

    @NonNull
    private Double loanValue;

    @Builder.Default
    private Boolean skipped = false;

    @Builder.Default
    private Boolean useSoftTerminationStrategy = false;
}
