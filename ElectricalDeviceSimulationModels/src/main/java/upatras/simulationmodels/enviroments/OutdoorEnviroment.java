/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.enviroments;

import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.TemperatureVariable;
import upatras.utilitylibrary.library.measurements.types.Area;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.types.Temperature;

/**
 *
 * @author valitsa-syros
 */
public class OutdoorEnviroment extends BasicEnviroment {

	public OutdoorEnviroment(String name, BasicEnviroment parent, Temperature temperature) {
		super(name, parent, Volume.cubicMeters(Double.MAX_VALUE),temperature==null? Temperature.degreecelsius(27):temperature, Double.MAX_VALUE,null,new Area(Double.MAX_VALUE));
	}

	public OutdoorEnviroment(String name, DeviceSimulationInstance instance, Temperature temperature) {
		super(name, instance,  Volume.cubicMeters(Double.MAX_VALUE), temperature==null? Temperature.degreecelsius(27):temperature,Double.MAX_VALUE);
	}

}
