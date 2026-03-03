package org.finance.calcs.core.model.obligationBase;

/**
 * FinancialObligation (FO) represents a generic financial obligation. These obligations represent
 * the totality of a financial obligation such as a loan or mortgage. Classes that are FOs are labeled as
 * &ltObligation>FO (e.g. MortgageFO)
 * <p/>
 * These are composed of {@link FinancialObligationComponent} (FOC) that represent some or all of the obligation such as
 * a loan, insurance, regular fees, etc. It is up to the FO how to process the FOC sub components
 * <p/>
 * Using the example of a mortgage: The mortgage is a {@link FinancialObligation} and the loan, insurance, HOA fees,
 * etc. are all examples of {@link FinancialObligationComponent}
 */
public interface FinancialObligation {
}
