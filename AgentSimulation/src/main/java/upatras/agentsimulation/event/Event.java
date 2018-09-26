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
public abstract class Event<O extends AbstractAgent,T extends AbstractAgent> {

	/**
	 * The event name should be selected carefully since Agent behaviors are
	 * activated based on matching with this name
	 *
	 */
	public final String name;
	boolean is_parallel = false;
	boolean is_blocking = true;

	/**
	 * DateTime when the event occured
	 *
	 */
	public final DateTime event_time;
	boolean finished;

	/**
	 * Agent that has started this event
	 *
	 */
	public final O origin;

	/**
	 * The population this event will be delivered to
	 *
	 */
	public final Population<T> targets;

	/**
	 * An event is any change in the simulation that could cause a behavior to
	 * activate. The name will be automatically generated.
	 *
	 * @param origin source of the event
	 * @param targets targets of the event
	 * @param event_time DateTime at which the event occured
	 */
	public Event(O origin, Population<T> targets, DateTime event_time) {
		this.name = getClass().getSimpleName();
		this.origin = origin;
		this.targets = targets;
		this.event_time = event_time;
	}

	/**
	 * An event is any change in the simulation that could cause a behavior to
	 * activate
	 *
	 * @param name name of the event , to be used to activate behaviors
	 * @param origin source of the event
	 * @param targets targets of the event
	 * @param event_time DateTime at which the event occured
	 */
	public Event(String name, O origin, Population<T> targets, DateTime event_time) {
		this.name = name;
		this.origin = origin;
		this.targets = targets;
		this.event_time = event_time;
	}

	/**
	 * Allow the event to be delivered in parallel to the targets
	 *
	 * @return
	 */
	public Event enableParrallel() {
		this.is_parallel = true;
		return this;
	}

	/**
	 * Don't wait for all the responses from the target and instead continue to
	 * the next event
	 *
	 * @return
	 */
	public Event enableNonBlocking() {
		this.is_blocking = false;
		return this;
	}

	@Override
	final public int hashCode() {
		return name.hashCode();
	}

	public void preprocessing() {
	}

	public void postprocessing() {
	}

	@Override
	final public boolean equals(Object o) {
		if (o.getClass() == Event.class) {
			return name.equals(((Event) o).name);
		}
		if (o.getClass() == Class.class) {
			return getClass().getSimpleName().equals(((Class) o).getSimpleName());
		}
		if (o.getClass() == String.class) {
			return getClass().getSimpleName().equals(o);
		}
		return false;

	}

	@Override
	public String toString() {
		return name + " occuring at " + event_time + " from " + origin.name;
	}

}
