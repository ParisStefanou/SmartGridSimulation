/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.highaccuracy;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerProductionVariable;
import upatras.simulationmodels.commands.OnOffCommands;
import upatras.simulationmodels.devices.Photovoltaic;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemCollector;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 *
 * @author Paris
 */
public class PhotovoltaicTest {

	public PhotovoltaicTest() {
	}

	/**
	 * Test of processCommand method, of class PhotovoltaicHA.
	 */
	@Test
	public void testTimePowerHA() {
		DateTime starttime = new DateTime(2017, 1, 1, 0, 0, 0);
		DeviceSimulationInstance in1 = new DeviceSimulationInstance();
		in1.setPresent(starttime);

		System.out.println("Generating Devices");

		PowerEnviroment env = new PowerEnviroment(in1);
		PhotovoltaicHA pv_ha = new PhotovoltaicHA(env, new ComplexPower(1000, 0));

		MeasurableItemCollector mc = null;
		if (false) {

			mc = new MeasurableItemCollector(env.getVariable(PowerProductionVariable.class));

		}

		in1.advanceDuration(Duration.standardHours(4));

		//4
		assertEquals(0, pv_ha.getAveragePowerProduced().getRealAsDouble(), 5);
		in1.advanceDuration(Duration.standardHours(4));

		//8
		assertNotEquals(0, pv_ha.getAveragePowerProduced().getRealAsDouble(), 1);
		in1.advanceDuration(Duration.standardHours(4));

		//12
		assertNotEquals(0, pv_ha.getAveragePowerProduced().getRealAsDouble(), 1);
		in1.advanceDuration(Duration.standardHours(4));

		//16
		assertNotEquals(0, pv_ha.getAveragePowerProduced().getRealAsDouble(), 1);
		in1.advanceDuration(Duration.standardHours(6));

		//22
		assertEquals(0, pv_ha.getAveragePowerProduced().getRealAsDouble(), 1);
		in1.advanceDuration(Duration.standardHours(2));

		//24
		assertEquals(0, pv_ha.getAveragePowerProduced().getRealAsDouble(), 1);
		if (mc != null) {
			mc.visualize();
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException ex) {
				Logger.getLogger(OvenHATest.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	/**
	 * Test of processCommand method, of class PhotovoltaicHA.
	 */
	@Test
	public void testTimePowerOffsetHA() {

		DateTime starttime = new DateTime(2017, 6, 1, 12, 0, 0);
		DeviceSimulationInstance in1 = new DeviceSimulationInstance();
		in1.setPresent(starttime);

		System.out.println("Generating Devices");

		PowerEnviroment env = new PowerEnviroment(in1);
		PhotovoltaicHA pv_ha = new PhotovoltaicHA(env, new ComplexPower(1000, 0));

		in1.advanceDuration(Duration.standardHours(1));

		//13
		assertNotEquals(0, pv_ha.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(3));

		//16
		assertNotEquals(0, pv_ha.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(4));

		//20
		assertEquals(0, pv_ha.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(4));

		//24
		assertEquals(0, pv_ha.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(4));

		//4
		assertEquals(0, pv_ha.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(4));

		//8
		assertNotEquals(0, pv_ha.getAveragePowerProduced().getRealAsDouble(), 1);

	}

	/**
	 * Test of processCommand method, of class PhotovoltaicHA.
	 */
	@Test
	public void testTimePowerSimple() {
		DateTime starttime = new DateTime(2017, 1, 1, 0, 0, 0);
		DeviceSimulationInstance in1 = new DeviceSimulationInstance();
		in1.setPresent(starttime);

		System.out.println("Generating Devices");

		PowerEnviroment env = new PowerEnviroment(in1);
		Photovoltaic simple_pv = new Photovoltaic(env, 1000);
		simple_pv.submitCommand(OnOffCommands.TurnOn());

		in1.advanceDuration(Duration.standardHours(4));

		//4
		assertEquals(0, simple_pv.getAveragePowerProduced().getRealAsDouble(), 5);

		in1.advanceDuration(Duration.standardHours(4));

		//8
		assertNotEquals(0, simple_pv.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(4));

		//12
		assertNotEquals(0, simple_pv.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(4));

		//16
		assertNotEquals(0, simple_pv.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(6));

		//22
		assertEquals(0, simple_pv.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(2));

		//24
		assertEquals(0, simple_pv.getAveragePowerProduced().getRealAsDouble(), 1);
	}

	/**
	 * Test of processCommand method, of class PhotovoltaicHA.
	 */
	@Test
	public void testTimePowerOffsetsimple() {

		DateTime starttime = new DateTime(2017, 6, 1, 12, 0, 0);
		DeviceSimulationInstance in1 = new DeviceSimulationInstance();
		in1.setPresent(starttime);

		System.out.println("Generating Devices");

		PowerEnviroment env = new PowerEnviroment(in1);
		Photovoltaic simple_pv = new Photovoltaic(env, 1000);

		in1.advanceDuration(Duration.standardHours(1));

		//13
		assertNotEquals(0, simple_pv.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(3));

		//16
		assertNotEquals(0, simple_pv.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(4));

		//20
		assertEquals(0, simple_pv.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(4));

		//24
		assertEquals(0, simple_pv.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(4));

		//4
		assertEquals(0, simple_pv.getAveragePowerProduced().getRealAsDouble(), 1);

		in1.advanceDuration(Duration.standardHours(4));

		//8
		assertNotEquals(0, simple_pv.getAveragePowerProduced().getRealAsDouble(), 1);

	}
}
