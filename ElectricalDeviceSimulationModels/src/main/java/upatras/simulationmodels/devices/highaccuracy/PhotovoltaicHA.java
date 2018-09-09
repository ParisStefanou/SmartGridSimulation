/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.simulationmodels.devices.highaccuracy;

import org.joda.time.DateTime;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class PhotovoltaicHA extends AbstractElectricalDevice {

	final ComplexPower nominal_power;
	final double latitude = 39.128834d;
	final double lonitude = 39.128834d;

	public PhotovoltaicHA(PowerEnviroment enviroment, ComplexPower nominal_power) {
		super(enviroment);
		this.nominal_power = nominal_power;
	}

	@Override
	protected void processCommand(Command c) {

	}

	@Override
	public void simulate(TimeStep simulation_step) {

		ComplexPower production = getPVProduction(simulation_step.middle_point, 6000, 27);

		producePower(production);

	}

	//yy-mm-dd
	public ComplexPower getPVProduction(DateTime timeinstance, double luminocity, double temperature) {

		//hack
		if (timeinstance.hourOfDay().get() <= 3 || timeinstance.hourOfDay().get() >= 20) {
			return new ComplexPower();
		}


		ComplexPower calculated_production = new ComplexPower(getcurrentpower(solarPowerAtSpecificTimestamp(timeinstance, luminocity, false), 5.0, temperature), 0);

		if (calculated_production.getRealAsDouble() <= 0) {
			return new ComplexPower();
		} else {
			return calculated_production;
		}

	}

	public int get_n(DateTime date) {
		int i = date.getDayOfMonth();
		int j = date.getMonthOfYear();
		int[] month_n = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 301, 334};

		return month_n[j - 1] + i;

	}

	public double solarPowerAtSpecificTimestamp(DateTime timeinstance, double luminosity, boolean snowing) {

		int n = get_n(timeinstance);

		double d = 23.45 * Math.sin(Math.toRadians(360.0 * (284.0 + n) / 365.0));// se rad
		double d_rad = Math.toRadians(d);
		double latitude_rad = Math.toRadians(latitude);
		double Ws = Math.acos(-Math.tan(latitude_rad) * Math.tan(d_rad));// se rad xaris tokso cos
		double Ws_rad = Ws;

		double leftvalue = 0;
		double rightvalue = 0;

		// ypologismos energeias se 1 mera sto orio ths atmosfairas se orizontia epifaneia
		double Ho = (24.0 * 3600.0 / Math.PI) * 1353.0 * (1 + 0.033 * Math.cos(Math.toRadians(360.0 * n / 365.0)))
				* (Math.cos(latitude_rad) * Math.cos(d_rad) * Math.sin(Ws_rad) + Ws_rad * Math.sin(latitude_rad) * Math.sin(d_rad));

		double Kt = luminosity * 3600.0 / Ho;  //to H dinete se W*h to metatrepw se Power*sec=Energy
		double[] w_table = timeinstanceModifiers(timeinstance);
		for (int i = 0; i < 2; i++) {
			double Io = 12.0 * 3600.0 * 1353.0 / Math.PI * (1 + 0.033 * Math.cos(Math.toRadians(360.0 * n / 365.0)))
					* (Math.cos(latitude_rad) * Math.cos(d_rad) * (Math.sin(w_table[i + 1]) - Math.sin(w_table[i]))
					+ Math.sin(latitude_rad) * Math.sin(d_rad) * (w_table[i + 1] - w_table[i]));
			double I = Kt * Io;

			double Id;

			if (Kt < 0.35) {
				Id = I * (1 - 0.249 * Kt);
			} else if (Kt < 0.75 && Kt > 0.35) {

				Id = I * (1.557 - 1.84 * Kt);
			} else {

				Id = I * 0.177;
			}

			double Ib = I - Id;

			double w = (w_table[i] + w_table[i + 1]) / 2.0;
			double w_rad = w;

			// opou b h klish an den thn gnvrizoume thn fazoyme oso to gewgrafiko
			double Rb = (1 * Math.cos(d_rad) * Math.sin(w_rad) + 0 * Math.sin(d_rad))
					/ (Math.cos(latitude_rad) * Math.cos(d_rad) * Math.sin(w_rad) + w_rad * Math.sin(latitude_rad) * Math.sin(d_rad));

			//platos f 
			double p;
			if (snowing) {
				p = 0.7;
			} else {
				p = 0.2;
			}

			double It = Rb * Ib + Id * (1 + Math.cos(latitude_rad)) / 2.0 + I * p * (1 - Math.cos(latitude_rad)) / 2.0;
			// energeia se 30 '  se Energy/m^2
			double G = It / 1800.0;
			//isxys se Power/m ^ 2
			if (i == 0) {
				rightvalue = G;
			}
			if (i == 1) {
				leftvalue = G;
			}
		}

		double leftweight = timeinstance.minuteOfHour().get() % 30 / 30.0;
		double rightweight = 1 - leftweight;

		return leftweight * leftvalue + rightweight * rightvalue;
	}

	public double getnominalpower(double G, double Vwind, double Tambc) {

//Tamb thermokrasia peribalontos 
//Vwind taxythta anemou se m/sec
//P se watt
//G se watt/m^2
		double k = 20 + 6 * Vwind;
		double Tamb = Tambc + 273.15;
		double Tcell = (0.9 * G * (1 - 0.1) + k * Tamb) / k;
		return nominal_power.getRealAsDouble() * 1000 / (0.98 * 0.99 * (1 - 0.0045 * (Tcell - 298)) * G);

	}

	public double getcurrentpower(double G, double Vwind, double Tc) {//ok

//Tamb thermokrasia peribalontos 
//Vwind taxythta anemou se m/sec
//P se watt
//G se watt/m^2
		double k = 20 + 6 * Vwind;
		double Tamb = Tc + 273.15;
		double Tplaisiou = (0.9 * G * (1 - 0.1) + k * Tamb) / k;

		return nominal_power.getRealAsDouble() / (1000.0 / (0.98 * 0.99 * (1 - 0.0045 * (Tplaisiou - 298)) * G));

	}

	double[] timeinstanceModifiers(DateTime timestamp) {

		int starthalfhour = timestamp.hourOfDay().get() * 2;
		if (timestamp.minuteOfHour().get() >= 30) {
			starthalfhour += 1;
		}

		double startdeg = -12 * 15;

		startdeg += 7.5 * starthalfhour;
		double[] data = new double[3];

		data[0] = Math.toRadians(startdeg);
		startdeg += 7.5;
		data[1] = Math.toRadians(startdeg);
		startdeg += 7.5;
		data[2] = Math.toRadians(startdeg);
		return data;
	}

}
