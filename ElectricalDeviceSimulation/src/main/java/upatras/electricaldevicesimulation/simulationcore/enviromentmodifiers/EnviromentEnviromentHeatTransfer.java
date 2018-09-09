/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentmodifiers;

import org.joda.time.Duration;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironment;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalModifier;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.SpecificHeatVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.TemperatureVariable;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.measurements.types.Energy;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.types.Area;
import upatras.utilitylibrary.library.measurements.units.composite_units.HeatAbsorbingMaterial;

/**
 * @author Paris
 */
public class EnviromentEnviromentHeatTransfer extends AbstractEnvironmentalModifier {

    AbstractEnvironment from;
    AbstractEnvironment to;
    TemperatureVariable from_temp;
    TemperatureVariable to_temp;
    SpecificHeatVariable from_sh;
    SpecificHeatVariable to_sh;

    double thermal_conductivity;


    final Area surface_area;

    public EnviromentEnviromentHeatTransfer(AbstractEnvironment from, AbstractEnvironment to, double heat_transfer_coefficient, Area surface_area) {
        super(from.simulationinstance);

        this.name = from.name + " to " + to.name + " HT";
        this.from = from;
        this.to = to;
        this.thermal_conductivity = heat_transfer_coefficient;

        dependsOn(from);
        isDependedUpon(to);

        this.surface_area = surface_area;

        from_temp = (TemperatureVariable) from.getVariable(TemperatureVariable.class);
        to_temp = (TemperatureVariable) to.getVariable(TemperatureVariable.class);

        from_sh = (SpecificHeatVariable) from.getVariable(SpecificHeatVariable.class);
        to_sh = (SpecificHeatVariable) to.getVariable(SpecificHeatVariable.class);

    }

    @Override
    public void run(TimeStep simulation_step) {

        Duration actual_duration;

        long millisleft = simulation_step.duration.getMillis();

        while (millisleft > 0) {
            if (millisleft > 60000) {
                actual_duration = Duration.millis(60000);
                millisleft -= 60000;
            } else {
                actual_duration = Duration.millis(millisleft);
                millisleft = 0;
            }

            Energy energy_exchanged = HeatAbsorbingMaterial.heatTransfer(from_temp.get(), to_temp.get(), surface_area, thermal_conductivity, actual_duration);

            Temperature degchange1 = HeatAbsorbingMaterial.jouleTemperatureChange(energy_exchanged.multipliedBy(-1), from_sh.get().value);
            Temperature degchange2 = HeatAbsorbingMaterial.jouleTemperatureChange(energy_exchanged, to_sh.get().value);

            ((TemperatureVariable) from.getVariable(TemperatureVariable.class)).add(degchange1);
            ((TemperatureVariable) to.getVariable(TemperatureVariable.class)).add(degchange2);


        }

    }

}
