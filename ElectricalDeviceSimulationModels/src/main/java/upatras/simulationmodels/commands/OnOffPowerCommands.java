/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.commands;

import org.joda.time.DateTime;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class OnOffPowerCommands extends OnOffCommands {

	public static Command setPower(DateTime time, double power_percent) {

		return new Command("Set Power", time, power_percent);

	}

	public static Command setPower(DateTime time, ComplexPower target_power) {

		return new Command("Set Power", time, target_power);

	}

	public static Command increasePower(DateTime time, ComplexPower power_increase) {

		return new Command("Increase Power", time, power_increase);
	}

	public static Command decreasePower(DateTime time, ComplexPower power_increase) {

		return new Command("Decrease Power", time, power_increase);
	}

}
