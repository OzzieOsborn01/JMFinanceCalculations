package org.finance.calcs.processing.testing.mappers;

import org.finance.calcs.processing.model.context.foContext.MortgageFOProcessorContext;
import org.finance.calcs.processing.testing.constructs.MortgageFOProcessorTestContext;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MortgageFOProcessorTestContextMapper {
    MortgageFOProcessorTestContextMapper INSTANCE = Mappers.getMapper(MortgageFOProcessorTestContextMapper.class);

    MortgageFOProcessorTestContext contextToTestContext(MortgageFOProcessorContext context);
}
