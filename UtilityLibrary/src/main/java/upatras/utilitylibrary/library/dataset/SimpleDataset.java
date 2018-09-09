/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.dataset;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Paris
 */
public class SimpleDataset extends Dataset {

    final double[] dataset;
    public boolean debug;
    
    /**
     * A simple dataset for storing datapoints better memory performance than
     * optimized dataset
     *
     * @param data datapoints
     */
    public SimpleDataset(double[] data) {

        length = data.length;
        dataset = Arrays.copyOf(data, length + 1);
        dataset[length] = dataset[0];
    }

    /**
     * A simple dataset for storing datapoints better memory performance than
     * optimized dataset
     *
     * @param data datapoints
     */
    public SimpleDataset(ArrayList<Double> data) {

        length = data.size();
        this.dataset = new double[length + 1];

        for (int i = 0; i < length; i++) {
            dataset[i] = data.get(i);
        }
        dataset[length] = dataset[0];
    }

    @Override
    public double getStepSum(double start_location, double steps) {

        double sum = 0;
        for (int i = 0; i < steps; i++) {
            long startloc = ( (Math.round(start_location + i)) % (long) dataset.length);
            sum += dataset[(int) ((startloc + i) % dataset.length)];
        }

        return sum;

    }

    @Override
    public double getStepAverage(double start_location, double steps) {

        if (steps == 0) {
            return 0;
        }
        return getStepSum(start_location, steps) / steps;

    }

    //todo fix circle and maybe optimise for int location
    @Override
    public double getAtLoc(double location) {
        double moddedloc = location % length;

        int leftloc = (int) Math.floor(moddedloc);

        int rightloc = (int) Math.ceil(moddedloc);

        if (leftloc == rightloc) {
            return dataset[leftloc];
        } else {

            double leftweight = 1 - (moddedloc - leftloc);
            double rightweight = 1 - (rightloc - moddedloc);

            return dataset[leftloc] * leftweight + dataset[rightloc] * rightweight;
        }
    }

    @Override
    public double getAreaBeneath(double start_location, double steps) {

        if (steps < 1) {
            double v1 = getAtLoc(start_location);
            double v2 = getAtLoc(start_location + steps);

            return (v2 + v1) / 2 * steps;
        } else {
            int nextloc = (int) Math.ceil(start_location);
            double first_length = nextloc - start_location;

            double v1 = getAtLoc(start_location);
            double v2 = getAtLoc(nextloc);

            double first_step_area = (v2 + v1) / 2 * first_length;

            double rem_steps = steps - first_length;
            int rem_int_steps = (int) Math.floor(rem_steps);

            double int_step_area = 0;

            if (rem_int_steps > 0) {
                int_step_area = getStepAverage(nextloc, rem_int_steps) * rem_int_steps;
            }

            int last_int_loc = nextloc + rem_int_steps;
            double last_loc = start_location + steps;

            double v3 = getAtLoc(last_int_loc);
            double v4 = getAtLoc(last_loc);
            
            double last_length=last_loc-last_int_loc;

            double last_step_area = (v3 + v4) / 2 * last_length;

            return first_step_area + int_step_area + last_step_area;

        }

    }

	

}
