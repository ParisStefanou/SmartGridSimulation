/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.visualization;

import org.jfree.data.xy.AbstractXYDataset;
import upatras.utilitylibrary.library.measurements.Measurement;
import upatras.utilitylibrary.library.measurements.types.AbstractGenericDouble;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

import java.util.ArrayList;

/**
 * @author Paris
 */
public class StreamingDataset extends AbstractXYDataset {

    ArrayList<Measurement> items = new ArrayList();

    Object readwritelock = new Object();

    /**
     * Allows thread safe addition of Measurements
     *
     * @param measurement
     */
    synchronized public void addMeasurement(Measurement measurement) {
        items.add(measurement);
    }

    /**
     * The dataset will always contain one series
     *
     * @return
     */
    @Override
    synchronized public int getSeriesCount() {

        return 1;
    }

    /**
     * To be used by JFreeChart as a key for the series
     *
     * @param i should always be 0 , otherwise an Exception is thrown
     * @return returns a key for JFreeChart
     */
    @Override
    public Comparable getSeriesKey(int i) {
        if (i > 1 || i < 0) {
            throw new IllegalArgumentException("Series index out of bounds");
        }
        return "series";
    }

    /**
     * @param i Should always be 0
     * @return The amount of items in the dataset
     */
    @Override
    synchronized public int getItemCount(int i) {

        return items.size();

    }

    /**
     * Get X axis value of item at a specific location in the series
     *
     * @param i   should always be 0
     * @param loc location of the item
     * @return value of X axis of item
     */
    @Override
    synchronized public Number getX(int i, int loc) {
        return items.get(loc).time.toInstant().getMillis();
    }

    /**
     * Get Y axis value of item at a specific location in the series
     *
     * @param i   should always be 0
     * @param loc location of the item
     * @return value of Y axis of item
     */
    @Override
    synchronized public Number getY(int i, int loc) {
        return getYValue(items.get(loc));

    }

    double getYValue(Measurement m) {
        if (m.data instanceof AbstractGenericDouble) {
            return ((AbstractGenericDouble) m.data).value;
        } else if (m.data instanceof ComplexPower) {
            return ((ComplexPower) m.data).real.value;
        } else if (m.data instanceof Double) {
            return (double) m.data;
        }
        return 0;
    }

    public int getSize(){
        return items.size();
    }
}
