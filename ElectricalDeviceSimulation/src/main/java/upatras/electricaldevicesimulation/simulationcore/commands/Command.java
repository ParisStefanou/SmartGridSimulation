/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.commands;

import org.joda.time.DateTime;

/**
 *
 * @author gaspa
 */
public class Command {

	public final String name;
	public final Object[] args;
	public DateTime time;

	public Command(String name, DateTime time, Object... args) {
		this.name = name;
		this.args = args;
		this.time = time;
	}

	public Command(String name, Object[] args) {
		this.name = name;
		this.args = args;
		time = null;
	}

	public boolean isCommand(String command_name) {
		return name.equals(command_name);
	}

}
