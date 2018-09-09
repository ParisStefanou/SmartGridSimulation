/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.environmentalvariables;

import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalVariable;
import upatras.utilitylibrary.library.measurements.types.GenericDouble;

/**
 *
 * @author Paris
 */
public class DoubleVariable extends AbstractEnvironmentalVariable<GenericDouble> {

    public DoubleVariable() {
        unit = new GenericDouble(0);
    }

    public DoubleVariable( double initiavalue) {
        unit = new GenericDouble(0);
    }

    public void zero() {
        rwlock.writeLock().lock();
        try {
            unit = new GenericDouble(0);
        } finally {
            rwlock.writeLock().unlock();
        }

    }

    public void add(double variable) {
        rwlock.writeLock().lock();
        try {
            this.unit.value += variable;
        } finally {
            rwlock.writeLock().unlock();
        }

    }

}
