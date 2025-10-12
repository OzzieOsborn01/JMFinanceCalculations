package org.finance.calcs.processing.common.processor;

import org.finance.calcs.core.model.obligationBase.FinancialObligationComponent;
import org.finance.calcs.processing.common.model.FOCProcessorContext;

import java.time.LocalDate;

public interface FOCProcessor<Context extends FOCProcessorContext, FOCComp extends FinancialObligationComponent> {
    void processPayment(Context processorContext, FOCComp focComponent, LocalDate processDate);

    void processEndOfPeriod(Context processorContext, FOCComp focComponent, LocalDate processDate);

    default void processTransaction(Context processorContext, FOCComp focComponent, LocalDate processDate) {

    }
}
