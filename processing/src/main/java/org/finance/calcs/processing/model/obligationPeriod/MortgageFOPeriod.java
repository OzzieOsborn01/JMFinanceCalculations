package org.finance.calcs.processing.model.obligationPeriod;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class MortgageFOPeriod {
    int periodNumber;
    double loanContribution;
    double newLoanBalance;
    double interestContribution;
    double insuranceContribution;
    double mortgageInsuranceContribution;
    double payment;
    LocalDate startDate;
    LocalDate endDate;
}
