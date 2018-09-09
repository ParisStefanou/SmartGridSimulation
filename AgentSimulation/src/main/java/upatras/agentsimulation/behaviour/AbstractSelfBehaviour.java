/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.behaviour;

import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.event.Event;

/**
 *
 * @author Paris
 */
public abstract class AbstractSelfBehaviour<M extends AbstractAgent,T extends Event> extends AbstractBehaviour<T> {

    /**
     * The agent that both created the event and will be affected by it
     *
     */
    final public M self;

    /**
     * An event caused by an agent with the target being himself
     *
     * @param self the agent
     */
    public AbstractSelfBehaviour(M self) {
        this.self = self;
    }

}
