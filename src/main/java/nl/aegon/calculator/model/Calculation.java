package nl.aegon.calculator.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CALCULATIONS")
@Getter
@Setter
public class Calculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private int numberOne;

    @NotNull
    private int numberTwo;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Method method;

    private double result;
}
