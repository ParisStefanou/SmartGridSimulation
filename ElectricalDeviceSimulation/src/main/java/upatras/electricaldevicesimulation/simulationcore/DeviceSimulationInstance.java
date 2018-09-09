package upatras.electricaldevicesimulation.simulationcore;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.automaticparallelism.execution.dag.DagBuilder;
import upatras.automaticparallelism.execution.dag.DagExecutor;
import upatras.automaticparallelism.main.TimeInstance;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.SimulationTask;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author valitsa-syros
 */
public class DeviceSimulationInstance extends TimeInstance {

    public enum RunMode {
        parallel, serial
    }

    public boolean debug = false;

    public RunMode rm;

    public DagExecutor dag_executor;
    private final DagBuilder dag_builder = new DagBuilder();

    Duration step_duration = Duration.standardSeconds(1);

    public DeviceSimulationInstance() {
        super(DateTime.now());
    }

    public DeviceSimulationInstance(DateTime start_datetime) {
        super(start_datetime);
    }

    public DeviceSimulationInstance setStepDuration(Duration step_duration) {
        this.step_duration = step_duration;
        return this;
    }

    public DeviceSimulationInstance setRunMode(RunMode runmode) {
        this.rm = runmode;
        return this;
    }

    public DeviceSimulationInstance preprocess() {

        dag_executor = new DagExecutor(dag_builder.build());
        if (debug) {
            System.out.println("Preprocessing done");
        }
        return this;
    }

    @Override
    public void advanceDuration(Duration duration) {

        if (dag_executor == null) {
            System.out.println("Preprocess was not run , will run now");
            preprocess();
        }

        DateTime start = present;

        DateTime target = present.plus(duration);

        int steps = 0;

        if (rm == null) {
            System.out.println("Runmode was not set, will use serial");
            setRunMode(RunMode.serial);
        }

        while (present.isBefore(target)) {

            steps++;

            current_simulation_step = new TimeStep(present, step_duration);
            if (current_simulation_step.step_end.isAfter(target)) {
                current_simulation_step = new TimeStep(present, target);
            }

            switch (rm) {
                case parallel:
                    parallelStep(current_simulation_step);
                    break;
                case serial:
                    linearStep(current_simulation_step);
                    break;

            }
            present = current_simulation_step.step_end;
        }

        if (debug) {

            DateTime end = DateTime.now();

            float GHz = (float) 1.7;

            long SimulationTasknum = dag_executor.dag.getSize();

            double stepmillis = (double) (end.getMillis() - start.getMillis());
            System.out.println("\nexec time = " + stepmillis / 1000.0 + " s , SimulationTasks : " + SimulationTasknum + " , steps : " + steps);
            System.out.println("cputime/SimulationTask = " + stepmillis / (SimulationTasknum * (1000000.0)) + " ns");

            System.out.println("cpucycles/SimulationTask = " + stepmillis / (SimulationTasknum * (1000000.0)) * GHz + " cycles");
        }

    }

    @Override
    public void advanceStep() {

        if (dag_executor == null) {
            System.out.println("Preprocess was not run , will run now");
            preprocess();
        }

        long startms = 0;
        if (debug) {
            startms = System.currentTimeMillis();
        }

        current_simulation_step = new TimeStep(present, step_duration);
        if (rm == null) {
            System.out.println("Runmode was not set, will use serial");
            setRunMode(RunMode.serial);
        }
        switch (rm) {
            case parallel:
                parallelStep(current_simulation_step);
                break;
            case serial:
                linearStep(current_simulation_step);
                break;

        }
        present = current_simulation_step.step_end;

        if (debug) {

            long endms = System.currentTimeMillis();

            float GHz = (float) 1.7;

            long SimulationTasknum = dag_executor.dag.getSize();

            double stepmillis = (double) (endms - startms);
            System.out.println("\nexec time = " + stepmillis / 1000.0 + " s , SimulationTasks : " + SimulationTasknum + " , steps : " + 1);
            System.out.println("cputime/SimulationTask = " + stepmillis / (SimulationTasknum * (1000000.0)) + " ns");

            System.out.println("cpucycles/SimulationTask = " + stepmillis / (SimulationTasknum * (1000000.0)) * GHz + " cycles");
        }

    }

    @Override
    public void advanceRealTime() {

        if (dag_executor == null) {
            System.out.println("Preprocess was not run , will run now");
            preprocess();
        }

        if (rm == null) {
            System.out.println("Runmode was not set, will use serial");
            setRunMode(RunMode.serial);
        }

        DateTime past_rt = DateTime.now();

        while (true) {

            DateTime present_rt = DateTime.now();
            Duration step_duration_rt = new Duration(past_rt, present_rt);
            current_simulation_step = new TimeStep(present, step_duration_rt);
            dag_executor.parallelStep(current_simulation_step);

            if (debug) {

                float GHz = (float) 1.7;

                long SimulationTasknum = dag_executor.taskSize();

                double stepmillis = (double) (step_duration_rt.getMillis());
                System.out.println("\nexec time = " + stepmillis / 1000.0 + " s , SimulationTasks : " + SimulationTasknum + " , steps : " + (1));
                System.out.println("cputime/SimulationTask = " + stepmillis / (SimulationTasknum * (1000000.0)) + " ns");

                System.out.println("cpucycles/SimulationTask = " + stepmillis / (SimulationTasknum * (1000000.0)) * GHz + " cycles");
            }
            past_rt = present_rt;
        }

    }

    private void linearStep(TimeStep simulation_step) {

        dag_executor.serialStep(simulation_step);

    }

    private void parallelStep(TimeStep simulation_step) {

        dag_executor.parallelStep(simulation_step);

    }

    public void addSimulationTask(SimulationTask simulation_task) {
        try {
            dag_builder.addTask(simulation_task);
        } catch (Exception ex) {
            Logger.getLogger(DeviceSimulationInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void shutdown() {
        if (dag_executor != null) {
            dag_executor.finalize();
        }
    }

}
