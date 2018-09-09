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
public class RepeatingTimeEvent extends TimeEvent {

	Duration event_interval;

	public RepeatingTimeEvent(AbstractAgent origin, Population target, DateTime event_time, Duration event_interval) {
		super(origin, target, event_time);
		this.event_interval = event_interval;
	}

	public RepeatingTimeEvent(String name, AbstractAgent origin, Population target, DateTime event_time, Duration event_interval) {
		super(name, origin, target, event_time);
		this.event_interval = event_interval;
	}

	@Override
	public void preprocessing() {
		origin.asi.submitEvent(new RepeatingTimeEvent(name, origin, targets, event_time.plus(event_interval), event_interval));
	}

}
