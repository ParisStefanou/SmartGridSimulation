/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentmodifiers;

import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.AbstractEnvironmentalModifier;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerConsumptionVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerProductionVariable;

/**
 *
 * @author Paris
 */
public class EnviromentEnviromentPowerTransfer extends AbstractEnvironmentalModifier {

	PowerEnviroment from;
	PowerEnviroment to;

	public EnviromentEnviromentPowerTransfer(PowerEnviroment from, PowerEnviroment to) {
		super(from.simulationinstance);

		this.name = from.name + " to " + to.name + " PT";
		this.from = from;
		this.to = to;
		dependsOn(from);
		isDependedUpon(to);
	}

	@Override
	public void run(TimeStep simulation_step) {
		PowerConsumptionVariable consumption_from = (PowerConsumptionVariable) from.getVariable(PowerConsumptionVariable.class);
		to.addConsumedPower(consumption_from.get());

		PowerProductionVariable production_from = (PowerProductionVariable) from.getVariable(PowerProductionVariable.class);
		to.addProducedPower(production_from.get());

	}

}
