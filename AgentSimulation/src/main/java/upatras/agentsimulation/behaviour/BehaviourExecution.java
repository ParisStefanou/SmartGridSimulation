/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.behaviour;

import upatras.agentsimulation.event.Event;
import upatras.automaticparallelism.main.TimeStep;
import upatras.automaticparallelism.tasks.Task;

/**
 *
 * @author Paris
 */
public class BehaviourExecution extends Task {

    AbstractBehaviour b;
    Event e;

    /**Executes a specific behavior using an event
     *
     * @param behavior
     * @param event
     */
    public BehaviourExecution(AbstractBehaviour behavior, Event event) {
        this.b = behavior;
        this.e = event;
    }

    @Override
    public void run(TimeStep simulation_step) {
        b.execute(e);

    }

}
