package org.finance.calcs.processing.common.processor;

import org.finance.calcs.core.model.obligationBase.FinancialObligationComponent;
import org.finance.calcs.processing.common.model.FOCProcessorContext;

import java.time.LocalDate;

public interface Processor <Context extends FOCProcessorContext, FOCComp extends FinancialObligationComponent> {
    void process(Context processorContext, FOCComp focComponent, LocalDate processDate);
}
