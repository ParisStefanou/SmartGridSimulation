/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentbase;

import org.joda.time.DateTime;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
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
 */
public abstract class AbstractEnvironmentalVariable<T extends AbstractUnit> implements ChangingMeasurableItem, Visualizable {

	final public String name;
	protected T unit;
	public final ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock(true);

	public DeviceSimulationInstance simulationinstance;
	public AbstractEnvironment parent_enviroment;

	public AbstractEnvironmentalVariable() {
		this.name = this.getClass().getSimpleName();
	}

	public AbstractEnvironmentalVariable(String name) {
		this.name = name;
	}

	public final T get() {
		T toreturn;
		rwlock.readLock().lock();
		try {
			toreturn = (T) unit.clone();
		} finally {
			rwlock.readLock().unlock();
		}
		return toreturn;

	}


	public final void set(T unit) {
		rwlock.writeLock().lock();
		try {
			this.unit =(T) unit.clone();
		} finally {
			rwlock.writeLock().unlock();
		}

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

		JFrame toreturn = new MeasurableItemExtractor(this).visualize();
		toreturn.setTitle(this.name + " at " + this.parent_enviroment);
		return toreturn;
	}

	@Override
	public JPanel getPanel() {
		return new MeasurableItemExtractor(this).getPanel();
	}

	@Override
	public JFrame compressedVisualize() {
		JFrame toreturn = new MeasurableItemExtractor(this).compressedVisualize();
		toreturn.setTitle(this.name + " at " + this.parent_enviroment);
		return toreturn;
	}

	@Override
	public JPanel getCompressedPanel() {
		return new MeasurableItemExtractor(this).getCompressedPanel();

	}

	@Override
	public String toString() {
		return name + "@" + parent_enviroment.name;
	}

	@Override
	public Measurement getMeasurement(DateTime datetime) {
		return new Measurement(get(),datetime);
	}
}
