package org.finance.calcs.core.model.metadata;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Debt {
    private String debtor;
    private String reason;
    private Double debtTotal;
    private Double monthlyDebtPayment;

    public void updateDebtPayments(Double newDebtTotal, Double newMonthlyPayment) {
        debtTotal = newDebtTotal;
        monthlyDebtPayment = newMonthlyPayment;
    }
}
