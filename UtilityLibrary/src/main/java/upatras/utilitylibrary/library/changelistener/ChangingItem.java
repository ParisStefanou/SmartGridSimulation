/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.changelistener;

/**
 *
 * @author Paris
 */
public interface ChangingItem{

    /**
     * Notifies all listeners upon a change
     *
     */
    void notifyListeners();

    /**
     * A listener can start receiving notifications for changes from this object
     * by calling this function once
     *
     * @param change_listener
     */
    void addListener(ChangeListener change_listener);
    
    /**
     * A listener can stop receiving notifications for changes from this object
     * by calling this function once
     *
     * @param change_listener
     */
     void removeListener(ChangeListener change_listener);

}
