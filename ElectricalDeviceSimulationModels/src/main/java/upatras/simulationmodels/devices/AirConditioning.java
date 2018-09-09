/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices;

import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractSimulatableDevice;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.CompositeSimulatableDevice;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Paris
 */
public class AirConditioning extends CompositeSimulatableDevice {

    CoolingUnit cu;
    HeatRadiator hr;

    public AirConditioning(PowerEnviroment enviroment, double btu, double powerconsumption) {

        super(enviroment.simulationinstance);
        List<AbstractSimulatableDevice> components = new ArrayList<>();

        cu = new CoolingUnit(enviroment);
        hr = new HeatRadiator(enviroment);
        dependsOn(cu);
        dependsOn(hr);
        

        hr.dependsOn(cu);

    }

    @Override
    protected void processCommand(Command c) {
        cu.processCommand(c);
        hr.processCommand(c);
    }


    @Override
    public void simulate(TimeStep simulation_step) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   
    }

}
