/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.units.electrical;

import upatras.utilitylibrary.library.measurements.types.AbstractGenericDouble;

/**
 *
 * @author Paris
 */
public class Volt extends AbstractGenericDouble<Volt> {

	/**
	 * For anything that can be measured in Volts
	 *
	 * @param value initial value
	 */
	public Volt(double value) {
		super(value);
	}

	/**
	 * For anything that can be measured in Volts
	 *
	 * @param value the value to be used
	 */
	public Volt(Volt value) {
		super(value);
	}

	/**
	 * Amount of this unit
	 *
	 */
	public double value;

	@Override
	protected Volt generateParentClass(double value) {
		return new Volt(value);
	}
}
