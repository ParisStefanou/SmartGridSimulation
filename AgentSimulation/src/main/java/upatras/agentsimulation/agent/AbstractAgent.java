/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.agent;

import org.joda.time.DateTime;
import upatras.agentsimulation.behaviour.AbstractBehaviour;
import upatras.agentsimulation.behaviour.BehaviourAttachedTwiceException;
import upatras.agentsimulation.event.Event;
import upatras.agentsimulation.event.OrderableEvent;
import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.utilitylibrary.library.changelistener.SimulationChangeListener;
import upatras.utilitylibrary.library.changelistener.SimulationChangingItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Paris
 */
public abstract class AbstractAgent implements SimulationChangingItem {

    private ArrayList<SimulationChangeListener> listeners = new ArrayList<>();

    /**
     * An instance of AgentSimulationInstance that will be used for event times
     * , submitting events etc.
     */
    public final AgentSimulationInstance asi;

    private static long idcounter = 0;

    /**
     * Unique identifier
     */
    public final long id;

    /**
     * A name for debugging and visualization purposes
     */
    public String name;

    /**
     * All the behavior's that this agent will exert The key is an event that
     * will prompt the use of a behavior
     */
    public HashMap<String, LinkedList<AbstractBehaviour>> behaviors = new HashMap<>();

    /**
     * All the states this agent is in
     */
    public HashMap<String, Object> states = new HashMap<>();

    public boolean debug = false;

    /**
     *
     */
    private ReentrantReadWriteLock datalock = new ReentrantReadWriteLock(true);

    /**
     * An agent is an entity that follows specific behaviors and responds to
     * events of the environment This is the base class which all other agent
     * categories should extend
     *
     * @param asi An instance of AgentSimulationInstance
     */
    public AbstractAgent(AgentSimulationInstance asi) {
        this.asi = asi;
        id = idcounter;
        idcounter++;
        name = this.getClass().getSimpleName() + "-" + id;
    }

    /**
     * Attaches a behavior that will be activated when a specific event happens
     *
     * @param eventExample name of the event to catch
     * @param behavior     behavior to activate
     */
    public final void attachBehaviour(Class eventExample, AbstractBehaviour behavior) {
        String event_name = eventExample.getSimpleName();
        LinkedList<AbstractBehaviour> behavioursforevent = behaviors.get(event_name);
        if (behavioursforevent == null) {
            behavioursforevent = new LinkedList<>();
            behaviors.put(event_name, behavioursforevent);
            behavioursforevent.add(behavior);
        } else {
            if (behavioursforevent.contains(behavior)) {
                try {
                    throw new BehaviourAttachedTwiceException();
                } catch (BehaviourAttachedTwiceException ex) {
                    Logger.getLogger(AbstractAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            behavioursforevent.add(behavior);
        }
    }

    /**
     * Attaches a behavior that will be activated when a specific event happens
     *
     * @param event_name name of the event to catch
     * @param behavior   behavior to activate
     */
    public final void attachBehaviour(String event_name, AbstractBehaviour behavior) {
        LinkedList<AbstractBehaviour> behavioursforevent = behaviors.get(event_name);
        if (behavioursforevent == null) {
            behavioursforevent = new LinkedList<>();
            behaviors.put(event_name, behavioursforevent);
            behavioursforevent.add(behavior);
        } else {
            if (behavioursforevent.contains(behavior)) {
                try {
                    throw new BehaviourAttachedTwiceException();
                } catch (BehaviourAttachedTwiceException ex) {
                    Logger.getLogger(AbstractAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            behavioursforevent.add(behavior);
        }
    }

    /**
     * Removes a behavior that will be activated when a specific event happens
     * If the behavior wasn't attached to that event throws a
     * BehaviourDidntExistException
     *
     * @param eventname event to search for
     * @param behavior  behavior to activate
     */
    public final void detachBehaviour(String eventname, AbstractBehaviour behavior) {
        LinkedList<AbstractBehaviour> behavioursforevent = behaviors.get(eventname);
        if (behavioursforevent != null) {
            behavioursforevent.remove(behavior);
        }
        try {
            throw new BehaviourDidntExistException();
        } catch (BehaviourDidntExistException ex) {
            Logger.getLogger(AbstractAgent.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Clears all attached behaviors
     */
    public void deleteBehaviors() {
        behaviors.clear();
    }

    @Override
    public String toString() {

        return "Agent type:" + getClass().getSimpleName() + " id:" + id;
    }

    public void resolveEvent(Event event) {

        if (behaviors.isEmpty()) {
            return;
        }

        LinkedList<AbstractBehaviour> behaviours_found = behaviors.get(event.name);
        if (behaviours_found != null && !behaviours_found.isEmpty()) {
            for (AbstractBehaviour b : behaviours_found) {
                if (debug) {
                    System.out.println("Behaviour being executed : " + b.toString());
                }
                b.execute(event);
            }
        } else {
            if (debug) {
                System.out.println("The agent "
                        + this + " received the event " + event + " for which it had no attached behaviour");
            }
        }
    }

    private static class BehaviourDidntExistException extends Exception {

        public BehaviourDidntExistException() {
        }
    }

    /**
     * Used for parallel event resolution
     */
    public void acquireForReading() {
        datalock.readLock().lock();
    }

    /**
     * Used for parallel event resolution
     */
    public void acquireForWriting() {
        datalock.writeLock().lock();
    }

    /**
     * Used for parallel event resolution
     */
    public void releaseReader() {
        datalock.readLock().unlock();
    }

    /**
     * Used for parallel event resolution
     */
    public void releaseWriter() {
        datalock.writeLock().unlock();
    }

    @Override
    public void addListener(SimulationChangeListener change_listener) {
        listeners.add(change_listener);
    }

    @Override
    public void removeListener(SimulationChangeListener change_listener) {
        listeners.remove(change_listener);
    }

    @Override
    public void notifyListeners(DateTime instant) {

        for (SimulationChangeListener cl : listeners) {
            cl.itemchanged(instant);
        }

    }

    /**
     * Submits an event to the event_resolver's queue for future execution
     *
     * @param event the event to submit
     * @return
     */
    final public AbstractAgent submitEvent(OrderableEvent event) {
        asi.event_resolver.addEvent(event);
        return this;
    }

    /**
     * Submits a Collection events to the event_resolver's queue for future
     * execution
     *
     * @param events the events to submit
     * @return
     */
    final public AbstractAgent submitEvents(Collection<OrderableEvent> events) {
        asi.event_resolver.addEvents(events);
        return this;
    }

    final public Object getState(String state_name) {
        return states.get(state_name);
    }

    final public Object setState(String state_name, Object data) {
        if (debug) {
            System.out.println("State " + state_name + " was set to " + data);
        }
        return states.put(state_name, data);

    }

    final public Population toPopulation() {
        Population pop = new Population();
        pop.add(this);
        return pop;
    }

    public static long getAgentCount() {
        return idcounter;
    }

}
