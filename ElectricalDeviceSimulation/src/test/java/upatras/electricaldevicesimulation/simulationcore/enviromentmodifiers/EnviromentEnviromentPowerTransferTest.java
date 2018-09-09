/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentmodifiers;

import org.joda.time.DateTime;
import org.junit.Test;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerConsumptionVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerProductionVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerVariable;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Paris
 */
public class EnviromentEnviromentPowerTransferTest {

	public EnviromentEnviromentPowerTransferTest() {
	}

	/**
	 * Test of run method, of class DeviceEnviromentPowerTransfer.
	 */
	@Test
	public void testRun() {
		System.out.println("run");
		DeviceSimulationInstance in1 = new DeviceSimulationInstance(DateTime.now());

		PowerEnviroment p1 = new PowerEnviroment(in1);

		PowerEnviroment p2 = new PowerEnviroment(p1);

		new AbstractElectricalDevice(p2) {
			@Override
			protected void processCommand(Command c) {
			}

			@Override
			public void simulate(TimeStep simulation_step) {
				consumePower(new ComplexPower(40, 0));
			}
		};
		
		new AbstractElectricalDevice(p2) {
			@Override
			protected void processCommand(Command c) {
			}

			@Override
			public void simulate(TimeStep simulation_step) {
				consumePower(new ComplexPower(20, 0));
			}
		};
		
		new AbstractElectricalDevice(p2) {
			@Override
			protected void processCommand(Command c) {
			}

			@Override
			public void simulate(TimeStep simulation_step) {
				producePower(new ComplexPower(30, 0));
			}
		};

		in1.setRunMode(DeviceSimulationInstance.RunMode.parallel).advanceStep();

		assertEquals(((PowerConsumptionVariable) p1.getVariable(PowerConsumptionVariable.class)).get().getRealAsDouble(), 60, 0.1);
		assertEquals(((PowerProductionVariable) p1.getVariable(PowerProductionVariable.class)).get().getRealAsDouble(), 30, 0.1);
		assertEquals(((PowerVariable) p1.getVariable(PowerVariable.class)).get().getRealAsDouble(), 30, 0.1);

	}

}
