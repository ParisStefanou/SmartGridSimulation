package upatras.automaticparallelism.execution.dag;

import upatras.automaticparallelism.execution.batch.Batch;
import upatras.automaticparallelism.execution.batch.BatchExecutor;
import upatras.automaticparallelism.execution.batch.BatchThread;
import upatras.automaticparallelism.main.TimeStep;
import upatras.automaticparallelism.tasks.DependantTask;
import upatras.automaticparallelism.tasks.Executor;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author valitsa-syros
 */
public class DagExecutor {

    /**
     *
     */
    boolean debug = false;

    /**
     * The dependency tree contains the categorized tasks based on their
     * dependencies
     */
    public final Dag dag;

    private final BatchExecutor be;

    /**
     * Dag execution is made to run a dag of tasks based on their dependencies
     * Each depth is run in parallel
     *
     * @param dag A Dag that contains tasks sorted in dependency levels
     */
    public DagExecutor(Dag dag) {
        be = new BatchExecutor();
        this.dag = dag;
    }

    @Override
    public void finalize() {
        be.finalize();
    }

    /**
     * The normal run that is fully parallel based on depth Each depth waits for
     * the previous ones to finish running
     *
     * @param simulation_step The step's parameters according to which to run
     *                        the simulation for one step
     */
    public void parallelStep(TimeStep simulation_step) {

        if (debug) {
            System.out.println("Started step");
        }

        ArrayList<Batch> batches = new ArrayList<>();

        for (ArrayList<DependantTask> tasks : dag.ordered) {
            Batch b = new Batch<>();
            b.addTasks(tasks);
            batches.add(b);
        }

        for (int i = batches.size() - 1; i >= 0; i--) {
            be.parallelStep(simulation_step, batches.get(i), BatchThread.Mode.preprocess);
        }

        for (int i = 0; i < batches.size(); i++) {
            be.parallelStep(simulation_step, batches.get(i), BatchThread.Mode.run);
        }

        for (int i = batches.size() - 1; i >= 0; i--) {
            be.parallelStep(simulation_step, batches.get(i), BatchThread.Mode.postprocess);
        }

    }

    /**
     * A run for debugging, the tasks are run serially to be used for testing
     * algorithm correctness prior to testing thread safety
     *
     * @param simulation_step The step's parameters according to which to run
     *                        the simulation for one step
     */
    public void serialStep(TimeStep simulation_step) {

        for (int k = 0; k < dag.ordered.size(); k++) {
            for (int i = 0; i < dag.ordered.get(k).size(); i++) {
                dag.ordered.get(k).get(i).preprocess(simulation_step);
            }
        }

        for (int k = 0; k < dag.ordered.size(); k++) {
            for (int i = 0; i < dag.ordered.get(k).size(); i++) {
                dag.ordered.get(k).get(i).run(simulation_step);
            }
        }

        for (int k = 0; k < dag.ordered.size(); k++) {
            for (int i = 0; i < dag.ordered.get(k).size(); i++) {
                dag.ordered.get(k).get(i).postprocess(simulation_step);
            }
        }
    }

    /**
     * Get the amount of tasks that this executor contains
     *
     * @return The amount of tasks that this executor contains
     */
    public int taskSize() {
        return dag.tasksize;
    }

    /**
     * Attaches a Dag for execution , only one can be attached at a time
     *
     * @param task_storage Dag to attach
     */
}
