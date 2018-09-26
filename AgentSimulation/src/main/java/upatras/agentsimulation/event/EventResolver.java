/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.event;

import org.joda.time.DateTime;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.event.priorityqueue.EventBucket;
import upatras.agentsimulation.event.priorityqueue.BucketRBTree;
import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.agentsimulation.visualization.EventRelationGraph;
import upatras.automaticparallelism.execution.batch.Batch;
import upatras.automaticparallelism.execution.batch.BatchThread.Mode;
import upatras.automaticparallelism.main.TimeStep;
import upatras.automaticparallelism.tasks.Task;
import upatras.utilitylibrary.library.visualization.Visualizable;

import javax.swing.*;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.PriorityQueue;

/**
 * @author Paris
 */
public class EventResolver implements Visualizable {

    int prevcount = 10000;
    int currcount;
    final public boolean rb_mode;

    final AgentSimulationInstance asi;

    /**
     * For debugging, prints some basic information
     */
    public boolean debug = false;

    /**
     *
     */
    public EventRelationGraph erg = new EventRelationGraph();

    /**
     * Events currently in queue to be resolved
     */
    public AbstractQueue<TimeEvent> event_list;

    /**
     * This class will order any events submitted to it and start distributing
     * them to the agents event execution type will depend on whether isParallel
     * or isNonBlocking has been called on an event
     *
     * @param asi the AgentSimulationInstance to use for progression
     */
    public EventResolver(AgentSimulationInstance asi, boolean rb_mode) {
        this.asi = asi;
        this.rb_mode = rb_mode;
        if (rb_mode) {
            event_list = new BucketRBTree();
        } else {
            event_list = new PriorityQueue<>();
        }
    }

    /**
     * Adds event to the queue
     *
     * @param event
     */
    synchronized public void addEvent(OrderableEvent event) {
        if (event != null) {
            event_list.add((TimeEvent) event);
            currcount++;

        }
    }

    /**
     * Adds a Collection of events to the queue
     *
     * @param events the events to add
     */
    synchronized public void addEvents(Collection<OrderableEvent> events) {
        for (OrderableEvent oe : events) {
            if (oe != null) {
                event_list.add((TimeEvent) oe);
                currcount++;
            }
        }
    }

    /**
     * Resolves all the events in the queue
     *
     * @return returns the DateTime where the last event occured
     */
    public DateTime resolveAll() {

        while (!event_list.isEmpty()) {
            asi.setPresent(resolveOne());
        }
        return asi.getPresent();

    }

    /**
     * Resolves one event and progresses the simulation up to there or does
     * nothing if no event is found
     *
     * @return
     */
    public DateTime resolveOne() {

        if (event_list.isEmpty()) {
            return asi.getPresent();
        }

        if (!rb_mode) {
            OrderableEvent<AbstractAgent,AbstractAgent> next_event = event_list.remove();

            TimeStep simulation_step;
            if (next_event.event_time != null && next_event.event_time.isAfter(asi.getPresent()))
                simulation_step = new TimeStep(asi.getPresent(), next_event.event_time);
            else {
                simulation_step = new TimeStep(asi.getPresent(), asi.getPresent());
            }

            if (!next_event.targets.isEmpty()) {
                if (debug) {
                    System.out.println("Resolving event: " + next_event);
                }
                Batch b = new Batch();
                erg.addEvent(next_event);

                for (AbstractAgent a : next_event.targets) {
                    b.addTask(new ResolveEventTask(a, next_event));
                }

                next_event.preprocessing();
                if (next_event.is_parallel) {
                    asi.batch_executor.parallelStep(simulation_step, b, Mode.run);
                } else {
                    asi.batch_executor.serialStep(simulation_step, b, Mode.run);
                }
                next_event.postprocessing();

            } else {
                if (debug) {
                    System.out.println("Cannot resolve event : " + next_event + ", the targets were empty");
                }
            }
            asi.setPresent(simulation_step.step_end);

            return simulation_step.step_end;
        } else {
            BucketRBTree wrbtree = (BucketRBTree) event_list;

            EventBucket bucket = wrbtree.removeBucket();

            TimeStep simulation_step = new TimeStep(asi.getPresent(), bucket.middle_time);

            for (TimeEvent<AbstractAgent,AbstractAgent> next_event : bucket) {


                if (!next_event.targets.isEmpty()) {

                    if (debug) {
                        System.out.println("Resolving event using wrbtree: " + next_event);
                    }

                    Batch b = new Batch();
                    erg.addEvent(next_event);

                    for (AbstractAgent a : next_event.targets) {
                        b.addTask(new ResolveEventTask(a, next_event));
                    }
                    next_event.preprocessing();
                    if (next_event.is_parallel) {
                        asi.batch_executor.parallelStep(simulation_step, b, Mode.run);
                    } else {
                        asi.batch_executor.serialStep(simulation_step, b, Mode.run);
                    }
                    next_event.postprocessing();
                } else {
                    if (debug) {
                        System.out.println("Resolving event: " + next_event + " but the targets were empty");
                    }
                }
            }
            asi.setPresent(simulation_step.step_end);

            return simulation_step.step_end;
        }
    }

    /**
     * Resolves all events up to the datetime_limit
     *
     * @param datetime_limit DateTime up to which to solve events
     * @return
     */
    public DateTime resolveAllEventsBeforeDateTime(DateTime datetime_limit) {

        while (event_list.size() > 0 && event_list.peek().event_time.isBefore(datetime_limit)) {
            asi.setPresent(resolveOne());
        }

        asi.setPresent(datetime_limit);
        return datetime_limit;

    }

    /**
     * Resolves at most one event before the limit variable and progresses the
     * simulation to that events time or if no event is found just progresses
     * the simulation to the limit variable
     *
     * @param datetime_limit DateTime up to which to solve events
     * @return
     */
    public DateTime resolveOneEventBeforeDateTime(DateTime datetime_limit) {

        Event next_event = event_list.peek();

        if (next_event != null && next_event.event_time.isBefore(datetime_limit)) {
            asi.setPresent(resolveOne());
            return asi.getPresent();
        } else {
            asi.setPresent(datetime_limit);
            return datetime_limit;
        }
    }

    @Override
    public JFrame visualize() {
        return erg.visualize();
    }

    @Override
    public JPanel getPanel() {
        return erg.getPanel();
    }

    @Override
    public JFrame compressedVisualize() {
        return erg.compressedVisualize();
    }

    @Override
    public JPanel getCompressedPanel() {
        return erg.getCompressedPanel();
    }

    /**
     * Activates the behaviors that are assigned to the event
     */
    private class ResolveEventTask extends Task {

        Event event;
        AbstractAgent agent;

        ResolveEventTask(AbstractAgent agent, Event event) {
            this.event = event;
            this.agent = agent;
        }

        @Override
        public void run(TimeStep simulation_step) {
            agent.resolveEvent(event);

        }
    }
}
