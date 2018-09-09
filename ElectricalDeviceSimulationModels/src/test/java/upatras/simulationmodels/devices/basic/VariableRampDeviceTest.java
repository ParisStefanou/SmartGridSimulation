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
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerProductionVariable;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Paris
 */
public class VariableRampDeviceTest {

	public VariableRampDeviceTest() {
	}

	/**
	 * Test of getRemainingPower method, of class VariableRampDevice.
	 */
	@Test
	public void testRemainingPowerGenerator() {

		DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
		DeviceSimulationInstance dsi = new DeviceSimulationInstance();
		dsi.setPresent(starttime);
		dsi.setStepDuration(Duration.standardSeconds(5));

		System.out.println("Generating Devices");

		PowerEnviroment env = new PowerEnviroment(dsi);
		VariableRampDevice ramp_load = new VariableRampGenerator(new ComplexPower(), new ComplexPower(1000, 1000), new ComplexPower(1.0 / 60, 1.0 / 60), env);

		dsi.advanceDuration(Duration.standardSeconds(5));
		
		//t=5s
		assertEquals(0, ((PowerProductionVariable) env.getVariable(PowerProductionVariable.class)).get().getRealAsDouble(), 0.001);

		ramp_load.increasePower(starttime.plusMinutes(1), new ComplexPower(250, 250));
		dsi.advanceDuration(Duration.standardSeconds(115));
		
		//t=120s
		assertEquals(250, ((PowerProductionVariable) env.getVariable(PowerProductionVariable.class)).get().getRealAsDouble(), 0.001);

		
		ramp_load.increasePower(starttime.plusMinutes(2), new ComplexPower(250, 250));
		
		dsi.advanceDuration(Duration.standardSeconds(60));
		
		//t=180s
		assertEquals(500, ((PowerProductionVariable) env.getVariable(PowerProductionVariable.class)).get().getRealAsDouble(), 0.001);

		
		ramp_load.increasePower(starttime.plusMinutes(3), new ComplexPower(250, 250));
		
		dsi.advanceDuration(Duration.standardSeconds(60));
		
		//t=240s
		assertEquals(750, ((PowerProductionVariable) env.getVariable(PowerProductionVariable.class)).get().getRealAsDouble(), 0.001);

		ramp_load.increasePower(starttime.plusMinutes(4), new ComplexPower(500, 500));
		
		dsi.advanceDuration(Duration.standardSeconds(60));
		
		//t=300s
		assertEquals(1000, ((PowerProductionVariable) env.getVariable(PowerProductionVariable.class)).get().getRealAsDouble(), 0.001);
		
		ramp_load.increasePower(starttime.plusMinutes(5), new ComplexPower(500, 500));
		
		dsi.advanceDuration(Duration.standardSeconds(60));
		
		//t=360s
		assertEquals(1000, ((PowerProductionVariable) env.getVariable(PowerProductionVariable.class)).get().getRealAsDouble(), 0.001);
		
		ramp_load.decreasePower(starttime.plusMinutes(6), new ComplexPower(1000, 1000));
		
		dsi.advanceDuration(Duration.standardSeconds(240));
		
		//t=600s
		assertEquals(0, ((PowerProductionVariable) env.getVariable(PowerProductionVariable.class)).get().getRealAsDouble(), 0.001);

		ramp_load.increasePower(starttime.plusMinutes(10), new ComplexPower(20000, 20000));
		
		dsi.advanceDuration(Duration.standardSeconds(300));
		
		//t=900s
		assertEquals(1000, ((PowerProductionVariable) env.getVariable(PowerProductionVariable.class)).get().getRealAsDouble(), 0.001);

		ramp_load.decreasePower(starttime.plusMinutes(15), new ComplexPower(20000, 20000));
		
		dsi.advanceDuration(Duration.standardSeconds(300));
		
		//t=1200s
		assertEquals(0, ((PowerProductionVariable) env.getVariable(PowerProductionVariable.class)).get().getRealAsDouble(), 0.001);

		dsi.shutdown();
	}
	/**
	 * Test of getRemainingPower method, of class VariableRampDevice.
	 */
	@Test
	public void testRemainingPowerMotor() {

		DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
		DeviceSimulationInstance dsi = new DeviceSimulationInstance();
		dsi.setPresent(starttime);
		dsi.setStepDuration(Duration.standardSeconds(5));

		System.out.println("Generating Devices");

		PowerEnviroment env = new PowerEnviroment(dsi);
		VariableRampGenerator ramp_load = new VariableRampGenerator(new ComplexPower(), new ComplexPower(1000, 1000), new ComplexPower(1.0 / 60, 1.0 / 60), env);
		ramp_load.changeToMotor();

		dsi.advanceDuration(Duration.standardSeconds(5));

		//t=5s
		assertEquals(0, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 0.001);

		ramp_load.increasePower(starttime.plusMinutes(1), new ComplexPower(250, 250));
		dsi.advanceDuration(Duration.standardSeconds(115));

		//t=120s
		assertEquals(250, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 0.001);


		ramp_load.increasePower(starttime.plusMinutes(2), new ComplexPower(250, 250));

		dsi.advanceDuration(Duration.standardSeconds(60));

		//t=180s
		assertEquals(500, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 0.001);


		ramp_load.increasePower(starttime.plusMinutes(3), new ComplexPower(250, 250));

		dsi.advanceDuration(Duration.standardSeconds(60));

		//t=240s
		assertEquals(750, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 0.001);

		ramp_load.increasePower(starttime.plusMinutes(4), new ComplexPower(500, 500));

		dsi.advanceDuration(Duration.standardSeconds(60));

		//t=300s
		assertEquals(1000, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 0.001);

		ramp_load.increasePower(starttime.plusMinutes(5), new ComplexPower(500, 500));

		dsi.advanceDuration(Duration.standardSeconds(60));

		//t=360s
		assertEquals(1000, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 0.001);

		ramp_load.decreasePower(starttime.plusMinutes(6), new ComplexPower(1000, 1000));

		dsi.advanceDuration(Duration.standardSeconds(240));

		//t=600s
		assertEquals(0, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 0.001);

		ramp_load.increasePower(starttime.plusMinutes(10), new ComplexPower(20000, 20000));

		dsi.advanceDuration(Duration.standardSeconds(300));

		//t=900s
		assertEquals(1000, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 0.001);

		ramp_load.decreasePower(starttime.plusMinutes(15), new ComplexPower(20000, 20000));

		dsi.advanceDuration(Duration.standardSeconds(300));

		//t=1200s
		assertEquals(0, ((PowerConsumptionVariable) env.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 0.001);

		dsi.shutdown();
	}

}
