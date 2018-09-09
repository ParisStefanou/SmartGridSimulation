/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.DeviceBase;

import org.junit.Test;
import static org.junit.Assert.*;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.utilitylibrary.library.measurements.types.Energy;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class AbstractElectricalStorageDeviceTest {

	public AbstractElectricalStorageDeviceTest() {
	}

	/**
	 * Test of maxAvailableProductionDuration method, of class
	 * AbstractElectricalStorageDevice.
	 */
	@Test
	public void testMaxAvailableConsumptionDuration() {

		DeviceSimulationInstance dsi = new DeviceSimulationInstance();

		PowerEnviroment pe = new BasicEnviroment("whatever", dsi);

		PowerStorageImpl dev = new PowerStorageImpl(new Energy(1000000), new ComplexPower(1000, 1000), new ComplexPower(1000, 1000), pe);

		//battery charging
		assertEquals(1000000 / 1000, dev.maxAvailableConsumptionDuration(new ComplexPower(1000, 0)).getStandardSeconds());
		assertEquals(1000000 / 500, dev.maxAvailableConsumptionDuration(new ComplexPower(500, 0)).getStandardSeconds());

		//battery full
		dev.setCurrentEnergy(new Energy(1000000));

		assertEquals(0, dev.maxAvailableConsumptionDuration(new ComplexPower(1000, 0)).getStandardSeconds());
		assertEquals(0, dev.maxAvailableConsumptionDuration(new ComplexPower(500, 0)).getStandardSeconds());

	}

	/**
	 * Test of maxAvailableConsumptionDuration method, of class
	 * AbstractElectricalStorageDevice.
	 */
	@Test
	public void testMaxAvailableProductionDuration() {
		DeviceSimulationInstance dsi = new DeviceSimulationInstance();

		PowerEnviroment pe = new BasicEnviroment("whatever", dsi);

		PowerStorageImpl dev = new PowerStorageImpl(new Energy(1000000), new ComplexPower(1000, 1000), new ComplexPower(1000, 1000), pe);

		//battery empty
		assertEquals(0, dev.maxAvailableProductionDuration(new ComplexPower(1000, 0)).getStandardSeconds());
		assertEquals(0, dev.maxAvailableProductionDuration(new ComplexPower(500, 0)).getStandardSeconds());

		//battery charged
		dev.setCurrentEnergy(new Energy(1000000));

		assertEquals(1000000 / 1000, dev.maxAvailableProductionDuration(new ComplexPower(1000, 0)).getStandardSeconds());
		assertEquals(1000000 / 500, dev.maxAvailableProductionDuration(new ComplexPower(500, 0)).getStandardSeconds());
	
		//battery half charged
		dev.setCurrentEnergy(new Energy(500000));
	
		assertEquals(500000 / 1000, dev.maxAvailableProductionDuration(new ComplexPower(1000, 0)).getStandardSeconds());
		assertEquals(500000 / 1000, dev.maxAvailableConsumptionDuration(new ComplexPower(1000, 0)).getStandardSeconds());
		
		
	}
}

class PowerStorageImpl extends AbstractElectricalStorageDevice {

	public PowerStorageImpl(Energy max_storable_energy, ComplexPower max_energy_output, ComplexPower max_energy_input, PowerEnviroment enviroment) {
		super(max_storable_energy, max_energy_output, max_energy_input, enviroment);
	}

	@Override
	protected void processCommand(Command c) {
	}

	@Override
	public void simulate(TimeStep simulation_step) {
	}

}
