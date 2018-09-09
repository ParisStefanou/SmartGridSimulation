/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.agent;

/**
 * A base class for creating population generators
 *
 * @author Paris
 * @param <T> Type of agent to be generated
 */
public abstract class AbstractAgentFactory<T extends AbstractAgent> {

    /**
     * This function should be able to create and return exactly one agent when
     * called
     *
     * @return
     */
    public abstract T produceAgent();

    /**This will exactly count agents and add them to a Population
     *
     * @param count Amount of Agents to generate
     * @return The Population containing count Agents
     */
    public final Population<T> producePopulation(int count) {

        Population toreturn = new Population(count);

        for (int i = 0; i < count; i++) {
            toreturn.add(produceAgent());
        }
        return toreturn;

    }

}
