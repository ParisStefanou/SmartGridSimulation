/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.utilitylibrary.library.measurements.types.Energy;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;
import upatras.utilitylibrary.library.measurements.units.electrical.Var;

/**
 * @author Paris
 */
enum BatteryMode {
    charging, discharging, idle
}

public class Battery extends AbstractElectricalDevice implements AuxilaryPowerDevice {

    BatteryMode battery_mode = BatteryMode.idle;
    final double max_power_capacity;
    double current_power_capacity;
    final double max_charge_rate;
    final double max_discharge_rate;
    double discharge_rate;
    double charge_rate;

    public Battery(double power_capacity, double current_power, double max_power_output, double max_power_input, double discharge_setting, double charge_setting, PowerEnviroment enviroment) {
        super(enviroment);
        this.max_power_capacity = power_capacity;
        this.current_power_capacity = current_power;
        this.max_charge_rate = max_power_output;
        this.max_discharge_rate = max_power_input;
        this.discharge_rate = discharge_setting;
        this.charge_rate = charge_setting;
    }

    @Override
    protected void processCommand(Command c) {
        if (c.name.equals("Discharge")) {
            battery_mode = BatteryMode.discharging;
        } else if (c.name.equals("Charge")) {
            battery_mode = BatteryMode.charging;
        } else if (c.name.equals("Idle")) {
            battery_mode = BatteryMode.idle;
        }

    }

    @Override
    public void simulate(TimeStep simulation_step) {

        if (battery_mode == BatteryMode.charging) {

            if (current_power_capacity < max_power_capacity) {
                double power_drained = charge_rate * simulation_step.duration.getMillis() / 1000.0;
                current_power_capacity += charge_rate * simulation_step.duration.getMillis() / 1000.0;
                consumePower(new ComplexPower(power_drained, 0));
            }

        } else if (battery_mode == BatteryMode.discharging) {

            if (current_power_capacity > 0) {
                double power_drained = charge_rate * simulation_step.duration.getMillis() / 1000.0;
                current_power_capacity -= charge_rate * simulation_step.duration.getMillis() / 1000.0;
                producePower(new ComplexPower(power_drained, 0));
            }

        }

    }


    @Override
    public Power getMaxPowerGeneration() {
        return null;
    }

    @Override
    public Power getMaxPowerConsumption() {
        return null;
    }

    @Override
    public Power getAvailablePowerGeneration(Duration duration) {
        return null;
    }

    @Override
    public Power getAvailablePowerConsumption(Duration duration) {
        return null;
    }

    @Override
    public Duration getAvailableDurationGeneration(Power power) {
        return null;
    }

    @Override
    public Duration getAvailableDurationConsumption(Power power) {
        return null;
    }

    @Override
    public Power getPowerGeneration() {
        return null;
    }

    @Override
    public Power getPowerConsumption() {
        return null;
    }

    @Override
    public void setPowerGeneration(DateTime starttime, Duration duration, Power watts) {

    }

    @Override
    public void setPowerConsumption(DateTime starttime, Duration duration, Power watts) {

    }

    @Override
    public void equilizeStorage(DateTime starttime, Duration duration) {

    }

    @Override
    public void equilizeStorage(DateTime starttime, Power power) {

    }

    @Override
    public void chargeFully(DateTime startTime, Power power) {

    }

    @Override
    public void chargeFully(DateTime startTime, Duration duration) {

    }

    @Override
    public void dischargeFully(DateTime startTime, Power power) {

    }

    @Override
    public void dischargeFully(DateTime startTime, Duration duration) {

    }

    @Override
    public void setCosf(double cosf) {

    }
}
