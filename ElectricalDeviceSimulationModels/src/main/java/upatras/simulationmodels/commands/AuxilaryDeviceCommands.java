/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.commands;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.utilitylibrary.library.measurements.types.Power;

/**
 * @author Paris
 */
public class AuxilaryDeviceCommands {

    public static Command steady(DateTime time) {
        return new Command("Steady", time);
    }

    public static Command discharge(DateTime time, Power watts) {
        return new Command("ProducePower", time, watts);
    }

    public static Command charge(DateTime time, Power watts) {
        return new Command("ConsumePower", time, watts);
    }

    public static Command equalizeStorage(DateTime time,Power power) {
        return new Command("EqualizeStorage", time,power);
    }

    public static Command equalizeStorage(DateTime time,Duration duration) {
        return new Command("EqualizeStorage", time,duration);
    }

    public static Command dischargeFully(DateTime time,Power power) {
        return new Command("DischargeFully", time,power);
    }

    public static Command dischargeFully(DateTime time,Duration duration) {
        return new Command("DischargeFully", time,duration);
    }

    public static Command chargeFully(DateTime time,Power power) {
        return new Command("ChargeFully", time,power);
    }

    public static Command chargeFully(DateTime time,Duration duration) {
        return new Command("ChargeFully", time,duration);
    }
}
