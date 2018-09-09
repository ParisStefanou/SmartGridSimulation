/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.units.electrical;

import upatras.utilitylibrary.library.measurements.types.AbstractUnit;
import upatras.utilitylibrary.library.measurements.types.Power;

/**
 * For anything that can be measured in VoltAmperes
 *
 * @author Paris
 */
public class VoltAmpere extends AbstractUnit<VoltAmpere> {

    public final Power real;
    public final Var imaginary;

    /**
     * For anything that can be measured in VoltAmperes
     *
     * @param real      real power as a double
     * @param imaginary imaginary power as a double
     */
    public VoltAmpere(double real, double imaginary) {
        this.real = new Power(real);
        this.imaginary = new Var(imaginary);
    }

    public VoltAmpere(Power real, Var imaginary) {
        this.real = new Power(real);
        this.imaginary = new Var(imaginary);
    }

    /**
     * For anything that can be measured in VoltAmperes
     *
     * @param value the value to be used
     */
    public VoltAmpere(VoltAmpere value) {
        this.real = value.real;
        this.imaginary = value.imaginary;
    }

    @Override
    public VoltAmpere plus(double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VoltAmpere plus(VoltAmpere d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VoltAmpere minus(VoltAmpere d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VoltAmpere minus(double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VoltAmpere multipliedBy(VoltAmpere d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VoltAmpere multipliedBy(double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VoltAmpere dividedBy(VoltAmpere d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VoltAmpere dividedBy(double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VoltAmpere clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return real.toString() + "," + imaginary.toString();
    }
}
