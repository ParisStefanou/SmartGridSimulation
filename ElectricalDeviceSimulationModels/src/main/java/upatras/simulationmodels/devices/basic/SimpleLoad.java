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
import upatras.utilitylibrary.library.measurements.units.electrical.ActivePower;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;
import upatras.utilitylibrary.library.measurements.units.electrical.ReactivePower;

/**
 *
 * @author gaspa
 */
public class SimpleLoad extends AbstractElectricalDevice {

	public ComplexPower max_power;
	boolean on = false;

	public SimpleLoad(PowerEnviroment enviroment, double active_power, double reactive_power) {
		super(enviroment);
		this.max_power = new ComplexPower(active_power, reactive_power);
	}

	public SimpleLoad(PowerEnviroment enviroment, ActivePower active_power, ReactivePower reactive_power) {
		super(enviroment);
		this.max_power = new ComplexPower(active_power, reactive_power);
	}

	public SimpleLoad(PowerEnviroment enviroment, ComplexPower load) {
		super(enviroment);
		this.max_power = load;
	}

	@Override
	protected void processCommand(Command c) {
		if (c.name.equals("Turn On")) {
			on = true;
		}
		if (c.name.equals("Turn Off")) {
			on = false;
		}
	}

	@Override
	public void simulate(TimeStep simulation_step) {

		if (on) {
			handlePower(max_power);
		}

	}

}
