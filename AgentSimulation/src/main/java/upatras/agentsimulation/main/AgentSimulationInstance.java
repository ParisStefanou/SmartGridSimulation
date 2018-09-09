/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.main;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.agentsimulation.event.Event;
import upatras.agentsimulation.event.EventResolver;
import upatras.agentsimulation.event.OrderableEvent;
import upatras.automaticparallelism.execution.batch.BatchExecutor;
import upatras.automaticparallelism.main.TimeInstance;

import java.util.Collection;

/**
 * @author Paris
 */
public class AgentSimulationInstance extends TimeInstance {

    /**
     * The streaming executor delivers event to their recipients and executes
     * any following logic
     */
    public final BatchExecutor batch_executor = new BatchExecutor();

    /**
     * The EventResolver submits events to the Batch events according to their
     * natural ordering and starts the BatchExecutor when needed
     */
    public final EventResolver event_resolver;

    /**
     * For deugging prints some basic info
     */
    public boolean debug = false;
    Duration step_duration;

    public AgentSimulationInstance(DateTime start_datetime) {
        super(start_datetime);
        event_resolver = new EventResolver(this, false);
    }

    public AgentSimulationInstance(DateTime start_datetime, boolean rb_mode) {
        super(start_datetime);
        event_resolver = new EventResolver(this, true);
    }

    /**
     * This function will run the simulation for a specific duration resolving
     * any event in that timeframe
     *
     * @param duration duration to advance
     */
    @Override
    public void advanceDuration(Duration duration) {

        DateTime target = getPresent().plus(duration);

        event_resolver.resolveAllEventsBeforeDateTime(target);

    }

    /**
     * This function will run until there are no more events inside the event
     * resolver while resolving the events according to the real time
     */
    @Override
    public void advanceRealTime() {

        while (true) {

            DateTime future = DateTime.now();

            event_resolver.resolveAllEventsBeforeDateTime(future);

            if (debug) {

                float GHz = (float) 1.7;

                long SimulationTasknum = event_resolver.event_list.size();

                double stepmillis = (double) (future.getMillis() - getPresent().getMillis());
                System.out.println("\nexec time = " + stepmillis / 1000.0 + " s , SimulationTasks : " + SimulationTasknum + " , steps : " + (1));
                System.out.println("cputime/SimulationTask = " + stepmillis / (SimulationTasknum * (1000000.0)) + " ns");

                System.out.println("cpucycles/SimulationTask = " + stepmillis / (SimulationTasknum * (1000000.0)) * GHz + " cycles");
            }

            setPresent(future);

            if (event_resolver.event_list.isEmpty()) {
                return;
            }
        }

    }

    /**
     * This function will resolve event_count events
     *
     * @param event_count the amount of events to resolve
     */
    public void advanceEventCount(int event_count) {
        for (int i = 0; i < event_count; i++) {
            event_resolver.resolveOne();
        }
    }

    /**
     * This function will resolve up to one event and return that events time or
     * advance the simulation up to the limit parameter
     *
     * @param limit the bound to try to run to
     * @return Either the time of occurance of the first event found or
     * otherwise the limit
     */
    public DateTime advanceOnceBeforeDateTime(DateTime limit) {

        return event_resolver.resolveOneEventBeforeDateTime(limit);

    }

    /**
     * This function will resolve all pending events
     */
    public void advanceToEnd() {
        event_resolver.resolveAll();
    }

    /**
     * Submits an event to the event_resolver's queue for future execution
     *
     * @param event the event to submit
     * @return
     */
    public AgentSimulationInstance submitEvent(OrderableEvent event) {

        if (debug) {
            System.out.println("Event submitted " + event);
        }
        this.event_resolver.addEvent(event);
        return this;
    }

    /**
     * Submits a Collection events to the event_resolver's queue for future
     * execution
     *
     * @param events the events to submit
     * @return
     */
    public AgentSimulationInstance submitEvents(Collection<OrderableEvent> events) {
        if (debug) {
            for (Event event : events) {
                System.out.println("Event submitted " + event);
            }
        }

        event_resolver.addEvents(events);
        return this;
    }

    @Override
    public void shutdown() {
        batch_executor.finalize();
    }

    @Override
    public void advanceStep() {

        DateTime target = getPresent().plus(step_duration);

        event_resolver.resolveAllEventsBeforeDateTime(target);

    }

}
