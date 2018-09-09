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
public abstract class OrderableEvent extends Event implements Comparable<OrderableEvent> {

	/**
     * An orderable event is an event that can be placed in a specific place in
     * an execution queue based on the type of ordering , see the subclasses for
     * examples. The name of the event will be automatically generated.
     *
     * @param origin Agent causing the event
     * @param target Agent receiving the event
     * @param event_time Timestamp of the event's occurance
     */
   
	
	public OrderableEvent(AbstractAgent origin, Population target, DateTime event_time) {
		super(origin, target, event_time);
	}

	
	
    /**
     * An orderable event is an event that can be placed in a specific place in
     * an execution queue based on the type of ordering , see the subclasses for
     * examples
     *
     * @param name A name based on which behaviors will activate on Agents
     * @param origin Agent causing the event
     * @param target Agent receiving the event
     * @param event_time Timestamp of the event's occurance
     */
    public OrderableEvent(String name, AbstractAgent origin, Population target,DateTime event_time) {
        super(name, origin, target,event_time);
    }

    /**
     * Should return 1 if this event should take place before the event it is
     * being compared to
     *
     * @param event the event being compared to
     * @return
     */
    @Override
    public abstract int compareTo(OrderableEvent event);

	/**
	 * Allow the event to be delivered in parallel to the targets
	 *
	 * @return
	 */
	@Override
	public OrderableEvent enableParrallel() {
		this.is_parallel = true;
		return this;
	}

	/**
	 * Don't wait for all the responses from the target and instead continue to
	 * the next event
	 *
	 * @return
	 */
	@Override
	public OrderableEvent enableNonBlocking() {
		this.is_blocking = false;
		return this;
	}

}
