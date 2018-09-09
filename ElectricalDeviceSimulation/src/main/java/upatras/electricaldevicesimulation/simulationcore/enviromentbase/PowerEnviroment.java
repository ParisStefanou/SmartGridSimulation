/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.enviromentbase;

import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentmodifiers.EnviromentEnviromentPowerTransfer;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerConsumptionVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerProductionVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerVariable;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author valitsa-syros
 */
public class PowerEnviroment extends AbstractEnvironment {

	@Override
	public void preprocess(TimeStep simulation_step) {
		super.preprocess(simulation_step);

		((PowerConsumptionVariable) getVariable(PowerConsumptionVariable.class)).zero();
		((PowerProductionVariable) getVariable(PowerProductionVariable.class)).zero();
		((PowerVariable) getVariable(PowerVariable.class)).zero();

	}

	public PowerEnviroment(DeviceSimulationInstance instance) {

		super(instance);

		attachVariable(new PowerProductionVariable());
		attachVariable(new PowerConsumptionVariable());
		attachVariable(new PowerVariable());

	}

	public PowerEnviroment(PowerEnviroment parent) {

		super(parent.simulationinstance);

		this.parent = parent;
		parent.children.add(this);

		attachVariable(new PowerProductionVariable());
		attachVariable(new PowerConsumptionVariable());
		attachVariable(new PowerVariable());

		new EnviromentEnviromentPowerTransfer(this, parent);

	}

	public void addProducedPower(ComplexPower power_to_add) {
		((PowerProductionVariable) getVariable(PowerProductionVariable.class)).add(power_to_add);
		((PowerVariable) getVariable(PowerVariable.class)).subtract(power_to_add);
	}

	public void addConsumedPower(ComplexPower power_to_add) {
		((PowerConsumptionVariable) getVariable(PowerConsumptionVariable.class)).add(power_to_add);
		((PowerVariable) getVariable(PowerVariable.class)).add(power_to_add);
	}

	@Override
	public void run(TimeStep simulation_step) {

	}

}
