/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.models;

import upatras.electricaldevicesimulation.simulationcore.Model;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.simulationmodels.commands.OnOffCommands;
import upatras.simulationmodels.devices.Photovoltaic;
import upatras.simulationmodels.devices.basic.RandomLoad;
import upatras.utilitylibrary.library.measurements.types.Area;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.measurements.units.composite_units.HeatAbsorbingMaterial;

/**
 *
 * @author Paris
 */
public class SimpleHouse extends Model {

    public final BasicEnviroment house_enviroment;

    public SimpleHouse(String name, BasicEnviroment parent) {
        super(parent.simulationinstance);
        this.name = name + " Model";
        house_enviroment = new BasicEnviroment(name, parent, new Volume(10000), new Temperature(27), HeatAbsorbingMaterial.air().getSpecificHeatM(10000),10.0,null);
        addContainedTask(house_enviroment);
        for (int i = 0; i < 3; i++) {
            RandomLoad lamp = new RandomLoad(house_enviroment, 100.0);
            lamp.submitCommand(OnOffCommands.TurnOn());
            addContainedTask(lamp);
        }
        Photovoltaic pv = new Photovoltaic(house_enviroment, 200);
        addContainedTask(pv);

    }

}
