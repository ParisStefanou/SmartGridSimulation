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
public class GasPowerPlant extends PowerPlant {

	public GasPowerPlant(ComplexPower initial_power, ComplexPower max_power, PowerEnviroment enviroment) {
		super(initial_power, max_power, max_power.dividedBy((20.0 * 60 * 1000), (20.0 * 60 * 1000)), enviroment);
	}

}
