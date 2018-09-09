package upatras.utilitylibrary.library.measurements.units.composite_units;

import org.joda.time.Duration;
import upatras.utilitylibrary.library.measurements.types.*;

/**
 * Created by Paris on 23-Jun-18.
 */
public class HeatAbsorbingMaterial {

    public final double heat_capacity;
    public final double density;

    public static HeatAbsorbingMaterial air() {
        return new HeatAbsorbingMaterial(1.0035, 1.15);
    }

    public static HeatAbsorbingMaterial water() {
        return new HeatAbsorbingMaterial(4.185, 1000);
    }

    public static HeatAbsorbingMaterial meat() {
        return new HeatAbsorbingMaterial(1.47, 1487.8);
    }

    public static HeatAbsorbingMaterial steel() {return new HeatAbsorbingMaterial(0.5, 8050);}

    public static HeatAbsorbingMaterial brick() {
        return new HeatAbsorbingMaterial(0.38, 600);
    }

    public static HeatAbsorbingMaterial wood() {
        return new HeatAbsorbingMaterial(1.76, 700);
    }


    /**
     * @param heat_capacity in joules per gramm
     * @param density       in kilograms per cubic meter
     */
    public HeatAbsorbingMaterial(double heat_capacity, double density) {
        this.heat_capacity = heat_capacity * 1000.0;
        this.density = density;
    }

    public double getSpecificHeatM(double volume) {
        double mass_of_material = density * volume;
        return heat_capacity * mass_of_material;
    }

    public double getSpecificHeatM(Volume volume) {
        double mass_of_material = density * volume.value;
        return heat_capacity * mass_of_material;
    }

    public double getSpecificHeatM(Mass mass) {
        return heat_capacity * mass.value;
    }


    // from https://www.process-heating.com/articles/87988-calculating-heat-loss
    public static Energy heatTransfer(Temperature temp_1, Temperature temp_2, Area surface_area, double thermal_conductivity, Duration duration) {
        return new Power(thermal_conductivity * surface_area.value * (temp_1.minus(temp_2).value)
                * 1.1 / (3.412)).toEnergy(duration);
    }

    public static Power powerTransfer(Temperature temp_1, Temperature temp_2, Area surface_area, double thermal_conductivity) {
        return new Power(thermal_conductivity * surface_area.value * (temp_1.minus(temp_2).value) * 1.1 / (3.412));
    }

    public static Temperature jouleTemperatureChange(Energy joules, double specific_heat_mass) {
        return new Temperature(joules.value / specific_heat_mass);
    }

    public static Duration timeToTemperature(Power watts, Temperature temperature_diffrence, double specific_heat_mass) {
        return Duration.millis((long) (temperature_diffrence.value * specific_heat_mass / watts.value * 1000.0));
    }
}
