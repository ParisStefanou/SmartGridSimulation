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
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.TemperatureVariable;
import upatras.utilitylibrary.library.measurements.types.*;
import upatras.utilitylibrary.library.measurements.units.composite_units.HeatAbsorbingMaterial;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Paris
 */
public class RefrigeratorHA extends AbstractElectricalDevice {

    boolean is_on = false;
    boolean balancer = false;
    public Power watts;
    Temperature target_temperature = new Temperature(27);
    public final Volume volume;
    public final Area surface_area;
    protected AtomicInteger door_opened = new AtomicInteger();

    public final BasicEnviroment refrigerator_internal;
    public final BasicEnviroment refrigerator_external;
    TemperatureVariable external_temp;
    public TemperatureVariable internal_temp;

    double specific_heat;

    final double heating_efficiency = 0.1;
    final double thermal_conductivity_closed;
    final double thermal_conductivity_opened;
    public static Random rand = RandomGenerator.getRandom();
    int dt_millis = 1000;

    public RefrigeratorHA(BasicEnviroment parent_enviroment, Power watts, Volume volume, Temperature initial_temperature, double thermal_conductivity_closed) {
        super(parent_enviroment);

        refrigerator_external = parent_enviroment;
        external_temp = (TemperatureVariable) parent_enviroment.getVariable(TemperatureVariable.class);

        specific_heat += HeatAbsorbingMaterial.steel().getSpecificHeatM(new Mass(0.5));
        specific_heat += HeatAbsorbingMaterial.air().getSpecificHeatM(new Volume(1));
        specific_heat += HeatAbsorbingMaterial.meat().getSpecificHeatM(new Mass(5));

        refrigerator_internal = new BasicEnviroment(name + " internal environment", parent_enviroment.simulationinstance, volume,
                initial_temperature, specific_heat);

        this.dependsOn(refrigerator_internal);

        internal_temp = (TemperatureVariable) refrigerator_internal.getVariable(TemperatureVariable.class);

        this.volume = volume;
        this.watts = watts;

        double side_length = Math.cbrt(volume.value);
        this.surface_area = new Area(side_length * side_length * 8);
        this.thermal_conductivity_closed = thermal_conductivity_closed;
        this.thermal_conductivity_opened = thermal_conductivity_closed + 1;

        external_temp = (TemperatureVariable) parent_enviroment.getVariable(TemperatureVariable.class);
        internal_temp = (TemperatureVariable) refrigerator_internal.getVariable(TemperatureVariable.class);
        internal_temp.set(initial_temperature);
        target_temperature = initial_temperature;
    }

    @Override
    protected void processCommand(Command c) {

        switch (c.name) {
            case "Target Temperature":
                balancer = true;
                is_on = true;
                target_temperature = (Temperature) c.args[0];
                break;
            case "Turn On":
                is_on = true;
                balancer = false;
                break;
            case "Turn Off":
                is_on = false;
                balancer = false;
                break;
            case "Get item":
                this.submitCommand(new Command("Close door", c.time.plusSeconds(30 + rand.nextInt(90))));
                door_opened.incrementAndGet();
                break;
            case "Open door":
                balancer=true;
                door_opened.incrementAndGet();
                break;
            case "Close door":
                door_opened.decrementAndGet();
                break;
        }
    }

    @Override
    public void simulate(TimeStep simulation_step) {

        Duration actual_duration;

        long millisleft = simulation_step.duration.getMillis();

        Energy total_energy_used = new Energy(0);

        while (millisleft > 0) {
            if (millisleft > dt_millis) {
                actual_duration = Duration.millis(dt_millis);
                millisleft -= dt_millis;
            } else {
                actual_duration = Duration.millis(millisleft);
                millisleft = 0;
            }


            Energy energy_used;


            if (is_on) {
                if (balancer) {
                    if (internal_temp.get().value > target_temperature.value) {
                        energy_used = watts.toEnergy(actual_duration);
                    } else {
                        energy_used = new Energy();
                    }
                } else {
                    energy_used = watts.toEnergy(actual_duration);
                }


                if (energy_used.value > 0.001) {

                    total_energy_used = total_energy_used.plus(energy_used);

                    Energy energy_extracted_from_inside = energy_used.multipliedBy(heating_efficiency);

                    refrigerator_internal.useEnergyForTemperature(energy_extracted_from_inside.multipliedBy(-1));
                    refrigerator_external.useEnergyForTemperature(energy_used.plus(energy_extracted_from_inside));
                }
            }
            Energy energy_exchanged_through_walls;
            if (door_opened.get() == 0) {
                energy_exchanged_through_walls = HeatAbsorbingMaterial.heatTransfer(external_temp.get(), internal_temp.get(), surface_area, thermal_conductivity_closed, actual_duration);
            } else {
                energy_exchanged_through_walls = HeatAbsorbingMaterial.heatTransfer(external_temp.get(), internal_temp.get(), surface_area, thermal_conductivity_opened, actual_duration);
            }
            refrigerator_internal.useEnergyForTemperature(energy_exchanged_through_walls);
            refrigerator_external.useEnergyForTemperature(energy_exchanged_through_walls.multipliedBy(-1));


        }
        consumePower(total_energy_used, 1);

    }


    public Power wallPowerBleed() {
        return HeatAbsorbingMaterial.powerTransfer(external_temp.get(), internal_temp.get(), surface_area, thermal_conductivity_closed);
    }

    public Duration timeToTagetTemperature() {

        if (target_temperature.value > internal_temp.get().value) {

            Power watts_through_wall = wallPowerBleed();
            return HeatAbsorbingMaterial.timeToTemperature(watts_through_wall, target_temperature.minus(internal_temp.get()), specific_heat);
        } else {
            Power watts_through_wall = wallPowerBleed().minus(watts.multipliedBy(heating_efficiency));
            return HeatAbsorbingMaterial.timeToTemperature(watts_through_wall, target_temperature.minus(internal_temp.get()), specific_heat);
        }
    }

    public Duration timeToTemperature(Temperature target_temp) {

        if (target_temp.value > internal_temp.get().value) {

            Power watts_through_wall = wallPowerBleed();
            return HeatAbsorbingMaterial.timeToTemperature(watts_through_wall, target_temp.minus(internal_temp.get()), specific_heat);

        } else {

            Power watts_through_wall = wallPowerBleed().minus(watts.multipliedBy(heating_efficiency));
            return HeatAbsorbingMaterial.timeToTemperature(watts_through_wall, target_temp.minus(internal_temp.get()), specific_heat);
        }
    }

}
