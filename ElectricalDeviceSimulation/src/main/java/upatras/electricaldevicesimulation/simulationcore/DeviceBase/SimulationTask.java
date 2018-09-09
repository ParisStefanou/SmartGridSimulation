/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.DeviceBase;

import upatras.automaticparallelism.tasks.DependantTask;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;

/**
 *
 * @author Paris
 */
public abstract class SimulationTask extends DependantTask {

    final public DeviceSimulationInstance simulationinstance;

    public SimulationTask(DeviceSimulationInstance instance) {
        instance.addSimulationTask(this);
        this.simulationinstance = instance;
    }

    public String printdata() {

        return "";
    }

}
