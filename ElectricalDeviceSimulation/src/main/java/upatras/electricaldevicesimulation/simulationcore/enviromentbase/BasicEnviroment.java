/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentbase;

import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentmodifiers.EnviromentEnviromentHeatTransfer;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.SpecificHeatVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.TemperatureVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.VolumeVariable;
import upatras.utilitylibrary.library.measurements.types.Energy;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.measurements.types.Area;
import upatras.utilitylibrary.library.measurements.units.composite_units.HeatAbsorbingMaterial;

/**
 * @author valitsa-syros
 */
public class BasicEnviroment extends PowerEnviroment {


    public BasicEnviroment(String name, BasicEnviroment parent, Volume volume, Temperature temperature, Double specific_heat, Double heat_transfer_coefficient, Area surface_area_common_with_parent) {
        super(parent);
        this.name = name;

        if (volume.value <= 0) {
            throw new RuntimeException("Enviroment was defined with zero or negative volume()");
        }

        attachVariable(new TemperatureVariable(temperature));
        attachVariable(new VolumeVariable(volume));
        attachVariable(new SpecificHeatVariable(specific_heat));
        attachVariable(new VolumeVariable(volume));

        if(specific_heat==null)
            specific_heat=HeatAbsorbingMaterial.air().getSpecificHeatM(volume);
        if (heat_transfer_coefficient == null)
            heat_transfer_coefficient = 1.0;
        if (surface_area_common_with_parent == null)
            surface_area_common_with_parent = new Area(Math.pow(Math.cbrt(volume.value), 2) * 5);
        new EnviromentEnviromentHeatTransfer(this, parent, heat_transfer_coefficient, surface_area_common_with_parent);

    }

    public BasicEnviroment(String name, DeviceSimulationInstance instance, Volume volume, Temperature temperature, double specific_heat) {
        super(instance);
        this.name = name;

        if (volume.value <= 0) {
            throw new RuntimeException("Enviroment was defined with zero or negative volume()");
        }

        attachVariable(new TemperatureVariable(temperature));
        attachVariable(new VolumeVariable(volume));
        attachVariable(new SpecificHeatVariable(specific_heat));
    }

    public BasicEnviroment(String name, DeviceSimulationInstance instance) {
        super(instance);
        this.name = name;

        Volume volume = new Volume(1000);

        attachVariable(new TemperatureVariable(27));
        attachVariable(new VolumeVariable(volume));
        attachVariable(new SpecificHeatVariable(HeatAbsorbingMaterial.air().getSpecificHeatM(volume)));

    }

    public void useEnergyForTemperature(Energy joules) {
        Temperature degchange = HeatAbsorbingMaterial.jouleTemperatureChange(joules, ((SpecificHeatVariable) getVariable(SpecificHeatVariable.class)).get().value);
        ((TemperatureVariable) getVariable(TemperatureVariable.class)).add(degchange);
    }

    public void useEnergyForTemperature(double joules) {
        useEnergyForTemperature(new Energy(joules));
    }


}
