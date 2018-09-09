/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.environmentalvariables;

import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalVariable;
import upatras.utilitylibrary.library.measurements.types.Volume;

/**
 *
 * @author Paris
 */
public class VolumeVariable extends AbstractEnvironmentalVariable<Volume> {

    public VolumeVariable(double initialvolume) {
        unit = new Volume(initialvolume);
    }

    public VolumeVariable(Volume volume) {
        this.unit = new Volume(volume.value);
    }



}
