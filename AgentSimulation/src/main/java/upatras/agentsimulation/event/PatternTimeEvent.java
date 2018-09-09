/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.event;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.agent.Population;

/**
 *
 * @author Paris
 */
public class PatternTimeEvent extends TimeEvent {

	Duration[] pattern;

	public PatternTimeEvent(AbstractAgent origin, Population target, DateTime event_time, Duration[] pattern) {
		super(origin, target, event_time);
		this.pattern=pattern;
	}
	
	public PatternTimeEvent(String name, AbstractAgent origin, Population target, DateTime event_time, Duration[] pattern) {
		super(name, origin, target, event_time);
		this.pattern = pattern;
	}

	@Override
	public void preprocessing() {
		for (Duration d : pattern) {
			origin.asi.submitEvent(new TimeEvent(name, origin, targets, event_time.plus(d)));
		}
	}

}
