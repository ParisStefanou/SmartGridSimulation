/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.exceptions;

/**
 *
 * @author Paris
 */
public class DependencyTreeHasAlreadyBeenCreated extends Exception {

    /**Thrown if the builder has already created the Dag
     *
     */
    public DependencyTreeHasAlreadyBeenCreated() {
    }
    
}
