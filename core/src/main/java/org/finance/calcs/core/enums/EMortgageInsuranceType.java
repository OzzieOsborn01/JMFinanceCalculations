package org.finance.calcs.core.enums;

/**
 * Enum representing the type of mortgage insurance. Either Private Mortgage Insurance (PMI - Traditional loan) or
 * Mortgage Insurance Premiums (MIP - FHA loan)
 */
public enum EMortgageInsuranceType {
    /**
     * Private Mortgage Insurance (PMI) - Mortgage insurance for a traditional/conventional loan.<p/>
     * Typically lasts until 20% or 22% of the loan is paid off
     */
    PRIVATE_MORTGAGE_INSURANCE, // Conventional Loans
    /**
     * Mortgage Insurance Premiums (MIP) - Mortgage insurance for a FHA loan.<p/>
     * Typically lasts for 11 years or for the life of the loan
     */
    MORTGAGE_INSURANCE_PREMIUMS
}
