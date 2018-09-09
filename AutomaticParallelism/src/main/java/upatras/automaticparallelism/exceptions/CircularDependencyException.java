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
public class CircularDependencyException extends Exception {

    /** Thrown in case in the Dag's creation a circular dependency is found
     *
     */
    public CircularDependencyException() {
    }
    
}
