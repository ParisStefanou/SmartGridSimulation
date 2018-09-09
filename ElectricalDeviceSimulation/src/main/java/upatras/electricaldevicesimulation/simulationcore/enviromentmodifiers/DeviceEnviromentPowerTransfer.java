/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentmodifiers;

import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalModifier;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;

/**
 *
 * @author Paris
 */
public class DeviceEnviromentPowerTransfer extends AbstractEnvironmentalModifier {
	
	AbstractElectricalDevice device;
	
	boolean debug = false;
	
	public DeviceEnviromentPowerTransfer(AbstractElectricalDevice device) {
		
		super(device.simulationinstance);
		
		this.device = device;
		this.name = id + " PT : " + device.name + " to " + device.enviroment.name;
		dependsOn(device);
		isDependedUpon(device.enviroment);
	}
	
	@Override
	public void run(TimeStep simulation_step) {
		PowerEnviroment to = device.enviroment;
		
		if (debug) {
			System.out.println("---------------------------------------------------------------");
			System.out.println("transfering consumption " + device.getAveragePowerConsumed().getRealAsDouble() + " to enviroment");
			System.out.println("transfering production " + device.getAveragePowerProduced().getRealAsDouble() + " to enviroment");
		}
		to.addConsumedPower(device.getAveragePowerConsumed());
		to.addProducedPower(device.getAveragePowerProduced());
		
	}
	
}
