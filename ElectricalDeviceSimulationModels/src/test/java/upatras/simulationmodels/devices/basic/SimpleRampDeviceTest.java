/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.basic;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerConsumptionVariable;
import upatras.simulationmodels.commands.OnOffCommands;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

import static org.junit.Assert.*;

/**
 *
 * @author Paris
 */
public class SimpleRampDeviceTest {

	public SimpleRampDeviceTest() {
	}

	/**
	 * Test of processCommand method, of class SimpleRampDevice.
	 */
	@Test
	public void testProcessCommand() {
		DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
		DeviceSimulationInstance dsi = new DeviceSimulationInstance();
		dsi.setPresent(starttime);

		System.out.println("Generating Devices");

		PowerEnviroment env = new PowerEnviroment(dsi);
		SimpleRampLoad ramp_load = new SimpleRampLoad(false, new ComplexPower(1000, 1000), new ComplexPower(1.0 / 100, 1.0 / 100), env);
		ramp_load.submitCommand(OnOffCommands.TurnOn(starttime.plusMinutes(1)));
		ramp_load.submitCommand(OnOffCommands.TurnOff(starttime.plusMinutes(5)));

		dsi.setStepDuration(Duration.standardSeconds(1)).advanceDuration(Duration.standardSeconds(30));

		assertEquals(0, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 0.001);

		dsi.advanceDuration(Duration.standardSeconds(31));

		//t = 61s
		assertNotEquals(0, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 0.001);

		dsi.advanceDuration(Duration.standardSeconds(50));

		//t = 111s
		assertEquals(500, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 20);

		dsi.advanceDuration(Duration.standardSeconds(3 * 60 + 10));

		//t = 301s
		assertTrue(((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble() < 1000);

		dsi.advanceDuration(Duration.standardSeconds(100));

		//t = 401s
		assertEquals(0, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 10);

		dsi.shutdown();

	}

}
