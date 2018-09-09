/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.tasks;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Paris
 */
public abstract class DependantTask extends Task {

    /**
     * The dependency depth will be used to allow this task to run in an
     * executor after all lesser depths are finished DO NOT CHANGE TO
     * INT.MAXVALUE IT WILL WRAP AROUND TO 0
     */
    public int dependency_depth = 1000000000;

    /**
     * Tasks that will have to be executed before this task runs
     */
    public List<DependantTask> depends_on_tasks = new ArrayList<>();

    /**
     * Tasks that will have to wait for this task to complete
     */
    public List<DependantTask> tasks_depend_upon = new ArrayList<>();

    /**
     * Creates a task with a name
     *
     * @param name
     */
    public DependantTask(String name) {
        super(name);
    }

    /**
     * Creates an anonymous task
     */
    public DependantTask() {
        super();
    }

    public Task setName(String name){
		this.name=name;
        return this;
    }
    
    /**
     * Adds a dependency to the depends_on task specifically the depends_on task
     * will have to finish before this task is started
     *
     * @param depends_on
     */
    final public void dependsOn(DependantTask depends_on) {
        depends_on_tasks.add(depends_on);
        depends_on.tasks_depend_upon.add(this);

    }

    /**
     * Adds a dependency to the depends_on task specifically the dependant task
     * will wait for this task to finish before starting
     *
     * @param dependant
     */
    final public void isDependedUpon(DependantTask dependant) {
        tasks_depend_upon.add(dependant);
        dependant.depends_on_tasks.add(this);

    }

    /**
     * Remove the dependence between the two tasks
     *
     * @param task1
     * @param task2
     */
    public static void breakdependency(DependantTask task1, DependantTask task2) {
        for (Task task : task1.depends_on_tasks) {
            if (task == task2) {
                task1.depends_on_tasks.remove(task2);
                for (Task othertask : task2.tasks_depend_upon) {
                    if (othertask == task1) {
                        task2.tasks_depend_upon.remove(task1);
                        return;
                    }
                }
            }
        }

        for (Task task : task2.depends_on_tasks) {
            if (task == task1) {
                task2.depends_on_tasks.remove(task1);
                for (Task othertask : task1.tasks_depend_upon) {
                    if (othertask == task2) {
                        task1.tasks_depend_upon.remove(task2);
                        return;
                    }
                }
            }
        }
    }

	@Override
    public String toString() {
        return name;
    }

    public String toDependencyString() {
        String out =  name + "\n";
        for (DependantTask dt : depends_on_tasks) {
            out += "   " + dt;
        }
        return out;
    }
}
