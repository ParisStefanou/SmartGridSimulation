/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.event;

import org.joda.time.DateTime;
import upatras.agentsimulation.agent.AbstractAgent;

/**
 * @author Paris
 */
public class SelfTimeEvent<O extends AbstractAgent> extends TimeEvent<O,O> {

    public SelfTimeEvent(O origin, DateTime event_time) {
        super(origin, origin.toPopulation(), event_time);
    }

    public SelfTimeEvent(String name, O origin, DateTime event_time) {
        super(name, origin, origin.toPopulation(), event_time);
    }

}
