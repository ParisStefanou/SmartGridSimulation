/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.units.electrical;

import upatras.utilitylibrary.library.measurements.types.AbstractGenericDouble;

/**
 *
 * @author Paris
 */
public class ApparentPower extends AbstractGenericDouble<ApparentPower> {

	public ApparentPower(ComplexPower complex_power) {
		super(Math.sqrt(Math.pow(complex_power.getRealAsDouble(), 2)
				+ Math.pow(complex_power.getImaginaryAsDouble(), 2)));

	}

	public ApparentPower(double value) {
		super(value);
	}

	public ApparentPower(ApparentPower value) {
		super(value);
	}

	@Override
	protected ApparentPower generateParentClass(double value) {
		return new ApparentPower(value);
	}

}
