/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.types;

import org.joda.time.Duration;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;
import upatras.utilitylibrary.library.measurements.units.electrical.Var;

/**
 * For anything that can be measured in Joules
 *
 * @author Paris
 */
public class Energy extends AbstractGenericDouble<Energy> {

    public Energy() {
        super();
    }

    public Energy(double joules) {
        super(joules);
    }

    public Energy(Energy energy) {
        super(energy);
    }

    public Energy(Power power, Duration duration) {
        super(power.value * duration.getMillis() / 1000.0);
    }

    public Energy(double watt, Duration duration) {
        super(watt * duration.getMillis() / 1000.0);
    }

    public static Energy Joules(double J) {
        return new Energy(J);
    }

    public static Energy kiloJoules(double kJ) {
        return new Energy(kJ * 1000);
    }

    public static Energy Calories(double cal) {
        return new Energy(cal * 4.184);
    }

    public static Energy kiloCalories(double kcal) {
        return new Energy(kcal  * 4184);
    }

    public static Energy wattHour(double wh) {
        return new Energy(wh * 3600);
    }

    @Override
    protected Energy generateParentClass(double value) {
        return new Energy(value);
    }

    public Power toPower(Duration duration) {
        return new Power(value / duration.getMillis() * 1000.0);
    }
    public Var toVar(Duration duration) {
        return new Var(value / duration.getMillis() * 1000.0);
    }

    public ComplexPower toComplexPower(Duration duration,double cosf) {
        return new ComplexPower(toPower(duration),cosf);
    }


    public double toWattHours() {
        return value / 3600.0;
    }

    public Duration dividedBy(Power watts) {
        return Duration.millis((long) (value / watts.value * 1000));
    }

    public Power dividedBy(Duration duration) {
        return toPower(duration);
    }

}
