/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.dataset;

import org.joda.time.Duration;

/**
 *
 * @author Paris
 */
public class GenericDataset extends Dataset{

    public GenericDataset(double[] data, Duration[] timediffs){
        
        
        
    } 
    
    @Override
    public double getStepSum(double start_location, double steps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getStepAverage(double start_location, double steps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getAtLoc(double location) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getAreaBeneath(double start_location, double steps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
