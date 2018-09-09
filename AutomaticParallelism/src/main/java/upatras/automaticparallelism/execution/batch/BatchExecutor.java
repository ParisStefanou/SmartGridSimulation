/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.execution.batch;

import upatras.automaticparallelism.execution.batch.BatchThread.Mode;
import upatras.automaticparallelism.execution.dag.DagExecutor;
import upatras.automaticparallelism.main.TimeStep;
import upatras.automaticparallelism.synchronization.UpDownLock;
import upatras.automaticparallelism.tasks.Executor;
import upatras.automaticparallelism.tasks.Task;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Paris
 */
public class BatchExecutor<T extends Task> {

    private ExecutorService executor;
    private ArrayList<BatchThread> bts = new ArrayList<>();
    public final int threadcount;

    private UpDownLock active_threads_lock = new UpDownLock(0);

    public BatchExecutor() {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
        this.threadcount = Runtime.getRuntime().availableProcessors() * 4;

        for (int i = 0; i < threadcount; i++) {
            BatchThread bt = new BatchThread(active_threads_lock, threadcount, BatchThread.ConsumptionAlgorithm.log);
            bts.add(bt);
            executor.submit(bt);
        }
    }

    public BatchExecutor(int threadcount, BatchThread.ConsumptionAlgorithm consumption_algorithm) {
        executor = Executors.newFixedThreadPool(threadcount);
        this.threadcount = threadcount;

        for (int i = 0; i < threadcount; i++) {
            BatchThread bt = new BatchThread(active_threads_lock, threadcount, consumption_algorithm);
            bts.add(bt);
            executor.submit(bt);
        }
    }

    @Override
    public void finalize() {

        for (BatchThread bt : bts) {
            bt.shutdown();
        }

        synchronized (this) {
            try {
                this.wait(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BatchExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        executor.shutdownNow();

        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(DagExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void process(int threadcount, Batch batch, TimeStep simulation_step, Mode runmode) {

        active_threads_lock.subtract(threadcount);
        for (int i = 0; i < threadcount; i++) {
            bts.get(i).process(batch, simulation_step, runmode);
        }
        active_threads_lock.waitUntillValue(0);
    }

    public BatchExecutor parallelStep(TimeStep simulation_step, Batch current_batch, Mode mode) {

        if (current_batch == null) {
            System.out.println("there were no available batches");
            return this;
        }

        switch (mode) {

            case preprocess:
                process(threadcount, current_batch, simulation_step, Mode.preprocess);
                break;
            case run:
                process(threadcount, current_batch, simulation_step, Mode.run);
                break;
            case postprocess:
                process(threadcount, current_batch, simulation_step, Mode.postprocess);
                break;
            case all:
                process(threadcount, current_batch, simulation_step, Mode.preprocess);
                current_batch.resetLoc();
                process(threadcount, current_batch, simulation_step, Mode.run);
                current_batch.resetLoc();
                process(threadcount, current_batch, simulation_step, Mode.postprocess);
        }
        current_batch.resetLoc();
        return this;

    }

    public BatchExecutor serialStep(TimeStep simulation_step, Batch current_batch, Mode mode) {

        if (current_batch == null) {
            System.out.println("there were no available batches");
            return this;
        }

        switch (mode) {

            case preprocess:
                process(1, current_batch, simulation_step, Mode.preprocess);
                break;

            case run:
                process(1, current_batch, simulation_step, Mode.run);
                break;

            case postprocess:
                process(1, current_batch, simulation_step, Mode.postprocess);
                break;

            case all:
                process(1, current_batch, simulation_step, Mode.preprocess);
                current_batch.resetLoc();
                process(1, current_batch, simulation_step, Mode.run);
                current_batch.resetLoc();
                process(1, current_batch, simulation_step, Mode.postprocess);

                break;
        }
        current_batch.resetLoc();

        return this;
    }

}
