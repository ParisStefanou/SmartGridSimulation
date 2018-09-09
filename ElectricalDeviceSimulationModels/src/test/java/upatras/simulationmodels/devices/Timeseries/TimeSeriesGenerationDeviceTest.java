/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.Timeseries;

import org.joda.time.Duration;
import org.junit.Test;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.simulationmodels.commands.TimeSeriesCommands;
import upatras.utilitylibrary.library.dataset.SimpleDataset;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Paris
 */
public class TimeSeriesGenerationDeviceTest {

    public TimeSeriesGenerationDeviceTest() {
    }

    /**
     * Test of simulate method, of class TimeSeriesGenerationDevice.
     */
    @Test
    public void testSimulate() throws FileNotFoundException, IOException {
        DeviceSimulationInstance si = new DeviceSimulationInstance();
        try {
            FileReader r = new FileReader("C:\\Users\\Paris\\Dropbox\\projects\\diplomatikh projects\\electrical device data\\ac Bryant\\Bryant_AC_KW_Period.csv");
            //FileReader r = new FileReader("C:\\Users\\Paris\\Dropbox\\projects\\diplomatikh projects\\electrical device data\\custom\\digital_signal.csv");

            BufferedReader brin = new BufferedReader(r);

            ArrayList<Double> data = new ArrayList<>();
            String csvdata = brin.readLine();
            do {
                data.add(Double.parseDouble(csvdata));
            } while ((csvdata = brin.readLine()) != null);

            PowerEnviroment env = new PowerEnviroment(si);
            SimpleDataset dataset = new SimpleDataset(data);

            TimeSeriesGenerationDevice ac = new TimeSeriesGenerationDevice(env, dataset, new Duration(1000 * 60));
            ac.submitCommand(TimeSeriesCommands.Start());
        } catch (FileNotFoundException ex) {

        }

    }

}
