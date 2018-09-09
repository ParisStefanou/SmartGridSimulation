/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.units.electrical;

import org.joda.time.Duration;
import upatras.utilitylibrary.library.measurements.types.Energy;
import upatras.utilitylibrary.library.measurements.types.Power;

/**
 *
 * @author Paris
 */
public class ActivePower extends Power {

	/**
	 * To be used when active power is meaningful
	 *
	 * @param value initial value
	 */
	public ActivePower(double value) {
		super(value);
	}

	/**
	 * To be used when active power is meaningful
	 *
	 * @param value the value to be used
	 */
	public ActivePower(Power value) {
		super(value);
	}

	public Energy getEnergy(Duration duration) {
		return new Energy(value * duration.getMillis() / 1000.0);

	}

}
