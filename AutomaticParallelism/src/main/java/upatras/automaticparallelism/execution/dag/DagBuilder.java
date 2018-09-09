/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.execution.dag;

import upatras.automaticparallelism.tasks.DependantTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paris
 */
public class DagBuilder {

	ArrayList<DependantTask> unprocessed = new ArrayList<>(1000000);
	ArrayList<DependantTask> next_to_process;

	/**
	 * A value used for debugging , prints some basic information
	 *
	 */
	private final boolean debug = false;

	/**
	 * This class takes as an input all the uncategorized task that are meant to
	 * participate in a simulation and categorizes them to a specific depth
	 * based on their dependencies
	 *
	 */
	public DagBuilder() {
	}

	/**
	 * Adds a task that will participate in the simulation to the temporary
	 * storage
	 *
	 * @param task Task that will participate in the simulation
	 * @return The DagBuilder
	 */
	public DagBuilder addTask(DependantTask task) {

		unprocessed.add(task);

		return this;
	}

	/**
	 * Adds a Collection of tasks that will participate in the simulation to the
	 * temporary storage
	 *
	 * @param tasks Collection of Tasks that will participate in the simulation
	 * @return The DagBuilder
	 */
	public DagBuilder addTask(Collection<DependantTask> tasks) {

		for (DependantTask dt : tasks) {
			addTask(dt);
		}
		return this;
	}

	/**
	 * Builds the Dag from the tasks submitted for participation
	 *
	 * @return The Dag containing all the tasks
	 */
	final public Dag build() {

		Dag dag = null;
		try {
			dag = order();
		} catch (Exception ex) {
			Logger.getLogger(DagBuilder.class.getName()).log(Level.SEVERE, null, ex);
		}
		return dag;
	}

	private Dag order() throws Exception {

		if(debug)System.out.println("Ordering started with " + unprocessed.size() + " amount of tasks");
		long start_ms = System.currentTimeMillis();

		Dag dag = new Dag();
		int currdepth = 0;
		dag.tasksize = unprocessed.size();
		ArrayList<DependantTask> zerolevel = new ArrayList<>(dag.tasksize);

		int size = unprocessed.size();
		next_to_process = new ArrayList<>();
		// handle the tasks with no dependencies and initialize them at depth zero
		// if no dependencies exist above it set the level to zero
		// and prepare anything that could be at level one
		unprocessed.stream().filter(t -> t.depends_on_tasks.isEmpty()).forEach(t -> {
			t.dependency_depth = 0;
			zerolevel.add(t);
			for (DependantTask dependant_task : t.tasks_depend_upon) {
				if (getActualDepth(dependant_task) == 1) {
					next_to_process.add(dependant_task);
					dependant_task.dependency_depth = 1;
				}
			}
		});
		if(debug)System.out.println("Depth " + currdepth + " contains " + zerolevel.size() + " tasks");
		dag.ordered.add(zerolevel);

		while (!next_to_process.isEmpty()) // add the level we are sure about to the dag and prepare for the next one
		{
			currdepth++;
			dag.ordered.add(next_to_process);
			next_to_process.trimToSize();
			unprocessed = next_to_process;
			next_to_process = new ArrayList<>(dag.tasksize);

			if(debug)System.out.println("Depth " + currdepth + " contains " + unprocessed.size() + " tasks");

			for (int i = 0; i < unprocessed.size(); i++) {

				DependantTask t = unprocessed.get(i);
				// try to figure out the next potential tasks
				for (DependantTask dependant_task : t.tasks_depend_upon) {
					if (dependant_task.dependency_depth > currdepth + 1) {
						if (getActualDepth(dependant_task) == currdepth + 1) {
							dependant_task.dependency_depth = currdepth + 1;
							next_to_process.add(dependant_task);
						}
					}
				}

			}
		}

		long duration_ms = System.currentTimeMillis() - start_ms;

		if(debug)System.out.println("Ordering finished taking " + duration_ms / 1000.0 + "seconds");
		return dag;

	}

	private int getActualDepth(DependantTask task) {
		int actualdepth = 0;
		for (DependantTask t : task.depends_on_tasks) {
			actualdepth = Math.max(actualdepth, t.dependency_depth + 1);
			if (actualdepth > 100000000) {
				return 100000000;
			}
		}
		return actualdepth;
	}

	/**
	 * Prints the list of tasks that have been added up to now
	 *
	 */
	public void printAll() {
		for (int i = 0; i < unprocessed.size(); i++) {
			System.out.println(unprocessed.get(i).toDependencyString());
		}

	}

}
