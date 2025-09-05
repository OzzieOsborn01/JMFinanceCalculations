package org.finance.calcs.core.model.mortgage;

import lombok.Builder;
import lombok.Data;
import org.finance.calcs.core.model.components.interest.InterestFOC;
import org.finance.calcs.core.model.components.loan.LoanFOC;
import org.finance.calcs.core.model.metadata.PersonalDetails;

@Data
@Builder
public class Mortgage {
    private final PersonalDetails personalDetails;

    private final LoanFOC loanComponent;

    private final InterestFOC interestComponent;
}
