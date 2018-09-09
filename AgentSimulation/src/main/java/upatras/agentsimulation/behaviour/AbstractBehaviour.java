/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.behaviour;

import upatras.agentsimulation.event.Event;

/**
 *
 * @author Paris
 * @param <T> The event to which the behavior should activate
 */
public abstract class AbstractBehaviour<T extends Event> {

    /**This method will be overriden by all subclasses
     * The action to take when the behavior activates
     *
     * @param event
     */
    public abstract void execute(T event);

    @Override
    public String toString() {
        return this.getClass().getSimpleName(); //To change body of generated methods, choose Tools | Templates.
    }

}
