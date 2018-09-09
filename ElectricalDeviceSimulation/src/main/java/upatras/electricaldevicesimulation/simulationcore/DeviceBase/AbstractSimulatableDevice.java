/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.DeviceBase;

import org.joda.time.DateTime;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * @author gaspa
 */
public abstract class AbstractSimulatableDevice extends SimulationTask {

    public boolean debug = false;

    private static long deviceid = 0;

    public final long id;

    public AbstractSimulatableDevice(DeviceSimulationInstance instance) {
        super(instance);
        id = deviceid++;
    }

    public TreeSet<Command> commandque = new TreeSet<>(new Comparator<Command>() {

        @Override
        public int compare(Command o1, Command o2) {


            if (o1.time == null) {
                return -1;
            } else if (o2.time == null) {
                return 1;
            }

            if (o1.time.equals(o2.time)) {
                return 0;
            } else if (o1.time.isAfter(o2.time)) {
                return 1;
            } else {
                return -1;
            }

        }
    });

    final public synchronized void submitCommand(Command c) {

        commandque.add(c);
    }

    final public synchronized void emptyCommandQueue() {
        commandque.clear();
    }

    final public synchronized void emptyCommandQueue(DateTime start, DateTime end) {

        ArrayList<Command> todelete = new ArrayList<>();

        for (Command c : commandque) {
            if (c.time != null && !c.time.isBefore(start) && !c.time.isAfter(end)) {
                todelete.add(c);
            }
        }
        commandque.removeAll(todelete);
    }

    @Override
    final public void run(TimeStep simulation_step) {
        simulate_with_commands(simulation_step);
    }

    private void simulate_with_commands(TimeStep simulation_step) {

        DateTime prev = simulation_step.step_start;
        DateTime end = simulation_step.step_end;

        while (commandque.size() > 0) {
            Command c = commandque.first();
            if (c.time == null) {
                c.time = prev;
            }
            if (c.time.isAfter(end)) {
                break;
            } else if (c.time.isAfter(prev)) {
                simulate(new TimeStep(prev, c.time));
                prev = c.time;
            } else {
                commandque.pollFirst();
                processCommand(c);
            }
        }
        simulate(new TimeStep(prev, end));
    }

    protected abstract void processCommand(Command c);

    public abstract void simulate(TimeStep simulation_step);

    public static long getDeviceCount() {
        return deviceid;
    }


}
