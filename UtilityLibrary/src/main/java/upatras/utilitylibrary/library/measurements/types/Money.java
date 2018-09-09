/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.types;

/**
 * For anything that can be measured in euros
 *
 * @author Paris
 */
public class Money extends AbstractGenericDouble<Money> {

    public Money(double value) {
        super(value);
    }

    public Money() {
        super(0);
    }

    public Money(Money value) {
        super(value);
    }

    public static Money Euros(double euros) {
        return new Money(euros);
    }

    public static Money Dollars(double dollars) {
        return new Money(dollars*0.86);
    }

    @Override
    protected Money generateParentClass(double value) {
        return new Money(value);
    }

}
