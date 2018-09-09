/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.basic;

import org.joda.time.DateTime;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public abstract class VariableRampDevice extends AbstractElectricalDevice {

	final ComplexPower max_power;
	ComplexPower target_power = new ComplexPower();
	ComplexPower current_power;
	final ComplexPower ramp_rate_ms;

	public VariableRampDevice(ComplexPower initial_power, ComplexPower max_power, ComplexPower ramp_rate_ms, PowerEnviroment enviroment) {
		super(enviroment);
		this.max_power = max_power;
		this.ramp_rate_ms = ramp_rate_ms;
		current_power = initial_power;
		target_power = initial_power;

	}

	public final ComplexPower getMaxPower() {

		return max_power.clone();
	}

	public final ComplexPower getRemainingPower() {

		return max_power.minus(target_power);
	}

	public final ComplexPower getCurrentPower() {
		return target_power.clone();
	}

	public final ComplexPower getRampRateMillisecond() {
		return ramp_rate_ms.clone();
	}

	public final void increaseToMaxPower(DateTime time) {

		if (max_power.real.value > 0) {
			submitCommand(new Command("Increase power", time, max_power.minus(current_power)));
		} else {
			submitCommand(new Command("Increase power", time, current_power.multipliedBy(-1)));
		}

	}

	public void increasePower(DateTime time, ComplexPower power_increase) {

		submitCommand(new Command("Increase power", time, power_increase));

	}

	private void increasePowerFunction(Command e) {

		if (e.args[0] instanceof ComplexPower) {
			ComplexPower power_increase = (ComplexPower) e.args[0];

			if (power_increase.getRealAsDouble() < 0) {
				power_increase = new ComplexPower(0, power_increase.getImaginaryAsDouble());
			}
			if (power_increase.getImaginaryAsDouble() < 0) {
				power_increase = new ComplexPower(power_increase.getRealAsDouble(), 0);
			}

			double real_power_target;
			if (target_power.plus(power_increase).getRealAsDouble() < max_power.getRealAsDouble()) {
				real_power_target = target_power.plus(power_increase).getRealAsDouble();
			} else {
				real_power_target = max_power.getRealAsDouble();
			}

			double imaginary_power_target;
			if (target_power.plus(power_increase).getImaginaryAsDouble() < max_power.getImaginaryAsDouble()) {
				imaginary_power_target = target_power.plus(power_increase).getImaginaryAsDouble();
			} else {
				imaginary_power_target = max_power.getImaginaryAsDouble();
			}

			target_power = new ComplexPower(real_power_target, imaginary_power_target);
		}
	}

	public void decreaseToMinPower(DateTime time) {

		if (max_power.real.value > 0) {
			submitCommand(new Command("Decrease power", time, current_power));
		} else {
			submitCommand(new Command("Decrease power", time, max_power.minus(current_power).multipliedBy(-1)));
		}

	}

	public void decreasePower(DateTime time, ComplexPower power_decrease) {

		submitCommand(new Command("Decrease power", time, power_decrease));

	}

	private void decreasePowerFunction(Command e) {

		if (e.args[0] instanceof ComplexPower) {
			ComplexPower power_decrease = (ComplexPower) e.args[0];
			if (power_decrease.getRealAsDouble() < 0) {
				power_decrease = new ComplexPower(0, power_decrease.getImaginaryAsDouble());
			}
			if (power_decrease.getImaginaryAsDouble() < 0) {
				power_decrease = new ComplexPower(power_decrease.getRealAsDouble(), 0);
			}

			double real_power_target;
			if (target_power.getRealAsDouble() > power_decrease.getRealAsDouble()) {
				real_power_target = target_power.getRealAsDouble() - power_decrease.getRealAsDouble();
			} else {
				real_power_target = 0;
			}

			double imaginary_power_target;
			if (target_power.getImaginaryAsDouble() > power_decrease.getImaginaryAsDouble()) {
				imaginary_power_target = target_power.getImaginaryAsDouble() - power_decrease.getImaginaryAsDouble();
			} else {
				imaginary_power_target = 0;
			}
			target_power = new ComplexPower(real_power_target, imaginary_power_target);

			if (real_power_target > max_power.getRealAsDouble()) {
				System.out.println("wtf");
			}

		}
	}

	public void setPower(DateTime time, ComplexPower target_power) {

		submitCommand(new Command("Set power", time, target_power));

	}

	private void setPowerFunction(Command e) {

		if (e.args[0] instanceof ComplexPower) {
			ComplexPower power_target = (ComplexPower) e.args[0];
			if (target_power.getRealAsDouble() <= max_power.getRealAsDouble() && target_power.getRealAsDouble() >= 0
					&& target_power.getImaginaryAsDouble() <= max_power.getImaginaryAsDouble() && target_power.getImaginaryAsDouble() >= 0) {
				target_power = new ComplexPower(power_target);
			}
		}
	}

	public void turnOff(DateTime time) {
		submitCommand(new Command("Turn off", time));

	}

	@Override
	protected void processCommand(Command c) {
		switch (c.name) {
			case "Turn Off":
				target_power = new ComplexPower();
				break;
			case "Increase power":
				increasePowerFunction(c);
				break;
			case "Decrease power":
				decreasePowerFunction(c);
				break;
			case "Set power":
				setPowerFunction(c);
				break;
			default:
				break;
		}
	}

}
