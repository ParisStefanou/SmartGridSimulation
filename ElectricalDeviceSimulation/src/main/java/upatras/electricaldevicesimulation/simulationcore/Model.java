/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore;

import upatras.automaticparallelism.exceptions.CircularDependencyException;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.Guis.visualization.Visualizer;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractSimulatableDevice;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.SimulationTask;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironment;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalModifier;
import upatras.utilitylibrary.library.Exceptions.InvalidObjectException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paris
 */
public class Model extends SimulationTask {

    public List<SimulationTask> contained_tasks = new ArrayList<>();

    public Model(DeviceSimulationInstance instance) {
        super(instance);
    }

    final public SimulationTask addContainedTask(SimulationTask task) {
        if(task ==this){
            try {
                throw new CircularDependencyException();
            } catch (CircularDependencyException ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        contained_tasks.add(task);
        dependsOn(task);
        
        return this;
    }

    final public List<AbstractEnvironment> getEnviroments() {
        ArrayList<AbstractEnvironment> toreturn = new ArrayList<>();

        for (SimulationTask t : contained_tasks) {
            if (AbstractEnvironment.class.isAssignableFrom(t.getClass())) {
                toreturn.add((AbstractEnvironment) t);
            }
        }

        return toreturn;
    }

    final public List<Model> getModels() {
        ArrayList<Model> toreturn = new ArrayList<>();

        for (SimulationTask t : contained_tasks) {
            if (Model.class.isAssignableFrom(t.getClass())) {
                toreturn.add((Model) t);
            }
        }

        return toreturn;
    }

    final public List<AbstractSimulatableDevice> getDevices() {
        ArrayList<AbstractSimulatableDevice> toreturn = new ArrayList<>();

        for (SimulationTask t : contained_tasks) {
            if (AbstractSimulatableDevice.class.isAssignableFrom(t.getClass())) {
                toreturn.add((AbstractSimulatableDevice) t);
            }
        }

        return toreturn;
    }

    final public void addvalidobject(Object object) throws InvalidObjectException {
        if (AbstractEnvironment.class.isAssignableFrom(object.getClass())) {
            addContainedTask((AbstractEnvironment) object);
        } else if (AbstractSimulatableDevice.class.isAssignableFrom(object.getClass())) {
            addContainedTask((AbstractSimulatableDevice) object);
        } else if (Model.class.isAssignableFrom(object.getClass())) {
            addContainedTask((Model) object);
        } else if (AbstractEnvironmentalModifier.class.isAssignableFrom(object.getClass())) {
            addContainedTask((AbstractEnvironmentalModifier) object);
        } else if (Visualizer.class.isAssignableFrom(object.getClass())) {
            addContainedTask((Visualizer) object);
        } else {
            throw new InvalidObjectException();
        }

    }

    @Override
    public void run(TimeStep simulation_step) {

    }

}
