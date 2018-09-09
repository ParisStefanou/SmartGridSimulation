/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentmodifiers;

import org.joda.time.Duration;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalModifier;

/**
 *
 * @author Paris
 */
public class DeviceLife extends AbstractEnvironmentalModifier {

    Duration totallife;
    Duration currentlife;
    DeviceEnviromentHeatTransfer dh;
    boolean alive = true;

    public DeviceLife(DeviceEnviromentHeatTransfer dh, Duration totallife, DeviceSimulationInstance instance) {

        super(instance);
        this.name = dh.device.name + " device life";

        this.dh = dh;
        this.totallife = totallife;
        this.currentlife = new Duration(0);

        dependsOn(dh);
        dependsOn(dh.device);
    }

    @Override
    public void run(TimeStep simulation_step) {

        if (alive) {
            currentlife = currentlife.plus(simulation_step.duration);
            if (currentlife.isLongerThan(totallife)) {
                dh.device.submitCommand(new Command("Turn Off", null));
                alive = false;
            }
        }
    }

}
