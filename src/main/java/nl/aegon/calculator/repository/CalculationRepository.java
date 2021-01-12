package nl.aegon.calculator.repository;

import nl.aegon.calculator.model.Calculation;
import org.springframework.data.repository.CrudRepository;

public interface CalculationRepository extends CrudRepository<Calculation, Long> {
}
