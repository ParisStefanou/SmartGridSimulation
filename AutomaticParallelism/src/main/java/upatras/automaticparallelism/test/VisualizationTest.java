/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.test;

import upatras.automaticparallelism.execution.dag.Dag;
import upatras.automaticparallelism.execution.dag.DagBuilder;
import upatras.automaticparallelism.tasks.DependantTask;
import upatras.automaticparallelism.tasks.EmptyDependantTask;

import java.util.ArrayList;

/**
 *
 * @author Paris
 */
public class VisualizationTest {

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		int counter = 1000000;
		DagBuilder dag_builder = new DagBuilder();

		ArrayList<DependantTask> t1 = new ArrayList<>();
		ArrayList<DependantTask> t2 = new ArrayList<>();
		ArrayList<DependantTask> t3 = new ArrayList<>();
		DependantTask t4 = new EmptyDependantTask("t4");
		dag_builder.addTask(t4);

		for (int i = 0; i < counter / 30; i++) {
			EmptyDependantTask et = new EmptyDependantTask("t3");
			et.dependsOn(t4);
			t3.add(et);
		}
		for (int i = 0; i < counter / 3; i++) {
			EmptyDependantTask et = new EmptyDependantTask("t2");
			et.dependsOn(t3.get(i % (counter / 30)));
			et.dependsOn(t4);
			t2.add(et);
		}

		for (int i = 0; i < counter; i++) {
			EmptyDependantTask et = new EmptyDependantTask("t1");
			et.dependsOn(t3.get(i % (counter / 30)));
			et.dependsOn(t4);
			et.dependsOn(t2.get(i % (counter / 3)));
			t1.add(et);
		}

		dag_builder.addTask(t3);
		dag_builder.addTask(t2);
		dag_builder.addTask(t1);

		System.out.println("visualize");

		Dag dag = dag_builder.build();

		if(counter<1000)
		dag.visualize();
	}
}
