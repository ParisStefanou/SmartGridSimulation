/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.tests;

import org.joda.time.Duration;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerConsumptionVariable;
import upatras.simulationmodels.models.SimpleHouse;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemExtractor;

/**
 *
 * @author Paris
 */
public class VisualizationTest {

    public static void main(String[] args) {

        System.out.println("Generating Devices");

        DeviceSimulationInstance in1 = new DeviceSimulationInstance();
        BasicEnviroment ho=new BasicEnviroment("House outer",in1);
        
        SimpleHouse sh = new SimpleHouse("house", ho);

        if (true) {
            PowerConsumptionVariable var = new PowerConsumptionVariable();
            sh.house_enviroment.attachVariable(var);
            new MeasurableItemExtractor(sh.house_enviroment.getVariable(PowerConsumptionVariable.class)).visualize();
        }

        in1.preprocess().setRunMode(DeviceSimulationInstance.RunMode.serial).advanceDuration(new Duration(1000 * 60 * 1000));

        if (true) {
            in1.dag_executor.dag.visualize();
        }
    }
}
