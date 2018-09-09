/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.dataset;

import org.joda.time.DateTime;
import upatras.utilitylibrary.library.measurements.Measurement;
import upatras.utilitylibrary.library.measurements.types.GenericDouble;
import upatras.utilitylibrary.library.visualization.StaticPlot;
import upatras.utilitylibrary.library.visualization.Visualizable;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author Paris
 */
public abstract class Dataset implements Visualizable {

    /**
     * Datapoints of the dataset
     */
    public int length;

    /**
     * For a specific amounts of steps in the dataset should get their sum
     *
     * @param start_location location to start from
     * @param steps          steps to take
     * @return sum accumulated
     */
    public abstract double getStepSum(double start_location, double steps);

    /**
     * For a specific amounts of steps in the dataset should get their average
     *
     * @param start_location location to start from
     * @param steps          steps to take
     * @return average counted
     */
    public abstract double getStepAverage(double start_location, double steps);

    /**
     * For a specific amounts of steps in the dataset should get the area below
     * them
     *
     * @param start_location location to start from
     * @param steps          steps to take
     * @return average counted
     */
    public abstract double getAreaBeneath(double start_location, double steps);

    /**
     * Should return the value at a specific location
     *
     * @param location
     * @return
     */
    public abstract double getAtLoc(double location);

    public StaticPlot datasetToPlot() {

        ArrayList<Measurement> l = new ArrayList();

        for (int i = 0; i < length; i++) {
            l.add(new Measurement(new GenericDouble(getStepAverage(i, 1)), new DateTime(i + 1)));
        }

        return new StaticPlot(l);

    }

    public StaticPlot datasetToCompressedStaticPlot() {
        int targetlength = 100;

        if (length < targetlength) {
            return datasetToPlot();
        } else {
            int actuallength = length;
            int compression = 1;
            while (actuallength > targetlength) {
                actuallength /= 2;
                compression *= 2;
            }

            ArrayList<Measurement> l = new ArrayList();

            for (int i = 0; i < actuallength; i++) {

                l.add(new Measurement(new GenericDouble(getStepAverage(i * compression, compression)), new DateTime(i + 1)));
            }

            return new StaticPlot(l);

        }
    }

    /**
     * Returns a JFrame visualizing all datapoints inside the dataset
     *
     * @return
     */
    public JFrame visualize() {

        return datasetToPlot().visualize();

    }

    /**
     * Returns a JPanel visualizing all datapoints inside the dataset
     *
     * @return
     */
    @Override
    public JPanel getPanel() {

        return datasetToPlot().getPanel();
    }

    @Override
    public JFrame compressedVisualize() {

        return datasetToCompressedStaticPlot().visualize();
    }

    @Override
    public JPanel getCompressedPanel() {
        return datasetToCompressedStaticPlot().getPanel();
    }
}
