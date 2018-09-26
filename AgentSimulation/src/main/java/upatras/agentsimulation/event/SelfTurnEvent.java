package upatras.agentsimulation.event;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.agent.Population;

public class SelfTurnEvent<O extends AbstractAgent> extends TurnEvent<O,O> {


    public SelfTurnEvent(O origin, int turn) {
        super(origin, origin.toPopulation(), turn);
    }

    public SelfTurnEvent(String name, O origin, int turn) {
        super(name, origin, origin.toPopulation(), turn);
    }

}
