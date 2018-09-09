/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.enviroments;

import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironment;

/**
 *
 * @author valitsa-syros
 */
public class EmptyEnviroment extends AbstractEnvironment {

    public EmptyEnviroment(DeviceSimulationInstance instance) {
        super(instance);
    }

    public EmptyEnviroment(AbstractEnvironment parent) {
        super(parent);
    }

    @Override
    public void run(TimeStep simulation_step) {
      
    }

}
