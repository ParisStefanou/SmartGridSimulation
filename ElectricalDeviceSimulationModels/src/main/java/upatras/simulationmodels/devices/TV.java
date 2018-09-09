/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices;

import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.simulationmodels.devices.basic.VariableLoad;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class TV extends VariableLoad {

	public TV(PowerEnviroment enviroment) {
		super(enviroment, new ComplexPower(200, 0));
	}

}
