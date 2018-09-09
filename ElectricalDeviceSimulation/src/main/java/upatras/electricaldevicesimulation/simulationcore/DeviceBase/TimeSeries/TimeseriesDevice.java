/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.DeviceBase.TimeSeries;

import javafx.util.Pair;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.utilitylibrary.library.dataset.Dataset;
import upatras.utilitylibrary.library.visualization.Visualizable;

import javax.swing.*;

/**
 *
 * @author Paris
 */
public abstract class TimeseriesDevice extends AbstractElectricalDevice implements Visualizable {

	final protected Dataset dataset;

	protected DateTime start_location;
	protected DateTime current_location;
	final protected Duration period;
	final protected Duration step;
	boolean running = false;

	public TimeseriesDevice(PowerEnviroment enviroment, Dataset dataset, Duration step) {
		super(enviroment);
		this.dataset = dataset;
		this.step = step;
		this.period = new Duration(step.getMillis() * dataset.length);
	}

	protected Pair<Long, Double> durationToSteps(Duration d) {
		long steps = d.getMillis() / step.getMillis();
		Double remainder = (d.getMillis() % step.getMillis()) * 1.0 / step.getMillis();

		return new Pair<>(steps, remainder);
	}

	@Override
	public void processCommand(Command c) {

		if (c.name.equals("Start")) {
			running = true;
			start_location = c.time;
			current_location = new DateTime(c.time);
		}
		if (c.name.equals("Stop")) {
			running = false;
		}
		if (c.name.equals("Wait")) {
			running = false;
		}

	}

	@Override
	public JFrame visualize() {

		return dataset.visualize();
	}

	@Override
	public JPanel getPanel() {

		return dataset.getPanel();
	}

	@Override
	public JFrame compressedVisualize() {
		return dataset.compressedVisualize();
	}

	@Override
	public JPanel getCompressedPanel() {
		return dataset.getCompressedPanel();
	}

}
