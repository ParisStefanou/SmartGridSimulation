/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.Guis.visualization;

import upatras.electricaldevicesimulation.simulationcore.DeviceBase.SimulationTask;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;

/**
 *
 * @author Paris
 */
public abstract class Visualizer extends SimulationTask{
    
    public Visualizer(DeviceSimulationInstance instance) {
        super(instance);
    }
    
}
