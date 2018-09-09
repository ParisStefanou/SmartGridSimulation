/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentbase;

import upatras.electricaldevicesimulation.simulationcore.DeviceBase.SimulationTask;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;

/**
 *
 * @author valitsa-syros
 */
public abstract class AbstractEnvironmentalModifier extends SimulationTask {
  
    public AbstractEnvironmentalModifier(DeviceSimulationInstance instance) {
        super(instance);
    }
  

}
