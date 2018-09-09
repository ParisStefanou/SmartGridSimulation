/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.units.electrical;

import upatras.utilitylibrary.library.measurements.types.Power;

/**
 * ComplexPower is a container for both Active and Reactive power to be used by
 * almost all device and environments
 *
 * @author Paris
 */
public class ComplexPower extends VoltAmpere {

    /**
     * CompexPower constructor initialized to zero
     */
    public ComplexPower() {
        this(0, 0);

    }

    /**
     * CompexPower constructor initialized by two doubles representing active
     * power in watts and reactive power in watts
     */
    public ComplexPower(double active_power_watt, double reactive_power_watt) {
        super(active_power_watt, reactive_power_watt);
    }

    /**
     * CompexPower constructor initialized by two doubles representing active
     * power in watts and reactive power in watts
     */
    public ComplexPower(Power power, double cosf) {
        super(power.value * cosf, power.value * (Math.sqrt(1 - cosf * cosf)));
    }

    /**
     * CompexPower constructor initialized by two Object representing active
     * power and reactive power
     */
    public ComplexPower(Power active_power, Var reactive_power) {
        super(active_power.value, reactive_power.value);
    }

    /**
     * CompexPower constructor initialized by a ComplePower Object copying it
     */
    public ComplexPower(ComplexPower complexpower) {
        super(complexpower.real, complexpower.imaginary);
    }

    /**
     * Gets a double representing the length of the phasor
     *
     * @return A double representing the length of the phasor
     */
    public double getLength() {
        return Math.sqrt(real.multipliedBy(real).value + (imaginary.multipliedBy(imaginary)).value);
    }

    /**
     * Gets a double representing the angle of the phasor
     *
     * @return A double representing the angle of the phasor
     */
    public double getAngle() {
        return Math.atan2(imaginary.value, real.value);
    }

    /**
     * Gets an ActivePower object representing the active power value
     *
     * @return An ActivePower object representing the active power value
     */
    public ActivePower getActivePower() {
        return new ActivePower(real);
    }

    /**
     * Gets a ReactivePower object representing the reactive power value
     *
     * @return A ReactivePower object representing the reactive power value
     */
    public ReactivePower getReactivePower() {
        return new ReactivePower(imaginary);
    }

    /**
     * Gets an ApparentPower value representing the magnitude of the ComplexPower
     *
     * @return A double value representing the magnitude of the ComplexPower
     */
    public double getApparentPower() {
        return Math.sqrt(real.multipliedBy(real).value + imaginary.multipliedBy(imaginary).value);
    }

    /**
     * Gets the real part of the phasor as a double representing active power in
     * watts
     *
     * @return The real part of the phasor as a double representing active power
     * in watts
     */
    public double getRealAsDouble() {
        return real.value;
    }

    /**
     * Gets the imaginary part of the phasor as a double representing reactive
     * power in watts
     *
     * @return The imaginary part of the phasor as a double representing
     * reactive power in watts
     */
    public double getImaginaryAsDouble() {

        return imaginary.value;
    }

    //--------------------Complex + Active---------------
    public ComplexPower plus(ActivePower active_power) {
        return new ComplexPower(real.plus(active_power.value), imaginary);
    }

    public ComplexPower minus(ActivePower active_power) {
        return new ComplexPower(real.minus(active_power), imaginary);
    }

    public ComplexPower multiply(ActivePower active_power) {
        return new ComplexPower(real.multipliedBy(active_power), imaginary);
    }

    public ComplexPower dividedBy(ActivePower active_power) {
        return new ComplexPower(real.dividedBy(active_power), imaginary);
    }

    //-----------------Complex + Reactive-----------------
    public ComplexPower plus(ReactivePower reactive_power) {
        return new ComplexPower(getRealAsDouble(), getImaginaryAsDouble() + reactive_power.value);
    }

    public ComplexPower minus(ReactivePower reactive_power) {
        return new ComplexPower(getRealAsDouble(), getImaginaryAsDouble() - reactive_power.value);
    }

    public ComplexPower multipliedBy(ReactivePower reactive_power) {
        return new ComplexPower(getRealAsDouble(), getImaginaryAsDouble() * reactive_power.value);
    }

    public ComplexPower dividedBy(ReactivePower reactive_power) {
        return new ComplexPower(getRealAsDouble(), getImaginaryAsDouble() / reactive_power.value);
    }

    //----------------Complex + Complex------------------------
    public ComplexPower plus(ComplexPower complex_power) {
        return new ComplexPower(getRealAsDouble() + complex_power.getRealAsDouble(),
                getImaginaryAsDouble() + complex_power.getImaginaryAsDouble());
    }

    public ComplexPower minus(ComplexPower complex_power) {
        return new ComplexPower(getRealAsDouble() - complex_power.getRealAsDouble(),
                getImaginaryAsDouble() - complex_power.getImaginaryAsDouble());
    }

    public ComplexPower multipliedBy(ComplexPower complex_power) {
        return new ComplexPower(getRealAsDouble() * complex_power.getRealAsDouble(),
                getImaginaryAsDouble() * complex_power.getImaginaryAsDouble());
    }

    public ComplexPower dividedBy(ComplexPower complex_power) {
        return new ComplexPower(getRealAsDouble() / complex_power.getRealAsDouble(),
                getImaginaryAsDouble() / complex_power.getImaginaryAsDouble());
    }

    //---------------Complex + numbers-------------------
    public ComplexPower plus(double active_power, double reactive_power) {
        return new ComplexPower(getRealAsDouble() + active_power,
                getImaginaryAsDouble() + reactive_power);
    }

    public ComplexPower minus(double active_power, double reactive_power) {
        return new ComplexPower(getRealAsDouble() - active_power,
                getImaginaryAsDouble() - reactive_power);
    }

    public ComplexPower multipliedBy(double active_power, double reactive_power) {
        return new ComplexPower(getRealAsDouble() * active_power,
                getImaginaryAsDouble() * reactive_power);
    }

    public ComplexPower dividedBy(double active_power, double reactive_power) {
        return new ComplexPower(getRealAsDouble() / active_power,
                getImaginaryAsDouble() / reactive_power);
    }

    @Override
    public ComplexPower clone() {
        return new ComplexPower(getRealAsDouble(), getImaginaryAsDouble());
    }

    public static ComplexPower KiloWatts(double real, double imaginary) {
        return new ComplexPower(real * 1000, imaginary * 1000);
    }

    public static ComplexPower MegaWatts(double real, double imaginary) {
        return new ComplexPower(real * 1000000, imaginary * 1000000);
    }
}
