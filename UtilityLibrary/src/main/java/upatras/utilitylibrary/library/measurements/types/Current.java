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
public class Current extends AbstractGenericDouble<Current> {
/**
     * For anything that can be measured in Amperes
     *
     * @param value initial value
     */
	public Current(double value) {
		super(value);
	}

	public Current() {
		super(0);
	}

	public Current(Current value) {
		super(value);
	}

	public static Current amperes(double A){
	    return new Current(A);
    }

    public static Current milliAmperes(double mA){
        return new Current(mA/1000.0);
    }

	@Override
	protected Current generateParentClass(double value) {
		return new Current(value);
	}

}
