package org.finance.calcs.core.model.components.mortageInsurance;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.finance.calcs.core.enums.EMortgageInsuranceType;
import org.finance.calcs.core.enums.EPaymentFrequency;
import org.finance.calcs.core.enums.ETerminationConditionFactor;
import org.finance.calcs.core.model.metadata.ObligationTerminationStrategy;
import org.finance.calcs.core.model.calculations.PaymentCalculation;
import org.finance.calcs.core.percent.Percent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * This is the initialization detail and terms of a Mortgage Insurance (MIP or PMI). This class is used to provide
 * initial values to {@link MortgageInsuranceFOC} and establish some default values. Best practice is to provide the
 * terms to the {@link org.finance.calcs.core.factories.MortgageInsuranceFOCFactory MortgageInsuranceFOCFactory} to
 * build the FOC component.<p>
 * Term Values:
 * <ul>
 *     <li>Mortgage Insurance Type (required - {@link EMortgageInsuranceType})</li>
 *     <li>House Value (required - value of the house)</li>
 *     <li>Loan Type (required - value of the loan)</li>
 *     <li>Termination condition factor (required - what will cause the end of the insurance
 *          {@link ETerminationConditionFactor})</li>
 *     <li>Payment Frequency (optional - defaults to monthly)</li>
 *     <li>Payment Calculation (required - how much is the regular payment {@link PaymentCalculation})</li>
 *     <li>Upfront Premium (optional - defaults to 0.0)</li>
 *     <li>Loan Start Date (optional - defaults to now)</li>
 *     <li>Soft Balance Term Condition (optional unless balance term condition is needed like for traditional mortgage
 *     - {@link ObligationTerminationStrategy})</li>
 *     <li>Hard Balance Term Condition (optional unless balance term condition is needed like for traditional mortgage
 *     - {@link ObligationTerminationStrategy})</li>
 *     <li>Soft Duration Term Condition (optional unless soft duration term condition is needed like for FHA loan
 *     - {@link ObligationTerminationStrategy})</li>
 *     <li>Hard Duration Term Condition (optional unless hard duration term condition is needed like for FHA loan
 *     - {@link ObligationTerminationStrategy})</li>
 *     <li>durationTermUnits (optional - ChronoUnit defaults to years - how long the duration termination)</li>
 * </ul>
 * @see MortgageInsuranceFOC MortgageInsuranceFOC
 * @see org.finance.calcs.core.factories.MortgageInsuranceFOCFactory MortgageInsuranceFOCFactory
 */
@Data
@Builder(toBuilder = true)
public class MortgageInsuranceTerms {
    @NonNull
    private EMortgageInsuranceType mortgageInsuranceType;

    @NonNull
    private Double houseValue;

    @NonNull
    private Double loanValue;

    @NonNull
    private ETerminationConditionFactor terminationConditionFactor;

    @Builder.Default
    private EPaymentFrequency paymentFrequency = EPaymentFrequency.MONTHLY;

    private PaymentCalculation paymentCalculation;

    @Builder.Default
    private Double upfrontPremium = 0.0;

    @Builder.Default
    private LocalDate startDate = LocalDate.now();

    @Builder.Default
    private Boolean overridden = false;

    // Handle overriding values

    private ObligationTerminationStrategy<Percent> softBalanceTermCondition;
    private ObligationTerminationStrategy<Long> softDurationTermCondition;
    private ObligationTerminationStrategy<Percent> hardBalanceTermCondition;
    private ObligationTerminationStrategy<Long> hardDurationTermCondition;

    @Builder.Default
    private ChronoUnit durationTermUnits = ChronoUnit.YEARS;
}
