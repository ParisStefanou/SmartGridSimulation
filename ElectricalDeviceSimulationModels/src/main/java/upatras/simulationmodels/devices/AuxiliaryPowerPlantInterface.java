package upatras.simulationmodels.devices;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.utilitylibrary.library.measurements.types.Power;

/**
 * Created by Paris on 28-Apr-18.
 */
public interface AuxiliaryPowerPlantInterface {


    void producePower(DateTime starttime, Duration effort_duration, Power watts, double cosf);

    void consumePower(DateTime starttime, Duration effort_duration, Power watts, double cosf);

    Power getMaxAvailablePowerProduction();

    Power getMaxAvailablePowerConsumption();

    Power getSteadyPowerProduction(Duration effort_duration);

    Power getSteadyPowerConsumption(Duration effort_duration);

    Duration getMaxAvailableProductionDuration(Power watts);

    Duration getMaxAvailableConsumptionDuration(Power watts);

}
