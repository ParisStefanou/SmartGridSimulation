/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration;

import org.joda.time.Duration;
import org.junit.Assert;
import org.junit.Test;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.Model;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.simulationmodels.models.SimpleHouse;

/**
 *
 * @author Paris
 */
public class ScalingTest {

	public ScalingTest() {
	}

	/**
	 * Test of processCommand method, of class PhotovoltaicHA.
	 */
	@Test
	public void scaleTest() {
		long houses = 10000;

		long start = System.currentTimeMillis();

		DeviceSimulationInstance in1 = new DeviceSimulationInstance();

		System.out.println("Generating Devices");
		Model housecomplex = new Model(in1);

		BasicEnviroment housecomplexenv = new BasicEnviroment("House Complex", in1);

		for (int i = 0; i < houses; i++) {
			SimpleHouse house = new SimpleHouse("House" + i, housecomplexenv);
			housecomplex.addContainedTask(house);
				if (i >= houses / 10.0) {
					if (i % (houses / 10.0) == 0) {
						System.out.println("Generation progress : " + i / (houses / 100.0) + " %");
					}
				}
		}
		System.out.println("Generated " + "100 % of the devices\n");

		long dur = System.currentTimeMillis() - start;

		Assert.assertTrue(dur < 5000);

		System.out.println("took less than 1 second\n");

		in1.setStepDuration(Duration.standardMinutes(1))
				.setRunMode(DeviceSimulationInstance.RunMode.parallel)
				.advanceDuration(Duration.standardMinutes(5));

		long start2 = System.currentTimeMillis();

		long dur2 = System.currentTimeMillis() - start2;

		Assert.assertTrue(dur2 < 30000/12);

		System.out.println("Finished simulationtook less than 30 seconds\n");

	}
}
