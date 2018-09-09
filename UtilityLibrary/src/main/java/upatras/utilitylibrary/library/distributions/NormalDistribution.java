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
public class NormalDistribution extends Distribution{

    
    double s;
    double m;
    
    /**Instantiates a normal distribution
     *
     * @param s
     * @param m
     */
    public NormalDistribution(double s, double m) {
        this.s = s;
        this.m = m;
    }
    
    /**Gets the y value for an x value
     *
     * @param x
     * @return
     */
    @Override
    public double gety(double x) {
       
        return 1/(Math.sqrt(2*s*s*Math.PI))*Math.exp(-(x-m)*(x-m)/(2*s*s));
    
    }
    
    
    
}
