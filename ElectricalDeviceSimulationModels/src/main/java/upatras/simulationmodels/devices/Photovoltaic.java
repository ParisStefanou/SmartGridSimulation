/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.simulationmodels.commands.OnOffCommands;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.units.electrical.ActivePower;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;
import upatras.utilitylibrary.library.measurements.units.electrical.Var;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paris
 */
public class Photovoltaic extends AbstractElectricalDevice {

	ActivePower nominalpower;

	public Photovoltaic(PowerEnviroment enviroment, ActivePower nominalpower) {
		super(enviroment);
		this.nominalpower = nominalpower;
		this.processCommand(OnOffCommands.TurnOn());
	}

	public Photovoltaic(PowerEnviroment enviroment, double nominalpower) {
		super(enviroment);
		this.nominalpower = new ActivePower(nominalpower);
		this.processCommand(OnOffCommands.TurnOn());
	}

	@Override
	protected void processCommand(Command c) {

	}

	@Override
	public void simulate(TimeStep simulation_step) {

		if (simulation_step.duration.isLongerThan(new Duration(1000 * 60 * 60 * 6))) {
			try {
				throw new Exception("Simple pv cannot run for steps bigger than 6 hours");
			} catch (Exception ex) {
				Logger.getLogger(Photovoltaic.class.getName()).log(Level.SEVERE, null, ex);
			}
			return;
		}

		if (!((simulation_step.step_start.hourOfDay().get() >= 20 && simulation_step.step_end.hourOfDay().get() < 6)
				|| (simulation_step.step_start.hourOfDay().get() < 6 && simulation_step.step_end.hourOfDay().get() < 6)
				|| (simulation_step.step_start.hourOfDay().get() >= 20 && simulation_step.step_end.hourOfDay().get() >= 20))) {

			DateTime start;
			if (simulation_step.step_start.hourOfDay().get() >= 6) {
				start = simulation_step.step_start;
			} else {
				start = new DateTime(simulation_step.step_start.year().get(), simulation_step.step_start.monthOfYear().get(),
						simulation_step.step_start.dayOfMonth().get(), 6, 0, 0);
			}

			DateTime end;
			if (simulation_step.step_end.hourOfDay().get() < 20) {
				end = simulation_step.step_end;
			} else {
				end = new DateTime(simulation_step.step_end.year().get(), simulation_step.step_end.monthOfYear().get(),
						simulation_step.step_end.dayOfMonth().get(), 20, 0, 0);
			}

			if (start.isAfter(end)) {
				try {
					throw new Exception("start time :" + start + " of SimplePV step was after end time:" + end);
				} catch (Exception ex) {
					Logger.getLogger(Photovoltaic.class.getName()).log(Level.SEVERE, null, ex);
				}
				return;
			}

			long start_ms = start.getMillisOfDay();
			long end_ms = end.getMillisOfDay();

			double ms_to_h = 1000 * 60 * 60;

			double value_at_start = -Math.sin(Math.PI * 6.0 / 14.0 - Math.PI / (14.0 * ms_to_h) * (start_ms));
			double value_at_end = -Math.sin(Math.PI * 6.0 / 14.0 - Math.PI / (14.0 * ms_to_h) * (end_ms));

			double production = nominalpower.value * (value_at_start + value_at_end) / 2;

			producePower(new ComplexPower(production,0));

		}
	}

}
