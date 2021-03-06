/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.event;

import org.joda.time.DateTime;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.agent.Population;

/**
 *
 * @author Paris
 */
public class TimeEvent<O extends AbstractAgent,T extends AbstractAgent> extends OrderableEvent<O,T> {

	/**
	 * A TimeEvent should be used when the simulations is taking place in real
	 * time
	 *
	 * @param origin Agent causing the event
	 * @param targets Agent population receiving the event
	 * @param event_time timestamp of the event occurance
	 */

	public TimeEvent(O origin, Population<T> targets, DateTime event_time) {
		super(origin, targets, event_time);
	}

	/**
	 * A TimeEvent should be used when the simulations is taking place in real
	 * time
	 *
	 * @param name A name based on which behaviors will activate on Agents
	 * @param origin Agent causing the event
	 * @param targets Agent population receiving the event
	 * @param event_time timestamp of the event occurance
	 */
	public TimeEvent(String name, O origin, Population<T> targets, DateTime event_time) {
		super(name, origin, targets, event_time);
	}

	@Override
	public int compareTo(OrderableEvent e) {
		return event_time.compareTo(((TimeEvent) e).event_time);
	}

}
