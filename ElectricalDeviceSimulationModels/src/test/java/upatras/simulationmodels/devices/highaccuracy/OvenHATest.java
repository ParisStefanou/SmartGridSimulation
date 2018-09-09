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
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerConsumptionVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.TemperatureVariable;
import upatras.simulationmodels.commands.TemperatureCommands;
import upatras.simulationmodels.enviroments.OutdoorEnviroment;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemCollector;
import upatras.utilitylibrary.library.measurements.Measurement;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

/**
 *
 * @author Paris
 */
public class OvenHATest {

	public OvenHATest() {
	}

	/**
	 * Test of heatTransfer method, of class OvenHA.
	 */
	@Test
	public void testAll() {
		DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
		DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
		in1.setPresent(starttime);

		System.out.println("Generating Devices");

		BasicEnviroment env = new OutdoorEnviroment("outside",in1,null);
		OvenHA oven = new OvenHA(env, new Power(2150), new Volume(1));

		MeasurableItemCollector mc = null;
		if (false) {
			mc = new MeasurableItemCollector(oven.internal_temp);
		}

		oven.submitCommand(TemperatureCommands.targetTemperature(starttime, new Temperature(220)));
		oven.submitCommand(TemperatureCommands.TurnOff(starttime.plusHours(1)));

		assertEquals(oven.internal_temp.get().value, 27, 10);

		in1.setStepDuration(Duration.standardMinutes(1)).advanceDuration(Duration.standardHours(1).plus(Duration.millis(1)));

		Measurement m = oven.oven_internal.getVariable(PowerConsumptionVariable.class).getMeasurement(in1.getPresent());
		if (m.data instanceof ComplexPower) {
			assertEquals(0,((ComplexPower) m.data).real.value,  0.1);
		} else {
			fail("Measurement of wrong type");
		}
        m = oven.oven_internal.getVariable(TemperatureVariable.class).getMeasurement(in1.getPresent());
        if (m.data instanceof Temperature) {
            assertEquals( 220,((Temperature) m.data).value, 1);
        } else {
            fail("Measurement of wrong type");
        }



        assertEquals(220, oven.internal_temp.get().value, 5);

		in1.advanceDuration(Duration.standardMinutes(15));

		m = oven.oven_internal.getVariable(PowerConsumptionVariable.class).getMeasurement(in1.getPresent());
		if (m.data instanceof ComplexPower) {
			assertEquals(0,((ComplexPower) m.data).real.value,  0.1);
		} else {
			fail("Measurement of wrong type");
		}

        m = oven.oven_internal.getVariable(TemperatureVariable.class).getMeasurement(in1.getPresent());
        if (m.data instanceof Temperature) {
            assertEquals( 27,((Temperature) m.data).value, 10);
        } else {
            fail("Measurement of wrong type");
        }

		assertEquals(27, oven.internal_temp.get().value, 10);

		if (mc != null) {
			mc.visualize();
			try {
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(OvenHATest.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

}
