package nl.aegon.calculator.web.transformer;

import nl.aegon.calculator.model.Calculation;
import nl.aegon.calculator.model.Method;
import nl.aegon.calculator.web.dto.CalculationDto;

public class CalculationTransformer {

    public static CalculationDto toDto(final Calculation calculation) {
        final CalculationDto dto = new CalculationDto();
        dto.setId(calculation.getId());
        dto.setNumberOne(calculation.getNumberOne());
        dto.setNumberTwo(calculation.getNumberTwo());
        dto.setMethod(calculation.getMethod().name());
        if(calculation.getResult()!=0) {
            dto.setResult(calculation.getResult());
        }
        return dto;
    }

    public static Calculation toModel(final CalculationDto dto) {
        final Calculation calculation = new Calculation();
        calculation.setId(dto.getId());
        calculation.setNumberOne(dto.getNumberOne());
        calculation.setNumberTwo(dto.getNumberTwo());
        calculation.setMethod(Method.valueOf(dto.getMethod()));
        return calculation;
    }
}
