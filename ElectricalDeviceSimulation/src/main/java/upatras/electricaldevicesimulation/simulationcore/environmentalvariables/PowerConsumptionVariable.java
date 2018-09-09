/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.environmentalvariables;

import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalVariable;
import upatras.utilitylibrary.library.measurements.Measurement;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class PowerConsumptionVariable extends AbstractEnvironmentalVariable<ComplexPower> {

    public PowerConsumptionVariable() {
        unit = new ComplexPower();
    }

    public PowerConsumptionVariable(ComplexPower initialpower) {
        unit = initialpower;
    }

    public void zero() {
        rwlock.writeLock().lock();
        try {
            unit = new ComplexPower();
        } finally {
            rwlock.writeLock().unlock();
        }

    }

    public void add(ComplexPower power) {
        rwlock.writeLock().lock();
        try {
            unit = unit.plus(power);
        } finally {
            rwlock.writeLock().unlock();
        }

    }

}
