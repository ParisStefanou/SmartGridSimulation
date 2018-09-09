/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.Timeseries;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.TimeSeries.TimeseriesDevice;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.utilitylibrary.library.dataset.Dataset;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class TimeSeriesGenerationDevice extends TimeseriesDevice {

	public TimeSeriesGenerationDevice(PowerEnviroment enviroment, Dataset dataset, Duration step) {
		super(enviroment, dataset, step);
	}

	@Override
	public void simulate(TimeStep simulation_step) {
		DateTime step_start = simulationinstance.current_simulation_step.step_start;
		DateTime step_end = simulationinstance.current_simulation_step.step_end;

		if (this.start_location != null && !start_location.isBefore(step_start) && current_location.isBefore(step_end)) {

			Interval interval_to_parse = new Interval(current_location, step_end);
			double steps = interval_to_parse.toDurationMillis() / step_end.getMillis();

			Interval interval_up_to_now = new Interval(start_location, current_location);
			double start_location = interval_up_to_now.toDurationMillis() / step_end.getMillis();

			consumePower(new ComplexPower(dataset.getAreaBeneath(start_location, steps) * step.getMillis() / 1000.0, 0));

		}
	}

}
