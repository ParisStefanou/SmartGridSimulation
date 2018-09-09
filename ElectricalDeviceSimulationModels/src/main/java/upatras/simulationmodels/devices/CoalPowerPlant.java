/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices;

import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class CoalPowerPlant extends PowerPlant {

	public CoalPowerPlant(ComplexPower initial_power, ComplexPower max_power, PowerEnviroment enviroment) {
		super(initial_power, max_power, max_power.dividedBy(250.0 * 60 * 1000, 250.0 * 60 * 1000), enviroment);
	}

}
