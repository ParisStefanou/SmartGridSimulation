/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.event;

import org.joda.time.DateTime;
import upatras.agentsimulation.agent.AbstractAgent;

/**
 *
 * @author Paris
 */
public class SelfEvent extends Event {

	/**
	 * A self event is fired from agent to himself to hop between behaviors
	 *
	 * @param self the agent that starts the event and is the target of the
	 * event
	 * @param event_time Timestamp of the event occurance
	 */
	public SelfEvent(AbstractAgent self, DateTime event_time) {
		super(self, self.toPopulation(), event_time);
	}

	/**
	 * A self event is fired from agent to himself to hop between behaviors
	 *
	 * @param name name of the event
	 * @param self the agent that starts the event and is the target of the
	 * event
	 * @param event_instant Timestamp of the event occurance
	 */
	public SelfEvent(String name, AbstractAgent self, DateTime event_instant) {
		super(name, self, self.toPopulation(), event_instant);
	}

}
