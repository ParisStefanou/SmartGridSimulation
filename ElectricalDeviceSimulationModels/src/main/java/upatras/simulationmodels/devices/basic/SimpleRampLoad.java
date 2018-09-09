/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.basic;

import org.joda.time.Duration;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class SimpleRampLoad extends SimpleRampDevice {

	public SimpleRampLoad(Boolean on, ComplexPower max_power, ComplexPower ramp_rate_ms, PowerEnviroment enviroment) {
		super(on, max_power, ramp_rate_ms, enviroment);
	}

	public SimpleRampLoad(Boolean on, ComplexPower max_power, Duration ramp_duration, PowerEnviroment enviroment) {
		super(on, max_power, max_power.dividedBy(ramp_duration.getMillis(), ramp_duration.getMillis()), enviroment);
	}

	@Override
	public void simulate(TimeStep simulation_step) {

		if (current_power.getRealAsDouble() == target_power.getRealAsDouble()) {
			consumePower(target_power);
			return;
		}
		long total_ms = simulation_step.duration.getMillis();

		double ramp_left = target_power.getRealAsDouble() - current_power.getRealAsDouble();

		long time_left_for_ramp_duration = Math.abs((long) (ramp_left / ramp_rate_ms.getRealAsDouble()));
		time_left_for_ramp_duration = Math.min(time_left_for_ramp_duration, total_ms);
		long target_power_duration = total_ms - time_left_for_ramp_duration;

		ComplexPower next_power;

		if (current_power.getRealAsDouble() < target_power.getRealAsDouble()) {

			next_power = new ComplexPower(current_power.getRealAsDouble() + ramp_rate_ms.getRealAsDouble() * time_left_for_ramp_duration,
					current_power.getImaginaryAsDouble() + ramp_rate_ms.getImaginaryAsDouble() * time_left_for_ramp_duration);
		} else {
			next_power = new ComplexPower(current_power.getRealAsDouble() - ramp_rate_ms.getRealAsDouble() * time_left_for_ramp_duration,
					current_power.getImaginaryAsDouble() - ramp_rate_ms.getImaginaryAsDouble() * time_left_for_ramp_duration);
		}

		double average_active_power = ((next_power.getRealAsDouble() + current_power.getRealAsDouble()) / 2 * time_left_for_ramp_duration
				+ target_power.getRealAsDouble() * target_power_duration) / total_ms;

		double average_reactive_power = ((next_power.getImaginaryAsDouble() + current_power.getImaginaryAsDouble()) / 2 * time_left_for_ramp_duration
				+ target_power.getImaginaryAsDouble() * target_power_duration) / total_ms;

		consumePower(new ComplexPower(average_active_power, average_reactive_power));

		current_power = next_power;

	}

}
