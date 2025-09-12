package org.finance.calcs.processing.common.processor;

import org.finance.calcs.core.model.obligationBase.FinancialObligation;
import org.finance.calcs.processing.common.model.FOProcessorContext;

import java.time.LocalDate;

public interface FOProcessor<Context /*extends FOProcessorContext*/, FOComp extends FinancialObligation> {
    default void process(Context processorContext, FOComp foComponent) {
        processToCompletion(processorContext, foComponent);
    }

    void processToCompletion(Context processorContext, FOComp foComponent);

    void processNextPeriod(Context processorContext, FOComp foComponent);

    void processPeriodRange(Context processorContext, FOComp foComponent, LocalDate endProcessDate);

    void processNumberOfPeriods(Context processorContext, FOComp foComponent, int periodsToProcess);
}
