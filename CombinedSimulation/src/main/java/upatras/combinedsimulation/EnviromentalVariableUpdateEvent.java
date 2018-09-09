/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.combinedsimulation;

import org.joda.time.DateTime;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.agent.Population;
import upatras.agentsimulation.event.TimeEvent;
import upatras.utilitylibrary.library.measurements.Measurement;

/**
 *
 * @author Paris
 */
public class EnviromentalVariableUpdateEvent<T extends Measurement> extends TimeEvent {
	
	public final T data;
	
	public EnviromentalVariableUpdateEvent(String name,T data, AbstractAgent origin, Population target, DateTime event_time) {
		super(name, origin, target, event_time);
		this.data=data;
	}
	
}
