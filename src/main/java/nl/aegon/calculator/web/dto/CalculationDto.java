package nl.aegon.calculator.web.dto;

import lombok.Data;

@Data
public class CalculationDto {
    private long id;

    private int numberOne;

    private int numberTwo;

    private String method;

    private double result;
}
