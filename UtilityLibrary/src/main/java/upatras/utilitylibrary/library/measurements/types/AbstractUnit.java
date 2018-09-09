/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements.types;

import org.joda.time.DateTime;
import upatras.utilitylibrary.library.measurements.Measurable;
import upatras.utilitylibrary.library.measurements.Measurement;

/**
 *
 * @author Paris
 */
public abstract class AbstractUnit<T extends AbstractUnit> implements Measurable {

	public abstract T plus(T d);
	public abstract T plus(double d);

	public abstract T minus(T d);
	public abstract T minus(double d);

	public abstract T multipliedBy(T d);
	public abstract T multipliedBy(double d);

	public abstract T dividedBy(T d);
	public abstract T dividedBy(double d);


    public abstract T clone();

	public Measurement getMeasurement(DateTime time)
	{
		return new Measurement(this.clone(),time);
	}

	public abstract String toString();
}
