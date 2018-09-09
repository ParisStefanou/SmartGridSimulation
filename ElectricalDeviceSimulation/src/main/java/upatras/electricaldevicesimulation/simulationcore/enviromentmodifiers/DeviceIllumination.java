/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentmodifiers;

import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalModifier;

/**
 *
 * @author Paris
 */
public class DeviceIllumination extends AbstractEnvironmentalModifier {

    public DeviceIllumination(String name,AbstractElectricalDevice device, DeviceSimulationInstance instance) {
          super( instance);
          this.name=device.name+" to "+device.enviroment.name+" luminosity transfer";
    }

    @Override
    public void run(TimeStep simulation_step) {
       
    }


}
