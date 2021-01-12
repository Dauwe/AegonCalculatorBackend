package nl.aegon.calculator.web.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class CalculationDto {
    private long id;

    @NotNull(message = "Both numbers and method of calculation are required.")
    private int numberOne;

    @NotNull(message = "Both numbers and method of calculation are required.")
    private int numberTwo;

    @NotNull(message = "Both numbers and method of calculation are required.")
    private String method;

    private double result;
}
