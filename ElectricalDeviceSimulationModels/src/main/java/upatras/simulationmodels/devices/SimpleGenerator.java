/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices;

import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.simulationmodels.devices.basic.SimpleLoad;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class SimpleGenerator extends SimpleLoad {

	public SimpleGenerator(PowerEnviroment enviroment, Power power, double cosf) {
		super(enviroment, new ComplexPower(power.value * cosf, power.value * Math.sin(Math.acos(cosf))));
	}

}
