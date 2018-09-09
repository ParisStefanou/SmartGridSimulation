/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices;

import org.joda.time.DateTime;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.simulationmodels.devices.basic.VariableRampGenerator;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class PowerPlant extends VariableRampGenerator {

	public PowerPlant(ComplexPower initial_power, ComplexPower max_power, ComplexPower ramp_rate_ms, PowerEnviroment enviroment) {
		super(initial_power, max_power, ramp_rate_ms, enviroment);
	}

	public final void produceMaximum(DateTime command_time) {
		setProduction(command_time, getMaxPower());
	}

	public final void produceMinimum(DateTime command_time) {
		setProduction(command_time, new ComplexPower());
	}

	public void increaseProduction(DateTime command_time, ComplexPower power) {
		increasePower(command_time, power);
	}

	public final void decreaseProduction(DateTime command_time, ComplexPower power) {
		increaseProduction(command_time, power.multipliedBy(-1, -1));
	}

	public final void setProduction(DateTime command_time, ComplexPower power) {
		setPower(command_time, power);
	}
}
