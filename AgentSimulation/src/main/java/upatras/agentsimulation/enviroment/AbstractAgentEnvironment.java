/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.enviroment;

import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.main.AgentSimulationInstance;

import java.util.HashMap;

/**
 *
 * @author Paris
 */
public abstract class AbstractAgentEnvironment extends AbstractAgent {

    HashMap<String, AbstractEnvironmentVariable> variablemap = new HashMap<>();

    /**
     * This is the base class for environments describing the location of an
     * Agent
     *
     * @param asi AgentSimulationInstance needed for event timestamps , etc.
     */
    public AbstractAgentEnvironment(AgentSimulationInstance asi) {
        super(asi);
    }

    /**
     * Method to add a variable to the environment
     *
     * @param variable
     */
    public void add(AbstractEnvironmentVariable variable) {
        variablemap.put(variable.name, variable);
    }

    /**
     * Method to get a variable from the environment
     *
     * @param variable_name the name of the variable to look for
     * @return returns an EnvironmentVariable if the variable is found otherwise a null
     */
    public AbstractEnvironmentVariable get(String variable_name) {

        return variablemap.get(variable_name);
    }

    /**
     * Method to safely add a variable to the environment
     *
     * @param variable
     */
    public void addifnotexists(AbstractEnvironmentVariable variable) {

        variablemap.putIfAbsent(variable.name, variable);
    }

    /**
     * Method to remove a variable from the environment
     *
     * @param variable
     */
    public void remove(AbstractEnvironmentVariable variable) {

        variablemap.remove(variable.name);
    }

    /**
     * Method to remove a variable from the environment by its name
     *
     * @param variablename
     */
    public void remove(String variablename) {

        variablemap.remove(variablename);
    }

}
