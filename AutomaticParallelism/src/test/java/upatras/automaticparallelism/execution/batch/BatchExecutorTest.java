package upatras.automaticparallelism.execution.batch;

import org.joda.time.DateTime;
import org.junit.Test;
import upatras.automaticparallelism.main.TimeStep;
import upatras.automaticparallelism.tasks.Task;

import java.util.Random;

import static org.junit.Assert.*;

public class BatchExecutorTest {

    public static long[] cache;
    public static int taskcount;

    @Test
    public void optimise() {

        BatchExecutor<ProcessingTask> be = new BatchExecutor<ProcessingTask>(8, BatchThread.ConsumptionAlgorithm.log);


        taskcount = 128000;


        for (int k = 0; k < 5; k++) {

            cache = new long[taskcount];
            Batch tasks_to_run = new Batch();
            for (int i = 0; i < taskcount; i++) {
                tasks_to_run.addTask(new ProcessingTask());
            }
            long start = System.currentTimeMillis();
            be.parallelStep(new TimeStep(DateTime.now(), DateTime.now().plusMinutes(1)), tasks_to_run, BatchThread.Mode.all);
            long time = System.currentTimeMillis() - start;

            taskcount *= 2;
        }
    }

    @Test
    public void perfTestLog() {

        BatchExecutor<ProcessingTask> be = new BatchExecutor<ProcessingTask>(8, BatchThread.ConsumptionAlgorithm.log);


        taskcount = 128000;


        for (int k = 0; k < 5; k++) {

            cache = new long[taskcount];
            Batch tasks_to_run = new Batch();
            for (int i = 0; i < taskcount; i++) {
                tasks_to_run.addTask(new ProcessingTask());
            }
            long start = System.currentTimeMillis();
            be.parallelStep(new TimeStep(DateTime.now(), DateTime.now().plusMinutes(1)), tasks_to_run, BatchThread.Mode.all);
            long time = System.currentTimeMillis() - start;

            System.out.println("Log with " + taskcount + " items took " + time / 1000.0 + " s");
            taskcount *= 2;
        }
    }

    @Test
    public void perfTestAll() {

        BatchExecutor<ProcessingTask> be = new BatchExecutor<ProcessingTask>(8, BatchThread.ConsumptionAlgorithm.all);

        taskcount = 128000;


        for (int k = 0; k < 5; k++) {

            cache = new long[taskcount];
            Batch tasks_to_run = new Batch();
            for (int i = 0; i < taskcount; i++) {
                tasks_to_run.addTask(new ProcessingTask());
            }
            long start = System.currentTimeMillis();
            be.parallelStep(new TimeStep(DateTime.now(), DateTime.now().plusMinutes(1)), tasks_to_run, BatchThread.Mode.all);
            long time = System.currentTimeMillis() - start;

            System.out.println("Log with " + taskcount + " items took " + time / 1000.0 + " s");
            taskcount *= 2;
        }
    }

    @Test
    public void perfTestsmall() {

        BatchExecutor<ProcessingTask> be = new BatchExecutor<ProcessingTask>(8, BatchThread.ConsumptionAlgorithm.small);


        taskcount = 128000;


        for (int k = 0; k < 5; k++) {

            cache = new long[taskcount];
            Batch tasks_to_run = new Batch();
            for (int i = 0; i < taskcount; i++) {
                tasks_to_run.addTask(new ProcessingTask());
            }
            long start = System.currentTimeMillis();
            be.parallelStep(new TimeStep(DateTime.now(), DateTime.now().plusMinutes(1)), tasks_to_run, BatchThread.Mode.all);
            long time = System.currentTimeMillis() - start;

            System.out.println("Log with " + taskcount + " items took " + time / 1000.0 + " s");
            taskcount *= 2;
        }
    }


    private class ProcessingTask extends Task {
        @Override
        public void run(TimeStep simulation_step) {
            int sum = 1;
            int randcount=new Random().nextInt(20000);
            for (int i = 0; i < randcount ; i++) {
                sum += i;
            }
            cache[(int) this.id%taskcount]=sum;
        }
    }
}