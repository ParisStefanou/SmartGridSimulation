/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.main;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paris
 */
public abstract class TimeInstance {

	public TimeStep current_simulation_step;

	protected DateTime present;

	public TimeInstance(DateTime present) {
		this.present = present;
	}

	public TimeInstance setPresent(DateTime present) {
		this.present = present;
		return this;
	}

	public DateTime getPresent() {
		return present;
	}

	public abstract void advanceStep();

	public abstract void advanceDuration(Duration duration);

	public abstract void advanceRealTime();

	public void advanceToDateTime(DateTime target) {

		if (target.isBefore(present)) {
			try {
				throw new Exception("future : " + target + " cant be before present :" + present);
			} catch (Exception ex) {
				Logger.getLogger(TimeInstance.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Duration advanceduration = new Duration(present, target);
		if (advanceduration.isShorterThan(Duration.millis(100))) {
			System.err.println("Duration of step is very small " + advanceduration.getMillis() + " ms , this will slow down the simulation a lot");
		} else {
			advanceDuration(advanceduration);
		}
	}

	public abstract void shutdown();

}
