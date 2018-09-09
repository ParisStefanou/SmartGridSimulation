package upatras.utilitylibrary.library.measurements.units.electrical;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.time.Duration;

/**
 *
 * @author Paris
 */
public class ReactivePower extends Var {

	/**
	 * To be used when reactive power is meaningful
	 *initialized as zero
	 */
	public ReactivePower() {
		super(0);
	}
	
	/**
	 * To be used when reactive power is meaningful
	 *
	 * @param value initial value
	 */
	public ReactivePower(double value) {
		super(value);
	}

	/**
	 * To be used when reactive power is meaningful
	 *
	 * @param value the value to be used
	 */
	public ReactivePower(Var value) {
		super(value);
	}

	public ReactivePower(ReactivePower value) {
		super(value);
	}

	public double getEnergy(Duration duration) {
		return value * duration.toMillis() / 1000.0;

	}

}
