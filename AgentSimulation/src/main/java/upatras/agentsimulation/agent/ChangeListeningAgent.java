/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.agent;

import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.utilitylibrary.library.changelistener.ChangingMeasurableItem;
import upatras.utilitylibrary.library.changelistener.SimulationChangeListener;

/**
 *
 * @author Paris
 */
public abstract class ChangeListeningAgent<T extends ChangingMeasurableItem> extends AbstractAgent implements SimulationChangeListener {

	public final T changing_item;

	public ChangeListeningAgent(AgentSimulationInstance asi, T changing_item) {
		super(asi);
		this.changing_item = changing_item;
	}

}
