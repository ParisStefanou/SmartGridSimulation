/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.event;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.agent.Population;

/**
 * @author Paris
 */
public class TurnEvent<O extends AbstractAgent,T extends AbstractAgent> extends TimeEvent<O,T> {

    public final int turn;

    /**
     * If one wants to run the simulation using RealTimeAdvance this parameter
     * converts turn to a DateTime so for example setting turntotimemult to 1000
     * would mean each turn is translated to 1 second
     */
    public static int turntotimemult = 200;
    static final DateTime start = DateTime.now();

    public TurnEvent(O origin, Population<T> targets, int turn) {
        super(origin, targets, start.plus(new Duration(turntotimemult * turn)));
        this.turn = turn;
    }

    /**
     * A TurnEvent should be used when the simulations is taking place in
     * distinct turns
     *
     * @param name   A name based on which behaviors will activate on Agents
     * @param origin Agent causing the event
     * @param targets Population receiving the event
     * @param turn   turn of the simulation
     */
    public TurnEvent(String name, O origin, Population<T> targets, int turn) {
        super(name, origin, targets, start.plus(new Duration(turntotimemult * turn)));
        this.turn = turn;
    }

    @Override
    public int compareTo(OrderableEvent e) {

        return turn - ((TurnEvent) e).turn;
    }

}
