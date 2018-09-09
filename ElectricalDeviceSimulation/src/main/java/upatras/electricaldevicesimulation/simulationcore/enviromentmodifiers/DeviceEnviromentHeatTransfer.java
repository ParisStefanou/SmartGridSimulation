/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentmodifiers;

import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalModifier;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.SpecificHeatVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.TemperatureVariable;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.measurements.types.Energy;
import upatras.utilitylibrary.library.measurements.units.composite_units.HeatAbsorbingMaterial;

/**
 *
 * @author Paris
 */
public class DeviceEnviromentHeatTransfer extends AbstractEnvironmentalModifier {

    AbstractElectricalDevice device;
    double energy_to_heat_conversion=1;

    public DeviceEnviromentHeatTransfer(AbstractElectricalDevice device) {

        super(device.simulationinstance);
        this.name = device.name + " to " + device.enviroment.name + " HT";
        this.device = device;
        dependsOn(device);
        device.enviroment.dependsOn(this);

    }

    public DeviceEnviromentHeatTransfer(AbstractElectricalDevice device,double energy_to_heat_conversion) {

        super(device.simulationinstance);
        this.name = device.name + " to " + device.enviroment.name + " HT";
        this.device = device;
        dependsOn(device);
        device.enviroment.dependsOn(this);
        this.energy_to_heat_conversion=energy_to_heat_conversion;

    }



    @Override
    public void run(TimeStep simulation_step) {

        Energy energy = device.getAveragePowerConsumed().getActivePower().getEnergy(simulationinstance.current_simulation_step.duration).multipliedBy(energy_to_heat_conversion);

        Temperature degchange=HeatAbsorbingMaterial.jouleTemperatureChange(energy,((SpecificHeatVariable)device.enviroment.getVariable(SpecificHeatVariable.class)).get().value);

        ((TemperatureVariable)device.enviroment.getVariable(TemperatureVariable.class)).add(degchange);

    }

}
