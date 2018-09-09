/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.types;

/**
 *
 * @author Paris
 */
public class GenericDouble extends AbstractGenericDouble<GenericDouble>{

	public GenericDouble(double value) {
		super(value);
	}


	public GenericDouble() {
		super();
	}

	public GenericDouble(GenericDouble value) {
		super(value);
	}

	@Override
	protected GenericDouble generateParentClass(double value) {
		return new GenericDouble(value);
	}
	
	
}
