package nl.aegon.calculator.service;

import nl.aegon.calculator.model.Calculation;
import nl.aegon.calculator.repository.CalculationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class SimpleCalculator {
    final CalculationRepository calculationRepository;

    public SimpleCalculator(CalculationRepository calculationRepository) {
        this.calculationRepository = calculationRepository;
    }

    public double add(int x, int y){
        return x + y;
    }

    public double subtract(int x, int y){
        return x - y;
    }

    public double multiply(int x, int y){
        return x * y;
    }

    public double divide(int x, int y){
        if(y==0){
            throw new ArithmeticException();
        }
        return (double)x / (double) y;
    }

    public Calculation calculate(Calculation calculation) {
        int numberOne = calculation.getNumberOne();
        int numberTwo = calculation.getNumberTwo();
        double result;

        switch(calculation.getMethod()){
            case ADD:
                result = this.add(numberOne, numberTwo);
                break;
            case SUBTRACT:
                result = this.subtract(numberOne, numberTwo);
                break;
            case MULTIPLY:
                result = this.multiply(numberOne, numberTwo);
                break;
            case DIVIDE:
                result = this.divide(numberOne, numberTwo);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + calculation.getMethod());
        }
        calculation.setResult(result);
        calculationRepository.save(calculation);
        return calculation;
    }

    public Collection<Calculation> getCalculations(){
        Iterable<Calculation> iterable = calculationRepository.findAll();

        ArrayList<Calculation> calculations = new ArrayList<>();
        iterable.forEach(calculations::add);

        return calculations;
    }
}
