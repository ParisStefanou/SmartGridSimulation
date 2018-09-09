/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.distributions;

/**
 *
 * @author Paris
 */
public class UniformDistribution extends Distribution{
    
    double value;
    
    /** Instantiate a uniform distribution
     *
     * @param value
     */
    public UniformDistribution(double value){
        this.value=value;
    }

    /**get the y value for the according x value
     *
     * @param x
     * @return
     */
    @Override
    public double gety(double x) {
       return value;
    }
    
    
}
