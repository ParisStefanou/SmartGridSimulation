/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.DeviceBase;

import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Paris
 */
public abstract class CompositeSimulatableDevice extends AbstractSimulatableDevice {

    List<AbstractSimulatableDevice> components = new ArrayList<>();

    public CompositeSimulatableDevice(DeviceSimulationInstance instance) {
        super( instance);
    }

    final public void add_component(AbstractSimulatableDevice device) {

        components.add(device);
    }

	@Override
    protected abstract void processCommand(Command c);

}
