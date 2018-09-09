/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.basic;

import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class VariableLoad extends AbstractElectricalDevice {

	public ComplexPower target_power = new ComplexPower();
	public final ComplexPower max_power;

	public VariableLoad(PowerEnviroment enviroment, ComplexPower max_power) {
		super(enviroment);
		this.max_power = max_power;
	}

	@Override
	protected void processCommand(Command c) {
		if (c.name.equals("Turn Off")) {
			target_power = new ComplexPower();
		} else if (c.name.equals("Set Power")) {
			double powerratio = (double) c.args[0];
			target_power = max_power.multipliedBy(powerratio, powerratio);
		}
	}

	@Override
	public void simulate(TimeStep simulation_step) {

		handlePower(target_power);

	}

}
