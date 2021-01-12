package nl.aegon.calculator.web;

import nl.aegon.calculator.model.Calculation;
import nl.aegon.calculator.service.SimpleCalculator;
import nl.aegon.calculator.web.dto.CalculationDto;
import nl.aegon.calculator.web.transformer.CalculationTransformer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    final SimpleCalculator simpleCalculator;

    public CalculatorController(SimpleCalculator simpleCalculator) {
        this.simpleCalculator = simpleCalculator;
    }

    @ExceptionHandler({ ArithmeticException.class })
    public ResponseEntity<Object> handleArithmeticException() {
        return ResponseEntity.badRequest().body("Can't divide by zero.");
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleValidationException() {
        return ResponseEntity.badRequest().body("Both numbers and method of calculation are required.");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<CalculationDto>> getCalculations() {
        Collection<CalculationDto> calculationDtos = simpleCalculator.getCalculations().stream()
                .map(CalculationTransformer::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(calculationDtos);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalculationDto> calculate(@RequestBody @Valid CalculationDto calculationDto) {
        if(calculationDto.getNumberTwo() == 0 & calculationDto.getMethod().equals("DIVIDE")){
            throw new ArithmeticException();
        }
        final Calculation calculation = CalculationTransformer.toModel(calculationDto);

        Calculation result = simpleCalculator.calculate(calculation);

        return ResponseEntity.ok(CalculationTransformer.toDto(result));
    }
}
