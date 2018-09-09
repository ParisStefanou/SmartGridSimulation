/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.combinedsimulation;

import org.joda.time.DateTime;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.agent.ChangeListeningAgent;
import upatras.agentsimulation.agent.Population;
import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalVariable;
import upatras.utilitylibrary.library.measurements.Measurement;

/**
 *
 * @author Paris
 */
public class EnviromentVariableChangeListeningAgent<T extends AbstractEnvironmentalVariable, M extends Measurement> extends ChangeListeningAgent<T> {
	
	public Population subscribers = new Population();
	public final String event_name;
	public EnviromentVariableChangeListeningAgent(String event_name,AgentSimulationInstance asi, T variable) {
		super(asi, variable);
		this.event_name=event_name;
		variable.addListener(this);
	}
	
	public void subscribe(AbstractAgent agent){
		subscribers.add(agent);
	}
	
	@Override
	public void itemchanged(DateTime instant) {
		
		Measurement data = ( changing_item).getMeasurement(instant);
		EnviromentalVariableUpdateEvent<M> variable_changed_event = new EnviromentalVariableUpdateEvent(event_name, data, this, subscribers, data.time);
		asi.submitEvent(variable_changed_event);
	}
	
}
