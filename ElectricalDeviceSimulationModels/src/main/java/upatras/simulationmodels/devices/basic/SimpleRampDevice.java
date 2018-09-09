/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.basic;

import org.joda.time.DateTime;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.simulationmodels.commands.OnOffCommands;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public abstract class SimpleRampDevice extends AbstractElectricalDevice {

	ComplexPower max_power;

	ComplexPower target_power;

	ComplexPower current_power;

	ComplexPower ramp_rate_ms;

	public SimpleRampDevice(Boolean on, ComplexPower max_power, ComplexPower ramp_rate_ms, PowerEnviroment enviroment) {
		super(enviroment);
		this.max_power = max_power;
		this.ramp_rate_ms = ramp_rate_ms;
		if (on) {
			current_power = max_power;
			target_power = max_power;
		} else {
			current_power = new ComplexPower();
			target_power = new ComplexPower();
		}
	}

	@Override
	protected void processCommand(Command c) {

		if (c.name.equals("Turn On")) {
			target_power = new ComplexPower(max_power);
		} else if (c.name.equals("Turn Off")) {
			target_power = new ComplexPower();
		}
	}

	public void turnOn(DateTime time) {
		submitCommand(OnOffCommands.TurnOn(time));
	}

	public void turnOff(DateTime time) {
		submitCommand(OnOffCommands.TurnOff(time));
	}

}
