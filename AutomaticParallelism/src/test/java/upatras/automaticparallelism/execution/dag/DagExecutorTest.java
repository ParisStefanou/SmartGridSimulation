/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.execution.dag;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import upatras.automaticparallelism.main.TimeStep;
import upatras.automaticparallelism.tasks.DependantTask;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Paris
 */
public class DagExecutorTest {
	
	public DagExecutorTest() {
	}

	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void restoreStreams() {
		System.setOut(System.out);
		System.setErr(System.err);
	}

	@Test
	public void dependencyTest() {

		DagBuilder dag_builder = new DagBuilder();

		DependantTask t1 = new DependantTask() {
			@Override
			public void run(TimeStep simulation_step) {
				System.out.print("1");
			}
		};
		DependantTask t2 = new DependantTask() {
			@Override
			public void run(TimeStep simulation_step) {
				System.out.print("2");
			}
		};
		DependantTask t3 = new DependantTask() {
			@Override
			public void run(TimeStep simulation_step) {
				System.out.print("3");
			}
		};
		DependantTask t4 = new DependantTask() {
			@Override
			public void run(TimeStep simulation_step) {
				System.out.print("4");
			}
		};

		t4.dependsOn(t3);
		t3.dependsOn(t2);
		t2.dependsOn(t1);

		dag_builder.addTask(t4);
		dag_builder.addTask(t3);
		dag_builder.addTask(t2);
		dag_builder.addTask(t1);

		Dag dag = dag_builder.build();

		DagExecutor de = new DagExecutor(dag);
		outContent.reset();
		de.serialStep(new TimeStep(DateTime.now(), DateTime.now().plusMinutes(1)));

		assertEquals("1234", outContent.toString());

	}

	@Test
	public void parallelDependencyTest() {

		DagBuilder dag_builder = new DagBuilder();

		DependantTask[] t4 = new DependantTask[5];
		for (int i = 0; i < 5; i++) {
			t4[i] = new DependantTask() {
				@Override
				public void run(TimeStep simulation_step) {
					System.out.print("4");
				}
			};

			dag_builder.addTask(t4[i]);
		}

		DependantTask[] t3 = new DependantTask[5];
		for (int i = 0; i < 5; i++) {
			t3[i] = new DependantTask() {
				@Override
				public void run(TimeStep simulation_step) {
					System.out.print("3");
				}
			};
			t4[i].dependsOn(t3[i]);
			dag_builder.addTask(t3[i]);
		}

		DependantTask[] t2 = new DependantTask[5];
		for (int i = 0; i < 5; i++) {
			t2[i] = new DependantTask() {
				@Override
				public void run(TimeStep simulation_step) {
					System.out.print("2");
				}
			};
			t3[i].dependsOn(t2[i]);
			dag_builder.addTask(t2[i]);
		}

		DependantTask[] t1 = new DependantTask[5];
		for (int i = 0; i < 5; i++) {
			t1[i] = new DependantTask() {
				@Override
				public void run(TimeStep simulation_step) {
					System.out.print("1");
				}
			};
			t2[i].dependsOn(t1[i]);
			dag_builder.addTask(t1[i]);
		}

		Dag dag = dag_builder.build();

		DagExecutor de = new DagExecutor(dag);
		outContent.reset();
		de.serialStep(new TimeStep(DateTime.now(), DateTime.now().plusMinutes(1)));

		assertEquals("11111222223333344444", outContent.toString());

	}
}
