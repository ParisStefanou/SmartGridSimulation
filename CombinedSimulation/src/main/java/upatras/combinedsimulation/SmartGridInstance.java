/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.combinedsimulation;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.automaticparallelism.main.TimeInstance;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance.RunMode;

/**
 * @author Paris
 */
public class SmartGridInstance extends TimeInstance {

    /**
     * An instance of AgentSimulationInstance to be used by the Agent part of
     * the simulation
     */
    public AgentSimulationInstance asi;

    /**
     * An instance of DeviceSimulationInstance to be used by the Electrical
     * Device part of the simulation
     */
    public DeviceSimulationInstance dsi;

    /**
     * For debugging , prints some basic info
     */
    public boolean debug = false;

    Duration max_step_duration;
    Duration min_step_duration;

    public SmartGridInstance(DateTime starting_time) {
        super(starting_time);
        asi = new AgentSimulationInstance(present);
        dsi = new DeviceSimulationInstance(present);
    }

    public SmartGridInstance preprocess(RunMode runmode, Duration max_step_duration, Duration min_step_duration) {
        dsi.setRunMode(runmode).setStepDuration(max_step_duration).preprocess();

        this.max_step_duration = max_step_duration;
        if (max_step_duration.isLongerThan(min_step_duration) || max_step_duration.isEqual(min_step_duration))
            this.min_step_duration = min_step_duration;
        else {
            System.err.println("You gave a minstep that was longer than the maxstep");
            this.min_step_duration = max_step_duration;
        }
        return this;
    }

    /**
     * Advances for a specific duration stopping at every event of the
     * AgentSimulationInstance and then letting the DeviceSimulationInstance to
     * catch up
     *
     * @param duration Duration to advance
     */
    @Override
    public void advanceDuration(Duration duration) {

        long start = System.currentTimeMillis();
        DateTime target = present.plus(duration);

        long millis_counter = 0;
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        long millis = target.getMillis() - present.getMillis();
        long printing_millis = millis / 100;
        while (present.isBefore(target)) {

            DateTime next_loop = present.plus(max_step_duration);
            DateTime agent_bucket = present.plus(max_step_duration);
            DateTime agentdate;
            do {
                agentdate = asi.advanceOnceBeforeDateTime(next_loop);
            } while (agentdate.isBefore(agent_bucket));

            dsi.advanceToDateTime(agentdate);
            present = agent_bucket;

            millis_counter += max_step_duration.getMillis();

            if (millis_counter > printing_millis) {
                System.out.println("Current Date :" + fmt.print(present) + "Target Date :" + fmt.print(target));
                millis_counter -= printing_millis;
            }
        }

        System.out.println("advance took " + (System.currentTimeMillis() - start) / 1000.0 + " seconds");

    }

    /**
     * *Advances according to the real time stopping at every event of the
     * AgentSimulationInstance and then letting the DeviceSimulationInstance to
     * catch up
     */
    @Override
    public void advanceRealTime() {

        dsi.preprocess();
        present = DateTime.now();

        while (true) {

            DateTime future = DateTime.now();

            current_simulation_step = new TimeStep(present, future);

            advanceDuration(new Duration(present, future));

            present = future;
        }

    }

    @Override
    public void shutdown() {
        asi.shutdown();
        dsi.shutdown();
    }

    @Override
    public void advanceStep() {
        advanceDuration(max_step_duration);
    }

}
