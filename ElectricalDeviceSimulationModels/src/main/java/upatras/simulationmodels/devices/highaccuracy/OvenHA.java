/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.highaccuracy;

import org.joda.time.Duration;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.SpecificHeatVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.TemperatureVariable;
import upatras.utilitylibrary.library.measurements.types.*;
import upatras.utilitylibrary.library.measurements.units.composite_units.HeatAbsorbingMaterial;

/**
 * @author Paris
 */
public class OvenHA extends AbstractElectricalDevice {

    boolean is_cooking = false;
    final Power watts;
    Temperature target_temp = new Temperature(27);
    final Volume volume;
    final Area surface_area;

    BasicEnviroment oven_internal;
    BasicEnviroment oven_external;
    TemperatureVariable external_temp;
    public TemperatureVariable internal_temp;


    double specific_heat_when_cooking = 0;
    double specific_heat_when_empty = 0;


    final double heating_efficiency = 0.95;

    public OvenHA(BasicEnviroment parent_enviroment, Power watts, Volume volume) {
        super(parent_enviroment);

        oven_external = parent_enviroment;
        external_temp = (TemperatureVariable) parent_enviroment.getVariable(TemperatureVariable.class);

        specific_heat_when_cooking += HeatAbsorbingMaterial.steel().getSpecificHeatM(new Mass(1));
        specific_heat_when_empty += HeatAbsorbingMaterial.steel().getSpecificHeatM(new Mass(1));

        specific_heat_when_cooking += HeatAbsorbingMaterial.air().getSpecificHeatM(new Volume(1));
        specific_heat_when_empty += HeatAbsorbingMaterial.air().getSpecificHeatM(new Volume(1));

        specific_heat_when_cooking += HeatAbsorbingMaterial.meat().getSpecificHeatM(new Mass(2));

        oven_internal = new BasicEnviroment(name + " internal environment", parent_enviroment.simulationinstance, volume,
                external_temp.get(), specific_heat_when_empty);

        internal_temp = (TemperatureVariable) oven_internal.getVariable(TemperatureVariable.class);

        this.dependsOn(oven_internal);

        this.volume = volume;
        this.watts = watts;

        double side_length = Math.cbrt(volume.value);
        this.surface_area = new Area(side_length * side_length * 8);


    }

    @Override
    protected void processCommand(Command c) {
        if (c.isCommand("Target Temperature")) {
            is_cooking = true;
            oven_internal.getVariable(SpecificHeatVariable.class).set(new GenericDouble(specific_heat_when_cooking));
            target_temp = (Temperature) c.args[0];
        } else if (c.isCommand("Turn Off")) {
            is_cooking = false;
            oven_internal.getVariable(SpecificHeatVariable.class).set(new GenericDouble(specific_heat_when_empty));
        }
    }


    @Override
    public void simulate(TimeStep simulation_step) {

        Duration actual_duration;

        long millisleft = simulation_step.duration.getMillis();

        Energy total_energy_used = new Energy(0);

        while (millisleft > 0) {
            if (millisleft > 1000) {
                actual_duration = Duration.millis(1000);
                millisleft -= 1000;
            } else {
                actual_duration = Duration.millis(millisleft);
                millisleft = 0;
            }

            if (is_cooking && internal_temp.get().value < target_temp.value) {
                Energy heating_energy = watts.toEnergy(actual_duration);
                total_energy_used=total_energy_used.plus(heating_energy);
                oven_internal.useEnergyForTemperature(heating_energy);
            }

            Energy joules_exchanged_through_walls;
            if (is_cooking) {
                joules_exchanged_through_walls = HeatAbsorbingMaterial.heatTransfer(external_temp.get(),internal_temp.get(), surface_area,0.05, actual_duration);
            } else {
                joules_exchanged_through_walls = HeatAbsorbingMaterial.heatTransfer(external_temp.get(),internal_temp.get(), surface_area,5, actual_duration);
            }

            oven_internal.useEnergyForTemperature(joules_exchanged_through_walls);
            oven_external.useEnergyForTemperature(joules_exchanged_through_walls.multipliedBy(-1));

        }
        consumePower(total_energy_used,1);

    }

}
