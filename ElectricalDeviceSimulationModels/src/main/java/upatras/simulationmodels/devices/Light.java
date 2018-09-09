/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices;

import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.simulationmodels.devices.basic.SimpleLoad;

/**
 *
 * @author Paris
 */
public class Light extends SimpleLoad {

    public Light(PowerEnviroment enviroment) {
        super(enviroment, 100, 0);
    }

}
