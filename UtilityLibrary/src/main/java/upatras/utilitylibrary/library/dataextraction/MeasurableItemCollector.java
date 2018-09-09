/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.dataextraction;

import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.utilitylibrary.library.changelistener.ChangingMeasurableItem;
import upatras.utilitylibrary.library.changelistener.SimulationChangeListener;
import upatras.utilitylibrary.library.measurements.Measurement;
import upatras.utilitylibrary.library.measurements.types.AbstractGenericDouble;
import upatras.utilitylibrary.library.measurements.types.GenericDouble;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;
import upatras.utilitylibrary.library.visualization.StaticPlot;
import upatras.utilitylibrary.library.visualization.Visualizable;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Paris
 */
public class MeasurableItemCollector implements Visualizable, SimulationChangeListener {

    final MeasurableItemExtractor evex;
    public ArrayList<Measurement> dataset = new ArrayList<>();

    public MeasurableItemCollector(ChangingMeasurableItem ev) {

        evex = new MeasurableItemExtractor(ev);
        evex.addListener(this);
    }

    public MeasurableItemCollector(MeasurableItemExtractor evex) {

        this.evex = evex;
        evex.addListener(this);
    }

    @Override
    public void itemchanged(DateTime instant) {
        dataset.add(evex.measurement);
    }

    public ArrayList<Measurement> compress(int time_slices) {

        Duration totalduration = new Duration(dataset.get(0).time, dataset.get(dataset.size() - 1).time);
        Duration spliceduration = totalduration.dividedBy(time_slices);

        ArrayList<Measurement> finalmeasurements = new ArrayList<>(10000);

        DateTime dtlimit = dataset.get(0).time.plus(spliceduration);

        int valueavgcounter = 0;
        double value = 0;
        double prevavg = 0;


        for (Measurement m : dataset) {


            if (m.time.isAfter(dtlimit)) {

                if (valueavgcounter > 0) {
                    prevavg = value / valueavgcounter;
                }

                while (m.time.isAfter(dtlimit)) {
                    dtlimit = dtlimit.plus(spliceduration);
                    finalmeasurements.add(new Measurement(new GenericDouble(prevavg), dtlimit.minus(spliceduration.dividedBy(2))));
                    value = 0;
                    valueavgcounter = 0;
                }
            }
            value += getYValue(m);
            valueavgcounter++;

        }
        finalmeasurements.add(new Measurement(new GenericDouble(value / valueavgcounter), dtlimit.minus(spliceduration.dividedBy(2))));

        return finalmeasurements;
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

    @Override
    public JFrame visualize() {

        JFrame toreturn = new StaticPlot(dataset).visualize();
        toreturn.setTitle(evex.ev.toString());
        return toreturn;
    }

    @Override
    public JPanel getPanel() {
        return new StaticPlot(dataset).getPanel();
    }

    public JFrame streamingVisualize() {

        JFrame toreturn = evex.visualize();
        toreturn.setTitle(evex.ev.toString());
        return toreturn;
    }

    public JPanel getStreamingPanel() {

        return evex.getPanel();

    }

    @Override
    public JFrame compressedVisualize() {
        JFrame toreturn = new StaticPlot(dataset).compressedVisualize();
        toreturn.setTitle(evex.ev.toString());
        return toreturn;
    }

    @Override
    public JPanel getCompressedPanel() {
        return new StaticPlot(dataset).getCompressedPanel();
    }

    public void toCSV(String filename, String path) {
        if (path == null) {
            path = ".";
        }
        if (filename == null) {
            filename = "data_measurements";
        }

        File f;
        if (FilenameUtils.getExtension(filename) == "csv") {
            f = new File(path + "/" + filename.trim());
        } else {
            f = new File(path + path + "/" + filename.trim() + ".csv");
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));

            for (Measurement value : dataset) {
                bw.write(value.data.toString() + "," + value.time.toString() + "\n");
            }
            bw.close();
            System.out.println("File " + filename + " written under " + f.getCanonicalPath());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void toCSV(String filename) {
        toCSV(filename, null);
    }

    public void toCSV() {
        toCSV(evex.ev.toString(), null);
    }

}
