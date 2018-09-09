/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentmodifiers;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.TemperatureVariable;
import upatras.utilitylibrary.library.measurements.Measurement;
import upatras.utilitylibrary.library.measurements.types.Area;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.measurements.units.composite_units.HeatAbsorbingMaterial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Paris
 */
public class EnviromentEnviromentHeatTransferTest {

    public EnviromentEnviromentHeatTransferTest() {
    }

    /**
     * Test of run method, of class EnviromentEnviromentHeatTransfer.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(DateTime.now());

        Volume v=new Volume(30);

        BasicEnviroment cold_env = new BasicEnviroment("ce", in1, v, new Temperature(0), HeatAbsorbingMaterial.air().getSpecificHeatM(v));

        BasicEnviroment hot_env = new BasicEnviroment("he", cold_env, v, new Temperature(100), HeatAbsorbingMaterial.air().getSpecificHeatM(v),null,null);

        in1.setStepDuration(Duration.standardMinutes(1)).advanceDuration(Duration.standardHours(3));

        Measurement m = hot_env.getVariable(TemperatureVariable.class).getMeasurement(DateTime.now());
        if (m.data instanceof Temperature) {
            assertEquals(50,((Temperature) m.data).value, 10);
        } else {
            fail("Measurement of wrong type");
        }

        m = (cold_env.getVariable(TemperatureVariable.class).getMeasurement(DateTime.now()));
        if (m.data instanceof Temperature) {
            assertEquals(50,((Temperature) m.data).value, 10);
        } else {
            fail("Measurement of wrong type");
        }
    }

    @Test
    public void testRunLargeStep() {
        System.out.println("run");
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(DateTime.now());

        Volume v=new Volume(30);

        BasicEnviroment cold_env = new BasicEnviroment("ce", in1,v, new Temperature(0), HeatAbsorbingMaterial.air().getSpecificHeatM(v));

        BasicEnviroment hot_env = new BasicEnviroment("he", cold_env,v, new Temperature(100), HeatAbsorbingMaterial.air().getSpecificHeatM(v),null,null);

        in1.setStepDuration(Duration.standardHours(1)).advanceDuration(Duration.standardHours(3));

        Measurement m = hot_env.getVariable(TemperatureVariable.class).getMeasurement(DateTime.now());
        if (m.data instanceof Temperature) {
            assertEquals(50,((Temperature) m.data).value, 10);
        } else {
            fail("Measurement of wrong type");
        }

        m = (cold_env.getVariable(TemperatureVariable.class).getMeasurement(DateTime.now()));
        if (m.data instanceof Temperature) {
            assertEquals( 50,((Temperature) m.data).value, 10);
        } else {
            fail("Measurement of wrong type");
        }


    }

}
