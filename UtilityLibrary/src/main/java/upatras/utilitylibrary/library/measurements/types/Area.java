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
public class Area extends AbstractGenericDouble<Area> {

	/**
	 * For anything that can be measured in Cubic Meters
	 *
	 * @param value initial value
	 */
	public Area(double value) {
		super(value);
	}

	public Area() {
		super();
	}

	public Area(Area value) {
		super(value);
	}

	public static Area squareMeters(double sqm){
		return new Area(sqm);
	}


	@Override
	protected Area generateParentClass(double value) {
		return new Area(value);
	}

}
