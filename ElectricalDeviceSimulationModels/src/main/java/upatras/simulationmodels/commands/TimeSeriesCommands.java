/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.commands;

import upatras.electricaldevicesimulation.simulationcore.commands.Command;

/**
 *
 * @author Paris
 */
public class TimeSeriesCommands{
    
    
    public static Command Start() {
        return new Command("Start", null);
    }

    public static Command Stop() {
        return new Command("Stop", null);
    }

    public static Command Reset() {
        return new Command("Reset", null);
    }

    public static Command Wait() {
        return new Command("Wait", null);
    }
    
    
}
