/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.environmentalvariables;

import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalVariable;
import upatras.utilitylibrary.library.measurements.types.Temperature;

/**
 * @author Paris
 */
public class TemperatureVariable extends AbstractEnvironmentalVariable<Temperature> {

    public TemperatureVariable(double initialtemperature) {
        unit = new Temperature(initialtemperature);
    }

    public TemperatureVariable(Temperature initialtemperature) {
        unit = initialtemperature;
    }

    public void zero() {
        rwlock.writeLock().lock();
        try {
            unit = new Temperature(0);
        } finally {
            rwlock.writeLock().unlock();
        }
    }

    public void add(Temperature temperature) {
        rwlock.writeLock().lock();
        try {
            if (Double.isNaN(temperature.value) || Double.isInfinite(temperature.value)) {
                System.out.println("errorneous add");
            }else {
                unit = new Temperature(unit.value + temperature.value);
            }
            } finally {
            rwlock.writeLock().unlock();
        }

    }

}
