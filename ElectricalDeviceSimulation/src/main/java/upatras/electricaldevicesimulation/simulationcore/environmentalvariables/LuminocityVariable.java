/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.environmentalvariables;

import org.joda.time.DateTime;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalVariable;
import upatras.utilitylibrary.library.measurements.Measurement;
import upatras.utilitylibrary.library.measurements.types.GenericDouble;

/**
 *
 * @author Paris
 */
public class LuminocityVariable extends AbstractEnvironmentalVariable<GenericDouble> {

    public LuminocityVariable(double initialluminocity) {
        unit = new GenericDouble(initialluminocity);
    }

    public void zero() {
        rwlock.writeLock().lock();
        try {
            unit = new GenericDouble(0);
        } finally {
            rwlock.writeLock().unlock();
        }

    }


    public void add(double luminocity) {
        rwlock.writeLock().lock();
        try {
            this.unit = new GenericDouble(unit.value + luminocity);
        } finally {
            rwlock.writeLock().unlock();
        }

    }
}
