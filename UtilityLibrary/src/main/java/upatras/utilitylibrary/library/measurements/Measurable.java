/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.measurements;

import org.joda.time.DateTime;

/**
 *
 * @author Paris
 */
public interface Measurable{

    /**
     * The implemeted function should take a snapshot of the variable based on
     * the Simulation Instance time
     *
     * @return
     */
    public Measurement getMeasurement(DateTime time);

}
