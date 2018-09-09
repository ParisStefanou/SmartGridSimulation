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
public class Volume extends AbstractGenericDouble<Volume> {

	/**
	 * For anything that can be measured in Cubic Meters
	 *
	 * @param value initial value
	 */
	public Volume(double value) {
		super(value);
	}

	public Volume() {
		super(0);
	}

	public Volume(Volume value) {
		super(value);
	}

	public static Volume cubicMeters(double m3){
	    return new Volume(m3);
    }

    public static Volume liter(double liter){
        return new Volume(liter/1000.0);
    }

	@Override
	protected Volume generateParentClass(double value) {
		return new Volume(value);
	}

}
