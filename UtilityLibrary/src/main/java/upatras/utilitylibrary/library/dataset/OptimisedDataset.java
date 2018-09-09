/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.dataset;

import java.util.ArrayList;

/**
 *
 * @author Paris
 */
public class OptimisedDataset extends Dataset {

    /**
     * The amount of leaves the next level will have is the amount of the
     * previous level/compression
     *
     */
    final public static int compression = 10;
    final double[] dataset;

    /**
     * The average of the whole dataset
     *
     */
    public final double avg;

    /**
     * The sum of the whole dataset
     *
     */
    public final double sum;
    final int maxcomploc;
    ArrayList<double[]> compressed_data = new ArrayList<>();

    /**
     * An OptimisedDataset acts like a dataset in storing datapoints but is
     * better when long simulation steps are taken since it also stores values
     * for multiple steps
     *
     *
     * @param data datapoints to create the dataset
     */
    public OptimisedDataset(double[] data) {
        this.dataset = data.clone();
        this.length = data.length;

        double temptotal = 0;
        for (int i = 0; i < dataset.length; i++) {
            temptotal += dataset[i];
        }

        sum = temptotal;
        avg = temptotal / dataset.length;

        compress(dataset);
        maxcomploc = dataset.length - (dataset.length % compression);
    }

    /**
     * An OptimisedDataset acts like a dataset in storing datapoints but is
     * better when long simulation steps are taken since it also stores values
     * for multiple steps
     *
     *
     * @param data datapoints to create the dataset
     */
    public OptimisedDataset(ArrayList<Double> data) {
        dataset = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            dataset[i] = data.get(i);
        }
        this.length = data.size();

        double temptotal = 0;
        for (int i = 0; i < dataset.length; i++) {
            temptotal += dataset[i];
        }

        sum = temptotal;
        avg = temptotal / dataset.length;

        compress(dataset);
        maxcomploc = dataset.length - (dataset.length % compression);

    }

    @Override
    public double getStepSum(double start_location, double steps) {
        return getcompressedsum((long) start_location, (long) steps);
    }

    @Override
    public double getStepAverage(double start_location, double steps) {
        return getcompressedsum((long) start_location, (long) steps) / steps;
    }

    private double getcompressedsum(long startloc, long totalsteps) {

        double toreturn = 0;

        long steps_left = totalsteps;

        if (steps_left >= dataset.length) {

            int rotations = (int) (steps_left / dataset.length);

            toreturn += rotations * sum;

            steps_left -= rotations * dataset.length;

        }

        int curr_loc = (int) (startloc % dataset.length);

        while (steps_left > 0) {

            int remainingcomp = (int) Math.min(maxcomploc - curr_loc, steps_left);

            while ((curr_loc % compression != 0 || curr_loc >= maxcomploc || remainingcomp < compression) && steps_left > 0) {
                toreturn += dataset[curr_loc];
                curr_loc++;
                steps_left--;
                remainingcomp--;
                if (curr_loc == dataset.length) {
                    curr_loc = 0;
                }

            }

            remainingcomp = (int) Math.min(maxcomploc - curr_loc, steps_left);

            while (remainingcomp >= compression) {
                final int level = (int) Math.floor(Math.log10(remainingcomp) / Math.log10(compression));

                toreturn += compressed_data.get(level - 1)[curr_loc / (int) Math.pow(compression, level)];

                steps_left -= Math.pow(compression, level);
                remainingcomp -= Math.pow(compression, level);
                curr_loc += Math.pow(compression, level);

                if (curr_loc == dataset.length) {
                    curr_loc = 0;
                }

            }

        }

        return toreturn;

    }

    private void compress(double[] datatocompress) {

        if (datatocompress.length >= compression) {

            double[] compresseddata = new double[datatocompress.length / compression];
            for (int i = 0; i < datatocompress.length / compression; i++) {
                compresseddata[i] = 0;
                for (int k = 0; k < compression; k++) {
                    compresseddata[i] += datatocompress[i * compression + k];
                }

            }
            compressed_data.add(compresseddata);
            compress(compresseddata);
        }

    }

    public double getAtLoc(double location) {
        int loc = (int) (location % (long) dataset.length);
        return dataset[loc];
    }

    @Override
    public double getAreaBeneath(double start_location, double steps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
