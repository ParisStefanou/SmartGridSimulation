/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.agent;

import upatras.agentsimulation.event.Event;
import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.agentsimulation.visualization.AgentBehaviourGraph;
import upatras.automaticparallelism.main.TimeStep;
import upatras.automaticparallelism.tasks.Task;
import upatras.utilitylibrary.library.visualization.Visualizable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paris
 * @param <T> What type of agents this population consists of
 */
public class Population<T extends AbstractAgent> extends ArrayList<T> implements Visualizable {

	/**
	 * Allocate an arraylist for a population of type T without initializing
	 * them Usefully for taking samples of other populations or dynamically
	 * allocating them
	 *
	 */
	public Population() {
		super();
	}

	/**
	 * Allocate an array size agentcount for a population of type T without
	 * initializing them Usefully for taking samples of other populations or
	 * dynamically allocating them
	 *
	 * @param agentcount amount to allocate
	 */
	public Population(int agentcount) {
		super(agentcount);
	}

	/**
	 * Used for creating Agents of type T with their default constructor
	 *
	 * @param agentcount amount of agents to generate
	 * @param type type of agents to generate, note a default constructor is
	 * needed else a InstantiationException is thrown
	 */
	public Population(AgentSimulationInstance asi, int agentcount, Class<T> type) {
		super(agentcount);

		for (int i = 0; i < agentcount; i++) {
			try {
				add(type.newInstance());
			} catch (InstantiationException | IllegalAccessException ex) {
				System.err.println("failed to create instance of " + type.getSimpleName() + " there was no default constructor , please use an agent factory");
				Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * Used for creating Agents of type T with an AgentFactory for the cases
	 * where a more complex object constructor is needed
	 *
	 * @param agentcount amount of agents to generate
	 * @param factory an agentfactory that extends AbstractAgentFactory
	 */
	public Population(int agentcount, AbstractAgentFactory<T> factory) {
		super(agentcount);
		for (int i = 0; i < agentcount; i++) {
			add(factory.produceAgent());
		}
	}

	/**
	 * A method to return a specific amount of the population as a new
	 * population No new Agents are created but instead events can now be sent
	 * specifically to this subset
	 *
	 * @param <M> type of Agent this new population will contain, can be a
	 * subtype of the original
	 * @param count amount of agents the new population should contain
	 * @return A new Population
	 */
	public <M extends T> Population<M> getSample(int count) {

		Population<M> toreturn = new Population(count);

		for (int i = 0; i < count; i++) {
			add(toreturn.get(i));
		}

		return toreturn;
	}

	/**
	 * A method to return a specific amount of the population as a new
	 * population No new Agents are created but instead events can now be sent
	 * specificaly to this subset
	 *
	 * @param <M> type of Agent this new population will contain, can be a
	 * subtype of the original
	 * @param percentage percentage of original agents to return from 0 to 1.0
	 * @return A new Population
	 *
	 */
	public <M extends T> Population<M> getSamplePercentage(double percentage) {

		int agentstoassign = (int) Math.floor(size() * percentage);

		Population<M> toreturn = new Population(agentstoassign);
		for (int i = 0; i < agentstoassign; i++) {
			add(toreturn.get(i));
		}
		return toreturn;
	}

	/**
	 * Transfer an event to the containing Agents
	 *
	 * @param event
	 */
	public void resolve(Event event) {

		for (AbstractAgent agent : this) {
			agent.resolveEvent(event);
		}
	}

	/**
	 * Adds the Agents of another Population to this Population
	 *
	 * @param population Population containing Agents to add
	 */
	public void addPopulation(Population<T> population) {

		addAll(population);
	}

	/**
	 * Transfer an event to the Agents in parallel
	 *
	 * @param event
	 * @return return the array of tasks to run for a StreamingExecutor to
	 * consume
	 */
	public ArrayList<Task> parresolve(Event event) {
		ArrayList<Task> tasks = new ArrayList<>();

		for (AbstractAgent agent : this) {
			tasks.add(new Task() {
				@Override
				public void run(TimeStep simulation_step) {
					agent.resolveEvent(event);
				}
			});
		}

		return tasks;

	}

	@Override
	public JFrame visualize() {

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(getPanel());
		frame.setSize(1200, 800);
		frame.pack();
		frame.setVisible(true);

		return frame;
	}

	@Override
	public JPanel getPanel() {
		return new AgentBehaviourGraph<T>(this).getPanel();
	}

	private static class AgentAttachedTwiceException extends Exception {

		public AgentAttachedTwiceException() {
		}
	}

	private static class TooManyAgentsAssignedException extends Exception {

		public TooManyAgentsAssignedException() {
		}
	}

	/**
	 * Creates a population that is the sum of the populations inside the
	 * Collection
	 *
	 * @param populations A Collection of populations
	 * @return the summed Population of the populations
	 */
	public static Population joinPopulations(Collection<Population> populations) {

		int totalcount = 0;

		for (Population p : populations) {
			totalcount += p.size();
		}

		Population toreturn = new Population(totalcount);

		for (Population p : populations) {
			toreturn.addPopulation(p);
		}

		return toreturn;

	}

	/**
	 * Creates a population that is the sum of the other two
	 *
	 * @param pop1 first Population to join
	 * @param pop2 second Population to join
	 * @return the summed Population of the populations
	 */
	public static Population joinPopulations(Population pop1, Population pop2) {

		int totalcount = pop1.size() + pop2.size();

		Population toreturn = new Population(totalcount);

		toreturn.addPopulation(pop1);
		toreturn.addPopulation(pop2);

		return toreturn;

	}

	@Override
	public JFrame compressedVisualize() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public JPanel getCompressedPanel() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
