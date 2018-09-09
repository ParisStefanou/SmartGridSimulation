package upatras.electricaldevicesimulation.simulationcore.environmentalvariables;

import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalVariable;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.types.GenericDouble;
import upatras.utilitylibrary.library.measurements.units.composite_units.HeatAbsorbingMaterial;

import java.util.ArrayList;

public class SpecificHeatVariable extends AbstractEnvironmentalVariable<GenericDouble> {

    public SpecificHeatVariable(double specifc_heat_value) {
        set(new GenericDouble(specifc_heat_value));
    }

    public SpecificHeatVariable(ArrayList<HeatAbsorbingMaterial> materials, ArrayList<Volume> volumes) {

        double total_specific_heat = 0;

        for (int i = 0; i < materials.size(); i++) {
            total_specific_heat += materials.get(i).getSpecificHeatM(volumes.get(i));
        }
        set(new GenericDouble(total_specific_heat));
    }

    public SpecificHeatVariable(HeatAbsorbingMaterial material, Volume volume) {

        set(new GenericDouble(material.getSpecificHeatM(volume)));
    }

}
