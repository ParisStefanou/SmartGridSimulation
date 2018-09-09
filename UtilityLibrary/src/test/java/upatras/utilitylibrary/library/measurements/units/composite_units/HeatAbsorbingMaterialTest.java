package upatras.utilitylibrary.library.measurements.units.composite_units;

import org.joda.time.Duration;
import org.junit.Test;
import upatras.utilitylibrary.library.measurements.types.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by Paris on 23-Jun-18.
 */
public class HeatAbsorbingMaterialTest {
    @Test
    public void timeToTemperature() throws Exception {

        Power power=Energy.kiloCalories(1).toPower(Duration.standardSeconds(1));
        double water_c=HeatAbsorbingMaterial.water().getSpecificHeatM(Mass.kilograms(1));

        Temperature degchange = new Temperature(1);

        Duration duration=HeatAbsorbingMaterial.timeToTemperature(power,degchange,water_c);

        assertEquals(duration.getMillis(), 1000, 1);
    }

    @Test
    public void water() throws Exception {
        double water_specific_heat = HeatAbsorbingMaterial.water().getSpecificHeatM(Mass.kilograms(1));
        double water_specific_heat2 = HeatAbsorbingMaterial.water().getSpecificHeatM(Volume.liter(1));

        assertEquals(water_specific_heat,water_specific_heat2,0.1);
    }

    @Test
    public void HeatExchange() throws Exception {
        Energy to_use=Energy.kiloCalories(1);
        double water_c=HeatAbsorbingMaterial.water().getSpecificHeatM(Mass.kilograms(1));

        Temperature degchange = new Temperature(to_use.value / water_c);
        assertEquals(1, degchange.value, 0.01);

    }

    @Test
    public void getSpecificHeat() throws Exception {
    }

    @Test
    public void getSpecificHeat1() throws Exception {
    }

    @Test
    public void getSpecificHeat2() throws Exception {
    }

}