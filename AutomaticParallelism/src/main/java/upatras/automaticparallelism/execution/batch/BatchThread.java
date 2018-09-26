/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.execution.batch;

import upatras.automaticparallelism.main.TimeStep;
import upatras.automaticparallelism.synchronization.UpDownLock;
import upatras.automaticparallelism.tasks.Task;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Paris
 */
public class BatchThread implements Runnable {


    /**
     * The executors created will be exactly as many as the virtual cores of the
     * system
     */
    public enum Mode {
        preprocess, run, postprocess, all
    }

    public enum ConsumptionAlgorithm {
        log, all, small
    }

    private final int threadcount;
    private final ConsumptionAlgorithm consumption_algorithm;

    private TimeStep simulation_Step;
    private Batch to_process_batch;
    private Mode runmode;
    private Semaphore run_barrier = new Semaphore(0);
    private UpDownLock threadsalive;
    private float percentage;
    private int minimum;

    boolean debug = false;
    AtomicBoolean shutdown = new AtomicBoolean(false);

    /**
     * A streaming task thread is a consumer of tasks that have been added to
     * the processing queue
     */
    public BatchThread(UpDownLock threadsalive, int threadcount, ConsumptionAlgorithm consumption_algorithm) {
        this.threadsalive = threadsalive;
        this.threadcount = threadcount;
        this.consumption_algorithm = consumption_algorithm;
    }

    synchronized public void process(Batch batch, TimeStep simulation_Step, Mode runmode) {
        this.to_process_batch = batch;
        this.runmode = runmode;
        this.simulation_Step = simulation_Step;

        this.minimum = (int) Math.max((batch.getSize() * 0.01), 10);
        this.percentage = (float) (1.0 / threadcount);

        if (debug) {
            System.out.println("streaming thread started , will handle " + batch.getSize() + " tasks");
        }
        run_barrier.release();
    }

    public void shutdown() {
        shutdown.set(true);
        run_barrier.release();
    }

    @Override
    public void run() {
        try {
            while (true) {


                try {
                    run_barrier.acquire();
                } catch (InterruptedException ex) {
                    Logger.getLogger(BatchThread.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (shutdown.get()) {
                    return;
                }

                VirtualTaskList<Task> taskstoexecute = null;


                while (true) {

                    switch (consumption_algorithm) {
                        case log:
                            taskstoexecute = to_process_batch.getPercentage(percentage, minimum);
                            break;
                        case all:
                            taskstoexecute = to_process_batch.getAll(threadcount);
                            break;
                        case small:
                            taskstoexecute = to_process_batch.getFlat(10);
                            break;
                    }

                    if (taskstoexecute == null) {
                        break;
                    }

                    for (int i = 0; i < taskstoexecute.getSize(); i++) {
                        Task t = taskstoexecute.get(i);
                        try {
                            switch (runmode) {
                                case preprocess:
                                    t.preprocess(simulation_Step);
                                    break;
                                case run:
                                    t.run(simulation_Step);
                                    break;
                                case postprocess:
                                    t.postprocess(simulation_Step);
                                    break;
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(BatchThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                threadsalive.add(1);

            }
        } catch (Exception ex) {
            System.out.println("A batchthread died with exception: " + ex.toString());
        }
    }

}


