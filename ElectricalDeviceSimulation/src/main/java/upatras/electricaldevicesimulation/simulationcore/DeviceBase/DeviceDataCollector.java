/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.DeviceBase;

import org.joda.time.DateTime;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalModifier;
import upatras.utilitylibrary.library.changelistener.ChangingMeasurableItem;
import upatras.utilitylibrary.library.changelistener.SimulationChangeListener;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemCollector;
import upatras.utilitylibrary.library.measurements.Measurement;

import java.util.ArrayList;

/**
 * @author Paris
 */
public abstract class DeviceDataCollector extends AbstractEnvironmentalModifier implements ChangingMeasurableItem {

    AbstractElectricalDevice device;
    public MeasurableItemCollector powercollector;
    boolean debug = false;
    Measurement measurement;

    public DeviceDataCollector(AbstractElectricalDevice device) {
        super(device.simulationinstance);

        this.device = device;
        this.name = id + " Data Collector : " + device.name;
        dependsOn(device);

        powercollector = new MeasurableItemCollector(this);
    }

    ArrayList<SimulationChangeListener> listeners = new ArrayList<>();

    @Override
    public void notifyListeners(DateTime instant) {
        for (SimulationChangeListener l : listeners) {
            l.itemchanged(instant);
        }
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
    public Measurement getMeasurement(DateTime time) {
        return measurement;
    }
}