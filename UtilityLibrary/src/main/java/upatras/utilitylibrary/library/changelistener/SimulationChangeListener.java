/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.changelistener;

import org.joda.time.DateTime;

/**
 *
 * @author Paris
 */
public interface SimulationChangeListener {

    /**
     * To be called when a change has occured so the listeners can act
     * accordingly
     */
    void itemchanged(DateTime instant);

}
