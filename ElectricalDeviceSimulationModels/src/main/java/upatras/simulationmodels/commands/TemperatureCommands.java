/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.commands;

import org.joda.time.DateTime;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.utilitylibrary.library.measurements.types.Temperature;

/**
 *
 * @author Paris
 */
public class TemperatureCommands {

	public static Command targetTemperature(DateTime time, Temperature target_temperature) {
		return new Command("Target Temperature", time, target_temperature);
	}

	public static Command TurnOff(DateTime time) {
		return new Command("Turn Off", time);
	}
	
		public static Command TurnOn(DateTime time) {
		return new Command("Turn On", time);
	}

}
