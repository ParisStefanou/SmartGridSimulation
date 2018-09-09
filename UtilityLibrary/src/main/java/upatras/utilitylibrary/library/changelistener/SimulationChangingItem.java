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
public interface SimulationChangingItem{

    /**
     * Notifies all listeners upon a change
     *
     */
    void notifyListeners(DateTime instant);

    /**
     * A listener can start receiving notifications for changes from this object
     * by calling this function once
     *
     * @param change_listener
     */
    void addListener(SimulationChangeListener change_listener);
    
    /**
     * A listener can stop receiving notifications for changes from this object
     * by calling this function once
     *
     * @param change_listener
     */
    void removeListener(SimulationChangeListener change_listener);

}
