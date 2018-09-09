/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.types;

/**
 * @author Paris
 */
public class Temperature extends AbstractGenericDouble<Temperature> {

    /**
     * For anything that can be measured in Degree Celsius
     *
     * @param value initial value
     */
    public Temperature(double value) {
        super(value);
    }

    public Temperature(Temperature unit) {
        super(unit);
    }

    public static Temperature degreecelsius(double C) {
        return new Temperature(C);
    }

    public static Temperature degreefarenheit(double F) {
        return new Temperature((F - 32) * 5 / 9);
    }

    @Override
    protected Temperature generateParentClass(double value) {
        return new Temperature(value);
    }

}
