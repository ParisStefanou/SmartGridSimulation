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
public class EmptyDependantTask extends DependantTask {

    public EmptyDependantTask() {
        super();
    }

	    public EmptyDependantTask(String name) {
        super(name);
    }
    @Override
    public void run(TimeStep simulation_step) {
        int result = series(0, 1, 100);
    }

    public int series(int prev1, int prev2, int times) {
        times--;
        if (times > 0) {
            return series(prev1, prev2, times);
        } else {
            return prev1;
        }

    }

}
