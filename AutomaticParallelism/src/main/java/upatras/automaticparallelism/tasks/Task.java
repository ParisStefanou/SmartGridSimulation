/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.tasks;

import upatras.automaticparallelism.main.TimeStep;

/**
 *
 * @author Paris
 */
public abstract class Task {

    /**
     * A name for debug and tracking purposes
     */
    public String name;

    /**
     * A unique identifier of the task
     */
    public final long id;

    private static long taskid = 0;

    /**
     * A task is a function that will be processed by an execution thread name
     * is optional but very helpfull in visualizing and debugging
     *
     * @param name
     */
    public Task(String name) {
        id = taskid++;
        this.name = id + " " + name;
    }

    /**
     * A task is a function that will be processed by an execution thread
     *
     */
    public Task() {
        id = taskid++;
        name = id + " " + this.getClass().getSimpleName();
    }

    /**
     * This function will run globaly before the main execution it should be
     * used for reseting temporary variables
     */
    public void preprocess(TimeStep simulation_step) {
        ;
    }

    /**
     * This function is where the main processing should happen it is run in
     * according the dependencies specified
     */
    public abstract void run(TimeStep simulation_step);

    /**
     * This function will run globally after all tasks have finished the run
     * function it should be used for visualization
     */
    public void postprocess(TimeStep simulation_step) {
        ;
    }

    @Override
    public String toString() {
        return name;
    }

}
