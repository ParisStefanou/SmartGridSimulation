/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.DeviceBase;

import org.joda.time.DateTime;
import upatras.automaticparallelism.main.TimeStep;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemCollector;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemExtractor;
import upatras.utilitylibrary.library.measurements.Measurement;

/**
 *
 * @author Paris
 */
public class DevicePowerTotalCollector extends DeviceDataCollector {

	public static MeasurableItemCollector getCollector(AbstractElectricalDevice device) {

		DevicePowerTotalCollector device_collector = new DevicePowerTotalCollector(device);
		return new MeasurableItemCollector(device_collector);

	}

	public static MeasurableItemExtractor getExtractor(AbstractElectricalDevice device) {

		DevicePowerTotalCollector device_collector = new DevicePowerTotalCollector(device);
		return new MeasurableItemExtractor(device_collector);

	}

	public DevicePowerTotalCollector(AbstractElectricalDevice device) {
		super(device);
	}

	@Override
	public void run(TimeStep simulation_step) {
		measurement = new Measurement(device.getAveragePowerConsumed().minus(device.getAveragePowerProduced()), simulation_step.middle_point);
		notifyListeners(simulation_step.middle_point);
	}

	@Override
	public Measurement getMeasurement(DateTime time) {
		return measurement;
	}
}
