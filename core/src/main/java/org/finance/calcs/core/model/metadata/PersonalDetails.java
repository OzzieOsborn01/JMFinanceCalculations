package org.finance.calcs.core.model.metadata;

import lombok.*;
import org.finance.calcs.core.enums.EIncomeComparisonType;
import org.finance.calcs.core.percent.Percent;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder(builderClassName = "PersonalDetailsTestingBuilder", builderMethodName = "unitTestBuilder")
@Data
public class PersonalDetails {
    @Setter(AccessLevel.NONE)
    private Double grossIncome;

    @Setter(AccessLevel.NONE)
    private Double netIncome;

    @Setter(AccessLevel.NONE)
    private Map<String, Debt> debts;

    @Setter(AccessLevel.NONE)
    private Double mortgagePayment;

    @Setter(AccessLevel.NONE)
    private IncomeComparisons grossIncomeComp;

    @Setter(AccessLevel.NONE)
    private IncomeComparisons netIncomeComp;

    private PersonalDetails(
            final Double grossIncome,
            final Double netIncome,
            final Map<String, Debt> debts,
            final Double mortgagePayment) {
        this.grossIncome = grossIncome;
        this.netIncome = netIncome;
        this.debts = debts;
        this.mortgagePayment = mortgagePayment;

        final Double debtPaymentAggregation = debtPaymentAggregation();
        grossIncomeComp = new IncomeComparisons(
                EIncomeComparisonType.GROSS,
                grossIncome,
                debtPaymentAggregation,
                mortgagePayment);
        netIncomeComp =
                new IncomeComparisons(
                        EIncomeComparisonType.NET,
                        netIncome,
                        debtPaymentAggregation,
                        mortgagePayment);
    }

    @Builder
    private PersonalDetails(
            final Double grossIncome,
            final Double netIncome,
            final List<Debt> debts) {
        this(
                grossIncome,
                netIncome,
                debts.stream().collect(Collectors.toMap(Debt::getDebtor, Function.identity())),
                0.0);
    }

    @Builder(builderClassName = "PersonalDetailsWithMortgageBuilder", builderMethodName = "withMortgageBuilder")
    private PersonalDetails(
            final Double grossIncome,
            final Double netIncome,
            final List<Debt> debts,
            final Double mortgagePayment) {
        this(
                grossIncome,
                netIncome,
                debts.stream().collect(Collectors.toMap(Debt::getDebtor, Function.identity())),
                mortgagePayment);
    }

    public static PersonalDetailsBuilder builder() {
        return new CustomPersonalDetailsBuilder();
    }

    public static PersonalDetailsWithMortgageBuilder withMortgageBuilder() {
        return new CustomPersonalDetailsWithMortgageBuilder();
    }

    private static class CustomPersonalDetailsBuilder extends PersonalDetailsBuilder {
        @Override
        public PersonalDetails build() {
            return new PersonalDetails(
                    super.grossIncome,
                    super.netIncome,
                    super.debts
            );
        }
    }

    private static class CustomPersonalDetailsWithMortgageBuilder extends PersonalDetailsWithMortgageBuilder {
        @Override
        public PersonalDetails build() {
            return new PersonalDetails(
                    super.grossIncome,
                    super.netIncome,
                    super.debts,
                    super.mortgagePayment
            );
        }
    }

    private IncomeComparisons getIncomeTypeComp(final EIncomeComparisonType incomeType) {
        switch (incomeType) {
            case GROSS: return grossIncomeComp;
            case NET: return netIncomeComp;
            default: throw new RuntimeException("Income Comp type not supported");
        }
    }

    public Percent getNonMortgageMonthlyDebtToIncomeRatio(final EIncomeComparisonType incomeType) {
        return getIncomeTypeComp(incomeType).getNonMortgageDebtToIncomeRatio();
    }

    public Percent getMortgageMonthlyDebtToIncomeRatio(final EIncomeComparisonType incomeType) {
        return getIncomeTypeComp(incomeType).getMortgageDebtToIncomeRatio();
    }

    public Percent getTotalMonthlyDebtToIncomeRatio(final EIncomeComparisonType incomeType) {
       return getIncomeTypeComp(incomeType).getTotalDebtToIncomeRatio();
    }

    private Double debtPaymentAggregation() {
        return debts.values().stream()
                .reduce(0.0, (sum, debt) -> sum + debt.getMonthlyDebtPayment(), Double::sum);
    }

    private void updateIncomeComparisonsPayments() {
        final Double debtPaymentAggregation = debtPaymentAggregation();
        grossIncomeComp.updateDebtPaymentToIncomeRatio(debtPaymentAggregation, mortgagePayment);
        netIncomeComp.updateDebtPaymentToIncomeRatio(debtPaymentAggregation, mortgagePayment);
    }

    public void setMortgagePayment(final Double mortgagePayment){
        this.mortgagePayment = mortgagePayment;
        updateIncomeComparisonsPayments();
    }

    public void updateIncomes(final Double grossIncome, final Double netIncome) {
        this.grossIncome = grossIncome;
        this.netIncome = netIncome;
        grossIncomeComp.setAnnualIncomeAmountForType(grossIncome);
        netIncomeComp.setAnnualIncomeAmountForType(netIncome);
        updateIncomeComparisonsPayments();
    }

    public void addNewDebt(final Debt newDebt) {
        if(debts.containsKey(newDebt.getDebtor())) {
            throw new RuntimeException("Debt already exists");
        }
        debts.put(newDebt.getDebtor(), newDebt);
        updateIncomeComparisonsPayments();
    }

    public void updateDebt(final Debt debt) {
        if(!debts.containsKey(debt.getDebtor())) {
            throw new RuntimeException("Debt doesn't exist");
        }
        debts.put(debt.getDebtor(), debt);
        updateIncomeComparisonsPayments();
    }

    public void removeDebt(final Debt debt) {
        if(!debts.containsKey(debt.getDebtor())) {
            throw new RuntimeException("Debt doesn't exist");
        }
        debts.remove(debt.getDebtor());
        updateIncomeComparisonsPayments();
    }
}
