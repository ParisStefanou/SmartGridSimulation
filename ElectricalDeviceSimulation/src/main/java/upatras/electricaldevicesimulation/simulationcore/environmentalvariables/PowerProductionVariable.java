/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.environmentalvariables;

import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalVariable;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class PowerProductionVariable extends AbstractEnvironmentalVariable<ComplexPower> {

	public PowerProductionVariable() {
		unit = new ComplexPower();
	}

	public PowerProductionVariable(ComplexPower initialpower) {
		unit = initialpower;
	}

	public void zero() {

		rwlock.writeLock().lock();
		try {
			unit = new ComplexPower(0, 0);
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

	public void subtract(ComplexPower power) {
		rwlock.writeLock().lock();
		try {
			unit = unit.minus(power);
		} finally {
			rwlock.writeLock().unlock();
		}

	}


}
