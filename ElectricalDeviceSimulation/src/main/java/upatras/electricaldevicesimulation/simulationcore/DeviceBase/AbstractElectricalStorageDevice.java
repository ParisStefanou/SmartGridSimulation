/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.electricaldevicesimulation.simulationcore.DeviceBase;

import org.joda.time.Duration;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.utilitylibrary.library.measurements.types.Energy;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public abstract class AbstractElectricalStorageDevice extends AbstractElectricalDevice {

	final Energy max_energy;
	Energy current_energy = new Energy();

	final ComplexPower max_energy_output;
	final ComplexPower max_energy_input;

	public AbstractElectricalStorageDevice(Energy max_storable_energy, ComplexPower max_energy_output, ComplexPower max_energy_input, PowerEnviroment enviroment) {
		super(enviroment);
		this.max_energy = max_storable_energy;
		this.max_energy_output = max_energy_output;
		this.max_energy_input = max_energy_input;
	}

	public void setCurrentEnergy(Energy target_energy) {

		if (target_energy.value < max_energy.value) {
			this.current_energy = target_energy;
		} else {
			this.current_energy = new Energy(max_energy);
		}
	}

	public Energy getCurrentEnergy() {

		return current_energy;
	}

	public Energy getRemainingEnergy() {

		return max_energy.minus(current_energy);
	}

	public Energy addEnergy(Energy energy_to_add) {

		if (getRemainingEnergy().value <= energy_to_add.value) {
			current_energy = current_energy.plus(energy_to_add);
			return new Energy();
		} else {
			Energy toreturn = energy_to_add.minus(getRemainingEnergy());
			current_energy = new Energy(max_energy);
			return toreturn;
		}
	}

	public Energy removeEnergy(Energy energy_to_remove) {

		if (current_energy.value >= energy_to_remove.value) {
			current_energy = current_energy.minus(energy_to_remove);
			return energy_to_remove;
		} else {
			Energy toreturn = new Energy(current_energy);
			current_energy = new Energy();
			return toreturn;
		}
	}

	public ComplexPower maxAvailablePowerProduction() {
		return max_energy_output;
	}

	public ComplexPower maxAvailablePowerConsumption() {
		return max_energy_input;
	}

	public Duration maxAvailableProductionDuration(ComplexPower amount) {
		long milliseconds = (long) (current_energy.value / amount.getApparentPower() * 1000.0);
		return Duration.millis(milliseconds);
	}

	public Duration maxAvailableConsumptionDuration(ComplexPower amount) {
		long milliseconds = (long) ((max_energy.minus(current_energy)).value / amount.getApparentPower() * 1000.0);
		return Duration.millis(milliseconds);
	}

}
