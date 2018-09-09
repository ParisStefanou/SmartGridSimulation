/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentbase;

import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.SimulationTask;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author valitsa-syros
 */
public abstract class AbstractEnvironment extends SimulationTask {

	public AbstractEnvironment parent = null;
	public List<AbstractEnvironment> children = new ArrayList<AbstractEnvironment>();

	ConcurrentHashMap<String, AbstractEnvironmentalVariable> enviroment_variables = new ConcurrentHashMap<>();
	
	public AbstractEnvironment(DeviceSimulationInstance instance) {
		super(instance);

	}

	public AbstractEnvironment(AbstractEnvironment parent) {

		super(parent.simulationinstance);
		this.parent = parent;
		parent.children.add(this);
	}

	@Override
	final public String printdata() {
		String s = "";
		for (Entry<String, AbstractEnvironmentalVariable> e : enviroment_variables.entrySet()) {
			s += "Key : " + e.getKey() + " , Value : " + e.getValue() + "\n";
		}
		return s;
	}

	final public void attachVariable(AbstractEnvironmentalVariable variable) {
		attachVariable(variable.getClass().getSimpleName(), variable);
	}

	final public void attachVariable(String name, AbstractEnvironmentalVariable variable) {
		enviroment_variables.put(name, variable);
		variable.simulationinstance = simulationinstance;
		variable.parent_enviroment = this;
	}

	final public AbstractEnvironmentalVariable getVariable(String name) {

		AbstractEnvironmentalVariable toreturn = enviroment_variables.get(name);
		if (toreturn == null) {
			try {
				throw new Exception("Enviroment:" + this.name + " didnt have variable with name: " + name);
			} catch (Exception ex) {
				Logger.getLogger(AbstractEnvironment.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return toreturn;
	}

	final public AbstractEnvironmentalVariable getVariable(Class c) {

		AbstractEnvironmentalVariable toreturn = enviroment_variables.get(c.getSimpleName());
		if (toreturn == null) {
			try {
				throw new Exception("Enviroment:" + this.name + " didnt have variable of type " + c.getSimpleName());
			} catch (Exception ex) {
				Logger.getLogger(AbstractEnvironment.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return toreturn;
	}

	@Override
	public void postprocess(TimeStep simulation_step) {
		super.postprocess(simulation_step);
		for (AbstractEnvironmentalVariable ev : enviroment_variables.values()) {
			ev.notifyListeners(simulation_step.middle_point);
		}
	}
}
