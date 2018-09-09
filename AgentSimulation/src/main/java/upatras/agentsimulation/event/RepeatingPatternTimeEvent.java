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
public class RepeatingPatternTimeEvent extends PatternTimeEvent {

	Duration pattern_interval;

	public RepeatingPatternTimeEvent(AbstractAgent origin, Population target, DateTime start_time, Duration[] pattern, Duration repeat_time) {
		super(origin, target, start_time, pattern);
		this.pattern_interval = repeat_time;
	}

	public RepeatingPatternTimeEvent(String name, AbstractAgent origin, Population target, DateTime start_time, Duration[] pattern, Duration repeat_time) {
		super(name, origin, target, start_time, pattern);
		this.pattern_interval = repeat_time;
	}

	@Override
	public void preprocessing() {

		DateTime inter_time = event_time;

		for (Duration d : pattern) {
			if (d != null) {
				inter_time = inter_time.plus(d);
				origin.asi.submitEvent(new TimeEvent(name, origin, targets, inter_time));
			}
		}
		origin.asi.submitEvent(new RepeatingPatternTimeEvent(name, origin, targets, event_time.plus(pattern_interval), pattern, pattern_interval));
	}

}
