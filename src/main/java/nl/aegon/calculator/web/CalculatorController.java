package nl.aegon.calculator.web;

import nl.aegon.calculator.model.Calculation;
import nl.aegon.calculator.service.SimpleCalculator;
import nl.aegon.calculator.web.dto.CalculationDto;
import nl.aegon.calculator.web.transformer.CalculationTransformer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    final SimpleCalculator simpleCalculator;

    public CalculatorController(SimpleCalculator simpleCalculator) {
        this.simpleCalculator = simpleCalculator;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<CalculationDto>> getCalculations() {
        Collection<CalculationDto> calculationDtos = simpleCalculator.getCalculations().stream()
                .map(CalculationTransformer::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(calculationDtos);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalculationDto> calculate(@RequestBody CalculationDto calculationDto) {
        final Calculation calculation = CalculationTransformer.toModel(calculationDto);

        Calculation result = simpleCalculator.calculate(calculation);

        return ResponseEntity.ok(CalculationTransformer.toDto(result));
    }
}
