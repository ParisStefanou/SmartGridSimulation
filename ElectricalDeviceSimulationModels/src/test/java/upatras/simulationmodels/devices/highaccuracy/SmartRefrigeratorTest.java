/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.highaccuracy;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

import static org.junit.Assert.*;

import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerVariable;
import upatras.simulationmodels.commands.TemperatureCommands;
import upatras.simulationmodels.enviroments.OutdoorEnviroment;
import upatras.utilitylibrary.library.measurements.types.Energy;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.types.Temperature;

/**
 * @author Paris
 */
public class SmartRefrigeratorTest {
    @Test
    public void chargeFully() throws Exception {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);
        in1.setStepDuration(Duration.standardMinutes(1));

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        Duration dur = refrigerator.chargeFullyNow(starttime, refrigerator.getMaxPowerConsumption());

        in1.advanceDuration(dur);

        assertEquals(5, refrigerator.internal_temp.get().value, 0.1);

        in1.advanceDuration(dur);

        assertEquals(5, refrigerator.internal_temp.get().value, 0.1);

    }

    @Test
    public void chargeFully1() throws Exception {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);
        in1.setStepDuration(Duration.standardMinutes(1));

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        refrigerator.chargeFully(starttime, Duration.standardMinutes(5));

        in1.advanceDuration(Duration.standardMinutes(5));

        assertEquals(5, refrigerator.internal_temp.get().value, 0.1);

        in1.advanceDuration(Duration.standardMinutes(5));

        assertEquals(5, refrigerator.internal_temp.get().value, 0.1);
    }

    @Test
    public void dischargeFully() throws Exception {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);
        in1.setStepDuration(Duration.standardMinutes(1));

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        Duration dur = refrigerator.dischargeFullyNow(starttime, refrigerator.getMaxPowerGeneration());

        in1.advanceDuration(dur);

        assertEquals(7, refrigerator.internal_temp.get().value, 0.1);

        in1.advanceDuration(dur);

        assertEquals(7, refrigerator.internal_temp.get().value, 0.1);
    }

    @Test
    public void dischargeFully1() throws Exception {

        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);
        in1.setStepDuration(Duration.standardMinutes(1));

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        refrigerator.dischargeFully(starttime, Duration.standardMinutes(6));

        in1.advanceDuration(Duration.standardMinutes(6));

        assertEquals(7, refrigerator.internal_temp.get().value, 0.1);

        in1.advanceDuration(Duration.standardMinutes(6));

        assertEquals(7, refrigerator.internal_temp.get().value, 0.1);
    }

    @Test
    public void chargedischargefully() throws Exception {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);
        in1.setStepDuration(Duration.standardMinutes(1));

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        Duration dur = refrigerator.chargeFullyNow(starttime, refrigerator.getMaxPowerConsumption());
        System.out.println("will charge for " + dur.toString());
        in1.advanceDuration(dur);

        assertEquals(5, refrigerator.internal_temp.get().value, 0.1);

        dur = refrigerator.dischargeFullyNow(starttime.plus(dur), refrigerator.getMaxPowerGeneration());
        System.out.println("will discharge for " + dur.toString());
        in1.advanceDuration(dur);

        assertEquals(7, refrigerator.internal_temp.get().value, 0.1);

    }

    @Test
    public void dischargechargefully() throws Exception {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);
        in1.setStepDuration(Duration.standardSeconds(1));

        OutdoorEnviroment out = new OutdoorEnviroment("outside",in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        Duration dur = refrigerator.dischargeFullyNow(starttime, refrigerator.getMaxPowerGeneration());

        in1.advanceDuration(dur);

        assertEquals(7, refrigerator.internal_temp.get().value, 0.1);

        dur = refrigerator.chargeFullyNow(starttime.plus(dur), refrigerator.getMaxPowerConsumption());

        in1.advanceDuration(dur);

        assertEquals(5, refrigerator.internal_temp.get().value, 0.1);

    }


    @Test
    public void getMaximumEnergy() throws Exception {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        Energy e1 = refrigerator.getMaximumEnergy();

        SmartRefrigerator refrigerator2 = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(8), new Temperature(5), 0.15);

        Energy e2 = refrigerator2.getMaximumEnergy();

        assertTrue(e2.value > e1.value);

        SmartRefrigerator refrigerator3 = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(6), new Temperature(6), 0.15);

        Energy e3 = refrigerator3.getMaximumEnergy();

        assertEquals(0, e3.value, 0.01);
    }

    @Test
    public void getStoredEnergy() throws Exception {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);
        in1.setStepDuration(Duration.standardMinutes(1));

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        Energy average = refrigerator.getStoredEnergy();

        Duration dur = refrigerator.chargeFullyNow(starttime, refrigerator.getMaxPowerConsumption());

        in1.advanceDuration(dur);

        Energy max = refrigerator.getStoredEnergy();

        assertEquals(average.value * 2, max.value, average.value * 0.1);

        dur = refrigerator.dischargeFullyNow(starttime.plus(dur), refrigerator.getMaxPowerGeneration());

        in1.advanceDuration(dur);

        Energy min = refrigerator.getStoredEnergy();

        assertEquals(0, min.value, average.value * 0.1);


    }

    @Test
    public void getStorableEnergy() throws Exception {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);
        in1.setStepDuration(Duration.standardMinutes(1));

        OutdoorEnviroment out = new OutdoorEnviroment("outside",in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        Energy average = refrigerator.getStorableEnergy();

        Duration dur = refrigerator.chargeFullyNow(starttime, refrigerator.getMaxPowerConsumption());

        in1.advanceDuration(dur);

        Energy min = refrigerator.getStorableEnergy();

        assertEquals(0, min.value, average.value * 0.1);

        dur = refrigerator.dischargeFullyNow(starttime.plus(dur), refrigerator.getMaxPowerGeneration());

        in1.advanceDuration(dur);

        Energy max = refrigerator.getStorableEnergy();

        assertEquals(average.value * 2, max.value, average.value * 0.1);
    }

    @Test
    public void getMaxPowerGeneration() throws Exception {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);
        in1.setStepDuration(Duration.standardMinutes(1));

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);
        assertEquals(refrigerator.wallPowerBleed().dividedBy(refrigerator.heating_efficiency).value, refrigerator.getMaxPowerGeneration().value, 1);

    }

    @Test
    public void getMaxChargeRate() throws Exception {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);
        in1.setStepDuration(Duration.standardMinutes(1));

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);
        assertEquals(refrigerator.original_max_power.minus((refrigerator.wallPowerBleed().dividedBy(refrigerator.heating_efficiency))).value, refrigerator.getMaxPowerConsumption().value, 1);
    }

    @Test
    public void getAvailablePowerProduction() throws Exception {
    }

    @Test
    public void getAvailableDurationProduction() throws Exception {
    }

    @Test
    public void getAvailablePowerConsumption() throws Exception {
    }

    @Test
    public void getAvailableDurationConsumption() throws Exception {
    }

    @Test
    public void setDischargeRate() throws Exception {
    }

    @Test
    public void setChargeRate() throws Exception {
    }

    @Test
    public void setCosf() throws Exception {
    }

    @Test
    public void equilizeStorage() throws Exception {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);
        in1.setStepDuration(Duration.standardMinutes(1));

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);
        Duration dur = refrigerator.dischargeFullyNow(starttime, refrigerator.getMaxPowerGeneration());

        in1.advanceDuration(dur);

        assertEquals(7, refrigerator.internal_temp.get().value, 0.1);

        starttime = starttime.plus(dur);
        dur = refrigerator.equilizeStorageNow(starttime, refrigerator.getMaxPowerConsumption());
        in1.advanceDuration(dur);

        assertEquals(6, refrigerator.internal_temp.get().value, 0.1);

        starttime = starttime.plus(dur);
        dur = refrigerator.chargeFullyNow(starttime, refrigerator.getMaxPowerConsumption());

        in1.advanceDuration(dur);

        assertEquals(5, refrigerator.internal_temp.get().value, 0.1);

        starttime = starttime.plus(dur);
        dur = refrigerator.equilizeStorageNow(starttime, refrigerator.getMaxPowerGeneration());
        in1.advanceDuration(dur);

        assertEquals(6, refrigerator.internal_temp.get().value, 0.1);
    }

    @Test
    public void equilizeStorage1() throws Exception {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);
        in1.setStepDuration(Duration.standardMinutes(1));

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        Duration dur = refrigerator.dischargeFullyNow(starttime, refrigerator.getMaxPowerGeneration());

        in1.advanceDuration(dur);

        assertEquals(7, refrigerator.internal_temp.get().value, 0.1);

        starttime = starttime.plus(dur);
        refrigerator.equilizeStorage(starttime, Duration.standardMinutes(10));
        in1.advanceDuration( Duration.standardMinutes(10));

        assertEquals(6, refrigerator.internal_temp.get().value, 0.1);

        starttime = starttime.plus( Duration.standardMinutes(10));
        dur = refrigerator.chargeFullyNow(starttime, refrigerator.getMaxPowerConsumption());

        in1.advanceDuration(dur);

        assertEquals(5, refrigerator.internal_temp.get().value, 0.1);

        starttime = starttime.plus(dur);
        refrigerator.equilizeStorage(starttime, Duration.standardMinutes(10));
        in1.advanceDuration( Duration.standardMinutes(10));

        assertEquals(6, refrigerator.internal_temp.get().value, 0.1);
    }


    @Test
    public void steadyRefrigerator() throws Exception {

        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(269), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        Duration dur = Duration.standardSeconds(60);
        in1.setStepDuration(Duration.standardSeconds(10));
        for (int i = 0; i < 100; i++) {
            in1.advanceDuration(dur);
            assertEquals(6, refrigerator.internal_temp.get().value, 0.1);
        }
    }


    @Test
    public void predictedTimeHeatingMax() {

        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        refrigerator.submitCommand(TemperatureCommands.TurnOff(starttime.plusMillis(1)));

        in1.advanceDuration(refrigerator.timeToHighestTemperature());

        assertEquals(7, refrigerator.internal_temp.get().value, 0.1);

        in1.shutdown();
    }


    @Test
    public void predictedTimeCoolingMax() {

        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(1), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);
        refrigerator.submitCommand(TemperatureCommands.targetTemperature(starttime, new Temperature(0)));

        in1.advanceDuration(refrigerator.timeToLowestTemperature());

        System.out.println("Time to lowest :" + refrigerator.timeToLowestTemperature());

        assertEquals(5, refrigerator.internal_temp.get().value, 0.1);

        in1.shutdown();
    }

    /**
     * Test of bestDurationEffortDischarge method, of class SmartRefrigerator.
     */

    @Test
    public void simplerunTest() {
        DateTime starttime = new DateTime(2017, 1, 1, 0, 0);
        DeviceSimulationInstance in1 = new DeviceSimulationInstance(starttime);
        in1.setPresent(starttime);

        OutdoorEnviroment out = new OutdoorEnviroment("outside", in1,null);

        SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6), new Temperature(6), new Temperature(6), new Temperature(7), new Temperature(5), 0.15);

        in1.setStepDuration(Duration.standardMinutes(5));

        Power average_power = refrigerator.average_power;
        for (int i = 0; i < 60; i++) {
            in1.advanceDuration(Duration.standardMinutes(5));
            assertEquals(average_power.value, refrigerator.getAveragePowerConsumed().getRealAsDouble(), average_power.value / 20.0);
        }

        in1.shutdown();
    }


}
