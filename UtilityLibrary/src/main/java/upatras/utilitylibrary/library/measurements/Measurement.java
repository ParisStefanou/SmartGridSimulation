package upatras.utilitylibrary.library.measurements;

import org.joda.time.DateTime;
import upatras.utilitylibrary.library.measurements.types.AbstractGenericDouble;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @param <T> can be any custom type that contains usefull data
 * @author Paris
 */
public class Measurement {

    /**
     * The data of the measurement
     */
    public final Object data;

    /**
     * Time instant of the measurement
     */
    public final DateTime time;

    /**
     * A measurement is a snapshot of any variable of the simulation usefull for
     * visualization purposes
     *
     * @param data The data of the measurement
     * @param time Time instant of the measurement
     */
    public Measurement(Object data, DateTime time) {
        this.data = data;
        this.time = time;
    }

    /**
     * Conversion of the measurement to a double so it can be depicted on a graph
     * =
     *
     * @return unit converted to a double
     */

    @Override
    public String toString() {
        if (data instanceof AbstractGenericDouble) {
            return "value : " + ((AbstractGenericDouble) data).value + " at : " + time;
        } else {
            return "Complex object : " + data.toString() + " at : " + time;
        }
    }

}
