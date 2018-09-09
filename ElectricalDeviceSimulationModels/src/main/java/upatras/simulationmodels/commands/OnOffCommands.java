/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.commands;

import org.joda.time.DateTime;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;

/**
 *
 * @author gaspa
 */
public class OnOffCommands {

	public static Command TurnOn(DateTime time) {
		return new Command("Turn On", time);
	}

	public static Command TurnOff(DateTime time) {
		return new Command("Turn Off", time);
	}

	@Deprecated
	public static Command TurnOn() {
		return new Command("Turn On", null);
	}

	@Deprecated
	public static Command TurnOff() {
		return new Command("Turn Off", null);
	}

}
