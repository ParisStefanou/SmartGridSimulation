/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.enviroment;

import org.joda.time.DateTime;
import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.utilitylibrary.library.changelistener.ChangingMeasurableItem;
import upatras.utilitylibrary.library.changelistener.SimulationChangeListener;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemExtractor;
import upatras.utilitylibrary.library.measurements.Measurement;
import upatras.utilitylibrary.library.measurements.types.AbstractUnit;
import upatras.utilitylibrary.library.visualization.Visualizable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Paris
 * @param <T>
 */
public abstract class AbstractEnvironmentVariable<T extends AbstractUnit> implements ChangingMeasurableItem, Visualizable {

    final public String name;
    public T unit;
    public final ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock(true);

    public AgentSimulationInstance simulationinstance;
    public AbstractAgentEnvironment parent_enviroment;

    public AbstractEnvironmentVariable() {
        this.name = this.getClass().getSimpleName();
    }

    public AbstractEnvironmentVariable(String name) {
        this.name = name;
    }

    public T getUnit() {
        return unit;
    }

    ArrayList<SimulationChangeListener> listeners = new ArrayList<>();

    /**
     * Notifies all listeners upon a change
     *
     */
    public void notifyListeners(DateTime instant) {
        for (SimulationChangeListener listener : listeners) {
            listener.itemchanged(instant);
        }
    }

    /**
     * A listener can start receiving notifications for changes from this object
     * by calling this function once
     *
     * @param change_listener
     */
    public void addListener(SimulationChangeListener change_listener) {
        listeners.add(change_listener);
    }

    /**
     * A listener can stop receiving notifications for changes from this object
     * by calling this function once
     *
     * @param change_listener
     */
    public void removeListener(SimulationChangeListener change_listener) {
        listeners.remove(change_listener);
    }

    @Override
    public JFrame visualize() {

        return new MeasurableItemExtractor(this).visualize();
    }

    @Override
    public JPanel getPanel() {
        return new MeasurableItemExtractor(this).getPanel();
    }

}
