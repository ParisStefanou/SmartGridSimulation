/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.types;

import org.joda.time.Duration;

/**
 * For anything that can be measured in Watts
 *
 * @author Paris
 */
public class Power extends AbstractGenericDouble<Power> {

	/**
	 * For anything that can be measured in Watts, initialized as zero.
	 *
	 */
	public Power() {
		super();
	}

	/**
	 * For anything that can be measured in Watts
	 *
	 * @param value initial value
	 */
	public Power(double value) {
		super(value);
	}

	/**
	 * For anything that can be measured in Watts
	 *
	 * @param value the value to be used
	 */
	public Power(Power value) {
		super(value);
	}

	public Energy toEnergy(Duration duration) {
		return new Energy(this, duration);
	}

	@Override
	protected Power generateParentClass(double value) {
		return new Power(value);
	}

	public Duration divide(Power watts) {

		return Duration.millis((long) (value / watts.value * 1000));
	}

}
