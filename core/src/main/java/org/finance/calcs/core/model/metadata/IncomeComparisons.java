package org.finance.calcs.core.model.metadata;

import lombok.*;
import org.finance.calcs.core.enums.EIncomeComparisonType;
import org.finance.calcs.core.percent.Percent;
import org.finance.calcs.core.util.RoundingUtil;

import java.util.function.Function;

@AllArgsConstructor
@Builder
@Data
public class IncomeComparisons {
    private EIncomeComparisonType incomeComparisonType;
    private Double annualIncomeAmountForType;

    @Builder.Default
    @Setter(value=AccessLevel.PRIVATE)
    Percent nonMortgageDebtToIncomeRatio = Percent.fromDecimal(0.0);
    @Builder.Default
    @Setter(value=AccessLevel.PRIVATE)
    Percent mortgageDebtToIncomeRatio = Percent.fromDecimal(0.0);
    @Builder.Default
    @Setter(value=AccessLevel.PRIVATE)
    Percent totalDebtToIncomeRatio = Percent.fromDecimal(0.0);

    public IncomeComparisons(
            EIncomeComparisonType incomeType,
            Double annualIncome,
            Double nonMortgagePayment,
            Double mortgagePayment
    ) {
        incomeComparisonType = incomeType;
        annualIncomeAmountForType = annualIncome;
        updateDebtPaymentToIncomeRatio(nonMortgagePayment, mortgagePayment);
    }

    public void updateDebtPaymentToIncomeRatio(Double nonMortgagePayment, Double mortgagePayment) {
        final Function<Double, Percent> debtRatioCalc = debtPayment -> {
            final Double d = RoundingUtil.roundValue((debtPayment * 12.0) / annualIncomeAmountForType, 2);
            return Percent.fromDecimal(d);
        };
        final Double nonMortgageDebt = RoundingUtil.roundValue(nonMortgagePayment, 2);
        final Double mortgageDebt = RoundingUtil.roundValue(mortgagePayment, 2);
        final Double totalDebt = RoundingUtil.roundValue(nonMortgageDebt + mortgageDebt, 2);

        setNonMortgageDebtToIncomeRatio(debtRatioCalc.apply(nonMortgageDebt));
        setMortgageDebtToIncomeRatio(debtRatioCalc.apply(mortgageDebt));
        setTotalDebtToIncomeRatio(debtRatioCalc.apply(totalDebt));
    }
}
