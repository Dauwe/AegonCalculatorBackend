package nl.aegon.calculator.service;

import nl.aegon.calculator.model.Calculation;
import nl.aegon.calculator.model.Method;
import nl.aegon.calculator.repository.CalculationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleCalculatorTest {
    @InjectMocks
    private SimpleCalculator simpleCalculator;

    @Mock
    private CalculationRepository calculationRepository;

    @Test
    void calculateAdd() {
        //given
        assertEquals(calculationRepository.count(), 0);
        Calculation calculation = new Calculation();
        calculation.setNumberOne(2);
        calculation.setNumberTwo(3);
        calculation.setMethod(Method.ADD);

        //when
        Calculation actual = simpleCalculator.calculate(calculation);

        //then
        assertEquals(5.0, actual.getResult());
        verify(calculationRepository).save(actual);
    }

    @Test
    void calculateSubtract() {
        //given
        assertEquals(calculationRepository.count(), 0);
        Calculation calculation = new Calculation();
        calculation.setNumberOne(2);
        calculation.setNumberTwo(3);
        calculation.setMethod(Method.SUBTRACT);

        //when
        Calculation actual = simpleCalculator.calculate(calculation);

        //then
        assertEquals(-1.0, actual.getResult());
        verify(calculationRepository).save(actual);
    }

    @Test
    void calculateMultiply() {
        //given
        assertEquals(calculationRepository.count(), 0);
        Calculation calculation = new Calculation();
        calculation.setNumberOne(2);
        calculation.setNumberTwo(3);
        calculation.setMethod(Method.MULTIPLY);

        //when
        Calculation actual = simpleCalculator.calculate(calculation);

        //then
        assertEquals(6.0, actual.getResult());
        verify(calculationRepository).save(actual);
    }

    @Test
    void calculateDivide() {
        //given
        assertEquals(calculationRepository.count(), 0);
        Calculation calculation = new Calculation();
        calculation.setNumberOne(2);
        calculation.setNumberTwo(4);
        calculation.setMethod(Method.DIVIDE);

        //when
        Calculation actual = simpleCalculator.calculate(calculation);

        //then
        assertEquals(0.5, actual.getResult());
        verify(calculationRepository).save(actual);
    }

    @Test
    void calculateDivideByZero() {
        //given
        assertEquals(calculationRepository.count(), 0);
        Calculation calculation = new Calculation();
        calculation.setNumberOne(2);
        calculation.setNumberTwo(0);
        calculation.setMethod(Method.DIVIDE);

        //when&then
        assertThrows(ArithmeticException.class, () ->simpleCalculator.calculate(calculation));
        assertEquals(calculationRepository.count(), 0);
    }

    @Test
    void getCalculations() {
        //given
        ArrayList<Calculation> calculations = new ArrayList<>();
        calculations.add(new Calculation());

        //when
        when(calculationRepository.findAll()).thenReturn(calculations);
        Collection<Calculation> actual = simpleCalculator.getCalculations();

        //then
        assertEquals(calculations, actual);
    }
}
