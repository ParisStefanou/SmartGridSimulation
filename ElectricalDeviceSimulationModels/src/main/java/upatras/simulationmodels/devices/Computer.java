/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices;

import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.simulationmodels.commands.OnOffPowerCommands;
import upatras.simulationmodels.devices.basic.VariableLoad;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class Computer extends VariableLoad {

	public Computer(PowerEnviroment enviroment) {
		super(enviroment, new ComplexPower(600, 0));
	}

	@Override
	protected void processCommand(Command c) {
		super.processCommand(c);
		if (c.isCommand("Gaming")) {
			super.processCommand(OnOffPowerCommands.setPower(c.time, 100.0));
		} else if (c.isCommand("Computing")) {
			super.processCommand(OnOffPowerCommands.setPower(c.time, 50.0));
		} else if (c.isCommand("Browsing")) {
			super.processCommand(OnOffPowerCommands.setPower(c.time, 20.0));
		}
	}

}
