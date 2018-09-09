/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.highaccuracy;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerConsumptionVariable;
import upatras.simulationmodels.commands.TemperatureCommands;
import upatras.simulationmodels.enviroments.OutdoorEnviroment;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemCollector;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.types.Temperature;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import upatras.simulationmodels.commands.OnOffCommands;

/**
 *
 * @author Paris
 */
public class RefrigeratorHATest {

	public RefrigeratorHATest() {
	}

	/**
	 * Test of processCommand method, of class RefrigeratorHA.
	 */
	@Test
	public void passiveHotterTest() {
		DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
		DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
		in1.setPresent(starttime);

		System.out.println("Generating Devices");

		OutdoorEnviroment env = new OutdoorEnviroment("out", in1,null);
		RefrigeratorHA refrigerator = new RefrigeratorHA(env, new Power(600), new Volume(6), new Temperature(10), 0.1);

		refrigerator.submitCommand(TemperatureCommands.targetTemperature(starttime, new Temperature(8)));
		refrigerator.submitCommand(TemperatureCommands.targetTemperature(starttime.plusMinutes(1), new Temperature(8)));
		refrigerator.submitCommand(TemperatureCommands.targetTemperature(starttime.plusMinutes(2), new Temperature(8)));
		refrigerator.submitCommand(TemperatureCommands.targetTemperature(starttime.plusMinutes(3), new Temperature(8)));

		in1.setStepDuration(Duration.standardMinutes(1));

		in1.advanceStep();

		assertTrue(((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble() <= 600);

		in1.advanceToDateTime(starttime.plusHours(1));

		assertEquals(8, refrigerator.internal_temp.get().value, 0.5);

		refrigerator.submitCommand(TemperatureCommands.targetTemperature(starttime.plusHours(1), new Temperature(4)));

		in1.advanceDuration(Duration.standardHours(1));

		assertEquals(4, refrigerator.internal_temp.get().value, 0.5);

		refrigerator.submitCommand(TemperatureCommands.TurnOff(starttime.plusHours(2)));

		in1.advanceDuration(Duration.standardHours(8));

		assertTrue(refrigerator.internal_temp.get().value > 12);

	}

	/**
	 * Test of processCommand method, of class RefrigeratorHA.
	 */
	@Test
	public void activeTest() {
		DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
		DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
		in1.setPresent(starttime);

		System.out.println("Generating Devices");

		OutdoorEnviroment env = new OutdoorEnviroment("out", in1,null);
		RefrigeratorHA refrigerator = new RefrigeratorHA(env, new Power(400), new Volume(6), new Temperature(4), 0.1);
		MeasurableItemCollector mc1 = null;
		MeasurableItemCollector mc2 = null;
		if (false) {

			mc1 = new MeasurableItemCollector(refrigerator.internal_temp);
			mc2 = new MeasurableItemCollector(env.getVariable(PowerConsumptionVariable.class));
		}

		refrigerator.submitCommand(TemperatureCommands.targetTemperature(starttime, new Temperature(4)));

		//t=0s
		assertEquals(4, refrigerator.internal_temp.get().value, 0.5);

		in1.setStepDuration(Duration.standardSeconds(5)).advanceDuration(Duration.standardMinutes(10));

		//t=600s
		assertEquals(4, refrigerator.internal_temp.get().value, 0.5);

		refrigerator.submitCommand(new Command("Open door", starttime.plusMinutes(10)));

		in1.advanceDuration(Duration.standardSeconds(30));

		//t=630s
		assertNotEquals(4, refrigerator.internal_temp.get().value, 0.0001);
		double old_int_temp = refrigerator.internal_temp.get().value;

		in1.advanceDuration(Duration.standardSeconds(30));
		in1.advanceDuration(Duration.standardMinutes(9));

		//t=1200s
		refrigerator.submitCommand(new Command("Open door", starttime.plusMinutes(20)));
		refrigerator.submitCommand(new Command("Open door", starttime.plusMinutes(20).plusSeconds(30)));
		refrigerator.submitCommand(new Command("Open door", starttime.plusMinutes(20).plusSeconds(60)));
		refrigerator.submitCommand(new Command("Open door", starttime.plusMinutes(20).plusSeconds(90)));

		in1.advanceDuration(Duration.standardSeconds(120));
		assertNotEquals(4, refrigerator.internal_temp.get().value, 0.0001);
		double new_int_temp = refrigerator.internal_temp.get().value;
		assertTrue(new_int_temp > old_int_temp);

		if (mc1 != null) {
			mc1.visualize();
			mc2.visualize();
			try {
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(OvenHATest.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	/**
	 * Test of processCommand method, of class RefrigeratorHA.
	 */
	@Test
	public void passiveSteadyTest() {

		DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
		DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
		in1.setPresent(starttime);

		OutdoorEnviroment out = new OutdoorEnviroment("outside",  in1,null);

		RefrigeratorHA refrigerator = new RefrigeratorHA(out, new Power(600), new Volume(6), new Temperature(6), 0.15);
		refrigerator.submitCommand(TemperatureCommands.targetTemperature(starttime, new Temperature(6)));

		MeasurableItemCollector evc1 = null;
		if (false) {
			evc1 = new MeasurableItemCollector(out.getVariable(PowerConsumptionVariable.class));
		}

		in1.setStepDuration(Duration.standardMinutes(5));

		in1.advanceDuration(Duration.standardHours(1));

		double average_power = refrigerator.getAveragePowerConsumed().getRealAsDouble();
		for (int i = 0; i < 60; i++) {
			in1.advanceDuration(Duration.standardMinutes(5));
			assertEquals(average_power, refrigerator.getAveragePowerConsumed().getRealAsDouble(), average_power / 20.0);
		}

		if (evc1 != null) {
			evc1.visualize();
			try {
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(OvenHATest.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		in1.shutdown();
	}

	@Test
	public void predictedTimeHeating() {

		DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
		DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
		in1.setPresent(starttime);

		OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

		RefrigeratorHA refrigerator = new RefrigeratorHA(out, new Power(600), new Volume(6), new Temperature(6), 0.15);
		refrigerator.submitCommand(TemperatureCommands.targetTemperature(starttime, new Temperature(6)));

		refrigerator.submitCommand(TemperatureCommands.TurnOff(starttime.plusMillis(1)));

		in1.advanceDuration(refrigerator.timeToTemperature(new Temperature(7)));

		assertEquals(7, refrigerator.internal_temp.get().value, 0.1);

		in1.shutdown();
	}

	@Test
	public void predictedTimeCooling() {

		DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
		DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
		in1.setPresent(starttime);

		OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

		RefrigeratorHA refrigerator = new RefrigeratorHA(out, new Power(600), new Volume(6), new Temperature(6), 0.15);

		refrigerator.submitCommand(OnOffCommands.TurnOn(starttime.plusMillis(1)));

		in1.advanceDuration(refrigerator.timeToTemperature(new Temperature(5)));

		assertEquals(5, refrigerator.internal_temp.get().value, 0.1);
		in1.shutdown();
	}

	
}
