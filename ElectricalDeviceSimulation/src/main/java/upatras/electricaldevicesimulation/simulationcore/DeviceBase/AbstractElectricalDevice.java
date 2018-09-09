/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.DeviceBase;

import org.joda.time.Duration;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.electricaldevicesimulation.simulationcore.enviromentmodifiers.DeviceEnviromentPowerTransfer;
import upatras.utilitylibrary.library.measurements.types.Energy;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;
import upatras.utilitylibrary.library.measurements.units.electrical.Var;

/**
 * @author Paris
 */
public abstract class AbstractElectricalDevice extends AbstractSimulatableDevice {

    public PowerEnviroment enviroment;

    public Power real_power_consumption;
    public Var imaginary_power_consumption;
    public Power real_power_production;
    public Var imaginary_power_production;
    private Duration step_duration;

    public AbstractElectricalDevice(PowerEnviroment enviroment) {

        super(enviroment.simulationinstance);
        this.enviroment = enviroment;
        new DeviceEnviromentPowerTransfer(this);

    }

    @Override
    public void preprocess(TimeStep simulation_step) {
        real_power_consumption = new Power();
        imaginary_power_consumption = new Var();
        real_power_production = new Power();
        imaginary_power_production = new Var();
        step_duration = simulation_step.duration;
    }

    final public ComplexPower getAveragePowerConsumed() {

        return new ComplexPower(real_power_consumption.value, imaginary_power_consumption.value);

    }

    final public ComplexPower getAveragePowerProduced() {

        return new ComplexPower(real_power_production.value, imaginary_power_production.value);

    }

    final synchronized public void consumePower(Energy energy, double cosf) {
        ComplexPower complex_power = energy.toComplexPower(step_duration, cosf);
        consumePower(complex_power);
    }

    final synchronized public void producePower(Energy energy, double cosf) {
        ComplexPower complex_power = energy.toComplexPower(step_duration, cosf);
        producePower(complex_power);
    }

    final synchronized public void consumePower(ComplexPower power) {
        real_power_consumption = real_power_consumption.plus(power.real);
        imaginary_power_consumption = imaginary_power_consumption.plus(power.imaginary);
    }

    final synchronized public void producePower(ComplexPower power) {
        real_power_production = real_power_production.plus(power.real);
        imaginary_power_production = imaginary_power_production.plus(power.imaginary);
    }

    final synchronized public void handlePower(Energy energy, double cosf) {

        ComplexPower power = energy.toComplexPower(step_duration, cosf);
        handlePower(power);

    }

    final synchronized public void handlePower(ComplexPower power) {

        if (power.real.value > 0) {
            real_power_consumption = real_power_consumption.plus(power.real);

        } else {
            real_power_production = real_power_production.minus(power.real);

        }

        if (power.imaginary.value > 0) {
            imaginary_power_consumption = imaginary_power_consumption.plus(power.imaginary);

        } else {
            imaginary_power_production = imaginary_power_production.minus(power.imaginary);

        }

    }

}
