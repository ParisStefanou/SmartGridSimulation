/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.basic;

import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class RandomLoad extends SimpleLoad {

	public RandomLoad(PowerEnviroment enviroment, double power_watt) {
		super(enviroment, new ComplexPower(power_watt, 0));
	}

	@Override
	public void simulate(TimeStep simulation_step) {

		if (on) {
			consumePower(new ComplexPower(Math.random() * max_power.getRealAsDouble(), 0));
		}

	}

}
