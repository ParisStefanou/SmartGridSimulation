/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.highaccuracy;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.simulationmodels.commands.AuxilaryDeviceCommands;
import upatras.simulationmodels.devices.AuxilaryPowerDevice;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.types.Energy;

/**
 * @author Paris
 */
public class SmartRefrigerator extends RefrigeratorHA implements AuxilaryPowerDevice {

    final public Temperature min_allowed_temp;
    final public Temperature max_allowed_temp;
    final public Temperature original_target_temp;

    final public Power original_max_power;
    final public Power average_power;
    double cosf = 1;

    public SmartRefrigerator(BasicEnviroment enviroment, Power max_power, Volume volume, Temperature initial_temperature, Temperature target_temperature, Temperature max_allowed_temp, Temperature min_allowed_temp, double thermal_conductivity) {
        super(enviroment, max_power, volume, initial_temperature, thermal_conductivity);
        this.target_temperature = target_temperature;
        this.max_allowed_temp = max_allowed_temp;
        this.min_allowed_temp = min_allowed_temp;
        this.average_power = wallPowerBleed().dividedBy(heating_efficiency);
        original_max_power = max_power;
        original_target_temp = target_temperature;
        this.is_on = true;
        this.balancer = true;
    }

    public Temperature differenceFromTargetTemperature() {
        return internal_temp.get().minus(target_temperature);

    }

    public Temperature getTargetTemperature() {
        return target_temperature;

    }

    public Duration timeToLowestTemperature() {

        return timeToTemperature(min_allowed_temp);
    }

    public Duration timeToHighestTemperature() {

        return timeToTemperature(max_allowed_temp);

    }


    public void controlledProductionNow(DateTime now, Power watts) {

        if (internal_temp.get().value < max_allowed_temp.value) {

            if (watts.value <= average_power.value) {
                this.watts = average_power.minus(watts);
            } else {
                this.watts = new Power(0);
            }

            Duration dur = getAvailableDurationGeneration(watts);

            if (watts.value != 0)
                AuxilaryDeviceCommands.steady(now.plus(dur));

            balancer = false;
            is_on = true;
        }
    }

    public void controlledConsumptionNow(DateTime now, Power watts) {

        if (watts.value < this.original_max_power.minus(average_power).value) {
            this.watts = average_power.plus(watts);
        } else {
            this.watts = original_max_power;
        }

        Duration dur = getAvailableDurationConsumption(watts);

        if (watts.value != 0)
            AuxilaryDeviceCommands.steady(now.plus(dur));

        balancer = false;
        is_on = true;
    }


    public Energy getMaximumEnergy() {
        Energy toreturn = new Energy(max_allowed_temp.minus(min_allowed_temp).value * specific_heat);
        if (toreturn.value < 0)
            return new Energy();
        else
            return toreturn;
    }

    public Energy getStoredEnergy() {
        Energy toreturn = new Energy(max_allowed_temp.minus(internal_temp.get()).value * specific_heat);
        if (toreturn.value < 0)
            return new Energy();
        else
            return toreturn;
    }

    public Energy getStorableEnergy() {
        Energy toreturn = new Energy(internal_temp.get().minus(min_allowed_temp).value * specific_heat);
        if (toreturn.value < 0)
            return new Energy();
        else
            return toreturn;
    }


    @Override
    public Power getMaxPowerGeneration() {
        return average_power.clone();
    }

    @Override
    public Power getMaxPowerConsumption() {
        return original_max_power.minus(average_power);
    }

    @Override
    public Power getAvailablePowerGeneration(Duration duration) {
        Energy energy_remaining = getStoredEnergy();
        Power potential_power = new Power(energy_remaining.dividedBy(heating_efficiency).dividedBy(duration.getMillis() / 1000.0).value);
        if (potential_power.value > getMaxPowerGeneration().value || potential_power.value < 0) {
            return new Power();
        } else {
            return potential_power;
        }
    }

    @Override
    public Duration getAvailableDurationGeneration(Power power) {

        if (power.value > getMaxPowerGeneration().value || power.value < 0) {
            return new Duration(0);
        } else {
            Energy energy_remaining = getStoredEnergy();
            return energy_remaining.dividedBy(heating_efficiency).dividedBy(power);
        }
    }

    @Override
    public Power getAvailablePowerConsumption(Duration duration) {
        Energy energy_remaining = getStorableEnergy();
        Power potential_power = new Power(energy_remaining.dividedBy(heating_efficiency).dividedBy(duration.getMillis() / 1000.0).value);
        if (potential_power.value > getMaxPowerConsumption().value) {
            return original_max_power.minus(average_power);
        } else {
            return potential_power;
        }
    }

    @Override
    public Duration getAvailableDurationConsumption(Power power) {

        if (power.value > getMaxPowerConsumption().value || power.value < 0) {
            return new Duration(0);
        } else {
            Energy energy_remaining = getStorableEnergy();
            return energy_remaining.dividedBy(heating_efficiency).dividedBy(power);
        }
    }

    @Override
    public Power getPowerGeneration() {
        return average_power.minus(watts);
    }

    @Override
    public Power getPowerConsumption() {
        return watts.minus(average_power);
    }


    @Override
    public void setPowerGeneration(DateTime starttime, Duration duration, Power watts) {
        if (watts.value > getMaxPowerGeneration().value) {
            throw new ExceedsMaxPossibleGenerationException("Requestsed : " + watts + " , max available :" + getMaxPowerGeneration().value);
        } else {
            emptyCommandQueue(starttime, starttime.plus(duration).minusMillis(1));
            submitCommand(AuxilaryDeviceCommands.discharge(starttime, watts));
            submitCommand(AuxilaryDeviceCommands.steady(starttime.plus(duration).minusMillis(1)));
        }
    }

    @Override
    public void setPowerConsumption(DateTime starttime, Duration duration, Power watts) {
        if (watts.value > getMaxPowerConsumption().value) {
            throw new ExceedsMaxPossibleProductionException("Requestsed : " + watts + " , max available :" + getMaxPowerConsumption().value);
        } else {
            emptyCommandQueue(starttime, starttime.plus(duration).minusMillis(1));
            submitCommand(AuxilaryDeviceCommands.charge(starttime, watts));
            submitCommand(AuxilaryDeviceCommands.steady(starttime.plus(duration).minusMillis(1)));
        }
    }

    @Override
    public void equilizeStorage(DateTime start_time, Duration duration) {
        submitCommand(AuxilaryDeviceCommands.equalizeStorage(start_time, duration));
    }

    @Override
    public void equilizeStorage(DateTime start_time, Power power) {
        submitCommand(AuxilaryDeviceCommands.equalizeStorage(start_time, power));
    }

    @Override
    public void chargeFully(DateTime start_time, Duration duration) {
        submitCommand(AuxilaryDeviceCommands.chargeFully(start_time, duration));
    }

    @Override
    public void chargeFully(DateTime start_time, Power power) {
        submitCommand(AuxilaryDeviceCommands.chargeFully(start_time, power));
    }

    @Override
    public void dischargeFully(DateTime start_time, Duration duration) {
        submitCommand(AuxilaryDeviceCommands.dischargeFully(start_time, duration));
    }

    @Override
    public void dischargeFully(DateTime start_time, Power power) {
        submitCommand(AuxilaryDeviceCommands.dischargeFully(start_time, power));
    }

    @Override
    public void setCosf(double cosf) {
        this.cosf = cosf;
    }


    public Power equilizeStorageNow(DateTime present, Duration duration) {
        Energy energy_gap = getStoredEnergy().minus(getMaximumEnergy().dividedBy(2));

        if (energy_gap.value < -1) {

            Power potential_power = new Power(energy_gap.multipliedBy(-1).dividedBy(heating_efficiency).dividedBy(duration.getMillis() / 1000.0).value);

            if (potential_power.value > getMaxPowerConsumption().value) {
                duration = energy_gap.multipliedBy(-1).dividedBy(heating_efficiency).dividedBy(getMaxPowerConsumption());
                setPowerConsumption(present, duration, getMaxPowerConsumption());
                return getMaxPowerConsumption();
            } else if (duration.getMillis() < 0) {
                System.err.println("The duration to equalize in was negative");
            } else {
                setPowerConsumption(present, duration, potential_power);
                return potential_power;
            }
        } else if (energy_gap.value > 1) {
            Power potential_power = new Power(energy_gap.dividedBy(heating_efficiency).dividedBy(duration.getMillis() / 1000.0).value);
            if (potential_power.value > getMaxPowerGeneration().value) {
                duration = energy_gap.dividedBy(heating_efficiency).dividedBy(getMaxPowerGeneration());
                setPowerGeneration(present, duration, getMaxPowerGeneration());
                return getMaxPowerGeneration();
            } else if (duration.getMillis() < 0) {
                System.err.println("The duration to equalize in was negative");
            } else {
                setPowerGeneration(present, duration, potential_power);
                return potential_power;
            }
        }
        return new Power();
    }

    public Duration equilizeStorageNow(DateTime present, Power power) {
        Energy energy_gap = getStoredEnergy().minus(getMaximumEnergy().dividedBy(2));

        if (energy_gap.value < -1) {

            Duration duration = energy_gap.multipliedBy(-1).dividedBy(heating_efficiency).dividedBy(power);
            if (power.value > getMaxPowerConsumption().value) {
                duration = energy_gap.multipliedBy(-1).dividedBy(heating_efficiency).dividedBy(getMaxPowerConsumption());
                setPowerConsumption(present, duration, getMaxPowerConsumption());
                return duration;
            } else if (power.value < 0) {
                System.err.println("The power that was to be used for equalization was negative");
            } else {
                setPowerConsumption(present, duration, power);
                return duration;
            }
        } else if (energy_gap.value > 1) {

            Duration duration = energy_gap.dividedBy(heating_efficiency).dividedBy(power);

            if (power.value > getMaxPowerGeneration().value) {
                duration = energy_gap.dividedBy(heating_efficiency).dividedBy(getMaxPowerGeneration());
                setPowerGeneration(present, duration, getMaxPowerGeneration());
                return duration;
            } else if (power.value < 0) {
                System.err.println("The power that was to be used for equalization was negative");
            } else {
                setPowerGeneration(present, duration, power);
                return duration;
            }
        }
        return new Duration(0);
    }


    public Duration chargeFullyNow(DateTime present, Power power) {
        if (power.value > getMaxPowerConsumption().value) {
            Duration duration = getAvailableDurationConsumption(getMaxPowerConsumption());
            setPowerConsumption(present, duration, getMaxPowerConsumption());
            return duration;
        } else {
            Duration duration = getAvailableDurationConsumption(power);
            setPowerConsumption(present, duration, power);
            return duration;
        }
    }

    public Power chargeFullyNow(DateTime startTime, Duration duration) {
        Power power = getAvailablePowerConsumption(duration);

        if (power.value > getMaxPowerConsumption().value) {
            duration = getAvailableDurationConsumption(getMaxPowerConsumption());
            setPowerConsumption(startTime, duration, getMaxPowerConsumption());
            return power;
        } else {
            setPowerConsumption(startTime, duration, power);
            return power;
        }
    }

    public Duration dischargeFullyNow(DateTime present, Power power) {

        if (power.value > getMaxPowerGeneration().value) {
            Duration duration = getAvailableDurationGeneration(getMaxPowerGeneration());
            setPowerGeneration(present, duration, getMaxPowerGeneration());
            return duration;
        } else {
            Duration duration = getAvailableDurationGeneration(power);
            setPowerGeneration(present, duration, power);
            return duration;
        }
    }

    public Power dischargeFullyNow(DateTime present, Duration duration) {
        Power power = getAvailablePowerGeneration(duration);

        if (power.value > getMaxPowerGeneration().value) {
            duration = getAvailableDurationGeneration(getMaxPowerGeneration());
            setPowerGeneration(present, duration, getMaxPowerGeneration());
            return power;
        } else {
            setPowerGeneration(present, duration, power);
            return power;
        }
    }

    public void resetOutput(DateTime time) {
        watts = original_max_power;
        target_temperature = original_target_temp;
        is_on = true;
        balancer = true;
    }

    public void steadyOutput() {
        watts = original_max_power;
        target_temperature = internal_temp.get();
        is_on = true;
        balancer = true;
    }

    @Override
    protected void processCommand(Command c) {
        super.processCommand(c);
        switch (c.name) {
            case "Steady":
                steadyOutput();
                break;
            case "ProducePower":
                controlledProductionNow(c.time, (Power) c.args[0]);
                break;
            case "ConsumePower":
                controlledConsumptionNow(c.time, (Power) c.args[0]);
                break;
            case "EqualizeStorage":
                if (c.args[0] instanceof Power)
                    equilizeStorageNow(c.time, (Power) c.args[0]);
                else {
                    equilizeStorageNow(c.time, (Duration) c.args[0]);
                }
                break;
            case "DischargeFully":
                if (c.args[0] instanceof Power)
                    dischargeFullyNow(c.time, (Power) c.args[0]);
                else {
                    dischargeFullyNow(c.time, (Duration) c.args[0]);
                }
                break;
            case "ChargeFully":
                if (c.args[0] instanceof Power)
                    chargeFullyNow(c.time, (Power) c.args[0]);
                else {
                    chargeFullyNow(c.time, (Duration) c.args[0]);
                }
                break;
            default:
                break;
        }
    }

}
