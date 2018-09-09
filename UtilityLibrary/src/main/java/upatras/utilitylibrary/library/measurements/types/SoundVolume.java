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
public class SoundVolume extends AbstractGenericDouble<SoundVolume> {

	/**
	 * For anything that can be measured in Decibels
	 *
	 * @param value initial value
	 */
	public SoundVolume(double value) {
		super(value);
	}

	public SoundVolume() {
		super(0);
	}

	public SoundVolume(SoundVolume value) {
		super(value);
	}

	public static SoundVolume decibels(double db){
		return new SoundVolume(db);
	}

	@Override
	protected SoundVolume generateParentClass(double value) {
		return new SoundVolume(value);
	}
}
