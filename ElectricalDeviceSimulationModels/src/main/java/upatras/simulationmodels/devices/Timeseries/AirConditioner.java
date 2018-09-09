/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.Timeseries;

import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.Model;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironment;

/**
 *
 * @author Paris
 */
public class AirConditioner extends Model{
    
    public AirConditioner(DeviceSimulationInstance instance,AbstractEnvironment freezing_enviroment,AbstractEnvironment heating_enviroment) {
        super(instance);
    }
    
}
