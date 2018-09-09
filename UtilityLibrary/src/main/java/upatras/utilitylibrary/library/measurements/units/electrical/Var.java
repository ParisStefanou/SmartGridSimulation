/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.units.electrical;

import org.joda.time.Duration;
import upatras.utilitylibrary.library.measurements.types.AbstractGenericDouble;
import upatras.utilitylibrary.library.measurements.types.Energy;

/**
 *
 * @author Paris
 */
public class Var extends AbstractGenericDouble<Var> {

	public double imaginary;

	/**
	 * For anything that can be measured in Vars, initialized as zero.
	 *
	 */
	public Var() {
		this.imaginary = 0;
	}

	/**
	 * For anything that can be measured in Vars
	 *
	 * @param complex initial complex value
	 */
	public Var(double complex) {
		this.imaginary = complex;
	}

	/**
	 * For anything that can be measured in Vars
	 *
	 * @param value initial value
	 */
	public Var(Var value) {
		this.imaginary = value.imaginary;
	}

	public Energy toJoule(Duration duration) {
		return new Energy(this.value, duration);
	}

	@Override
	protected Var generateParentClass(double value) {
		return new Var(value);
	}

}
