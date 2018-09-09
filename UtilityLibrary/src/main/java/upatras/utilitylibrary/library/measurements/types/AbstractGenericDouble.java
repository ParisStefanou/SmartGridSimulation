/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.types;

/**
 * @author Paris
 */
public abstract class AbstractGenericDouble<T extends AbstractGenericDouble> extends AbstractUnit<T> {

    /**
     * For anything that can be represented with a double, initialized as zero.
     */
    public AbstractGenericDouble() {
        this.value = 0;
    }

    /**
     * For anything that can be represented with a double
     *
     * @param value initial value
     */
    public AbstractGenericDouble(double value) {
        this.value = value;
    }

    /**
     * For anything that can be represented with a double
     *
     * @param value initial value
     */
    public AbstractGenericDouble(T value) {
        this.value = value.value;
    }

    /**
     * Amount of this unit
     */
    public double value;

    protected abstract T generateParentClass(double value);

    @Override
    public T plus(T d) {
        return generateParentClass(this.value + d.value);
    }

    @Override
    public T plus(double d) {
        return generateParentClass(this.value + d);
    }


    @Override
    public T minus(T d) {
        return generateParentClass(this.value - d.value);
    }

    @Override
    public T minus(double d) {
        return generateParentClass(this.value - d);
    }

    @Override
    public T multipliedBy(T d) {
        return generateParentClass(this.value * d.value);
    }

    @Override
    public T multipliedBy(double d) {
        return generateParentClass(this.value * d);
    }

    @Override
    public T dividedBy(T d) {
        return generateParentClass(this.value / d.value);
    }

    @Override
    public T dividedBy(double d) {
        return generateParentClass(this.value / d);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public T clone() {
        return generateParentClass(this.value);
    }


}
