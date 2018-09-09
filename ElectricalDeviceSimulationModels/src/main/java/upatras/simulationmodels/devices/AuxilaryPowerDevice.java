/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.utilitylibrary.library.measurements.types.Energy;
import upatras.utilitylibrary.library.measurements.types.Power;

/**
 * @author Paris
 */
public interface AuxilaryPowerDevice {

    Power getMaxPowerGeneration();

    Power getMaxPowerConsumption();

    Power getAvailablePowerGeneration(Duration duration);

    Power getAvailablePowerConsumption(Duration duration);

    Duration getAvailableDurationGeneration(Power power);

    Duration getAvailableDurationConsumption(Power power);

    Power getPowerGeneration();

    Power getPowerConsumption();

    void setPowerGeneration(DateTime start_time, Duration duration, Power watts);

    void setPowerConsumption(DateTime start_time, Duration duration, Power watts);

    void equilizeStorage(DateTime start_time, Duration duration);

    void equilizeStorage(DateTime start_time, Power power);

    void chargeFully(DateTime start_time, Duration duration);

    void chargeFully(DateTime start_time, Power power);

    void dischargeFully(DateTime start_time, Duration duration);

    void dischargeFully(DateTime start_time, Power power);

    void setCosf(double cosf);

}
