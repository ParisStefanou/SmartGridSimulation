/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements;

import org.joda.time.DateTime;
import upatras.utilitylibrary.library.changelistener.ChangingMeasurableItem;
import upatras.utilitylibrary.library.changelistener.SimulationChangeListener;
import upatras.utilitylibrary.library.visualization.Visualizable;

import java.util.ArrayList;

/**
 *
 * @author Paris
 * @param <T>
 */
public abstract class DataExtractor<T extends Measurement> implements  ChangingMeasurableItem,SimulationChangeListener, Visualizable {

    /**The latest Measurement extracted
     *
     */
    public T measurement = null;

    /**
     * Notifies all listeners upon a change with the time the change occured
     *
     * @param instant The DateTime of the change's occurance
     */
    @Override
    public void notifyListeners(DateTime instant) {
        for (SimulationChangeListener cl : listeners) {
            cl.itemchanged(instant);
        }
    }

    private ArrayList<SimulationChangeListener> listeners = new ArrayList<>();

    /**
     * A listener can start receiving notifications for changes from this object
     * by calling this function once
     *
     * @param change_listener
     */
    public void addListener(SimulationChangeListener change_listener) {
        listeners.add(change_listener);
    }

    @Override
    public void removeListener(SimulationChangeListener change_listener) {
        listeners.remove(change_listener);
    }

    @Override
    public Measurement getMeasurement(DateTime time) {
        return measurement;
    }

}
