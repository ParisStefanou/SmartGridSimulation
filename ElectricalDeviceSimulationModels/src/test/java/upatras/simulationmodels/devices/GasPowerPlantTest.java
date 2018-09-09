/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerProductionVariable;
import upatras.simulationmodels.devices.highaccuracy.OvenHATest;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemCollector;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 *
 * @author Paris
 */
public class GasPowerPlantTest {
	
	public GasPowerPlantTest() {
	}

	@Test
	public void testSomeMethod() {
	
		DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
		DeviceSimulationInstance dsi = new DeviceSimulationInstance();
		dsi.setPresent(starttime);
		dsi.setStepDuration(Duration.standardMinutes(1));

		System.out.println("Generating Devices");

		PowerEnviroment env = new PowerEnviroment(dsi);
		GasPowerPlant ramp_load = new GasPowerPlant(new ComplexPower(), new ComplexPower(1000000, 1000000), env);

		MeasurableItemCollector mc = null;
		if (false) {
			mc = new MeasurableItemCollector(env.getVariable(PowerProductionVariable.class));
		}

		
		
		dsi.advanceDuration(Duration.standardMinutes(10));

		
		//t=10m
		assertEquals(0, ramp_load.getAveragePowerProduced().getRealAsDouble(), 1);

		ramp_load.increasePower(starttime.plusMinutes(10), new ComplexPower(1000000, 1000000));
		
		
		dsi.advanceDuration(Duration.standardHours(1));
		
		//t=1h10m
		assertNotEquals(0, ramp_load.getAveragePowerProduced().getRealAsDouble(), 1);

		dsi.advanceDuration(Duration.standardHours(1));
		
		//t=2h10m
		assertEquals(1000000, ramp_load.getAveragePowerProduced().getRealAsDouble(), 1);
		
		ramp_load.decreasePower(starttime.plusHours(2).plusMinutes(10), ramp_load.getCurrentPower());
		
		dsi.advanceDuration(Duration.standardMinutes(5));
		
		//t=2h15m
		assertTrue(ramp_load.getAveragePowerProduced().getRealAsDouble()<1000000);
		
		if (mc != null) {
			mc.visualize();
			try {
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(OvenHATest.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		dsi.shutdown();
	}
	
}
