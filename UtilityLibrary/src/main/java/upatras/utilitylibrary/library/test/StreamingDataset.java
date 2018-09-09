/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.test;

import org.joda.time.DateTime;
import upatras.utilitylibrary.library.measurements.Measurement;
import upatras.utilitylibrary.library.visualization.StreamingPlot;

import java.util.Random;

/**
 *
 * @author Paris
 */
public class StreamingDataset {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        StreamingPlot sg = new StreamingPlot();

        sg.visualize();

        Random rand = new Random();

        for (int i = 0; i < 1000; i++) {
            sg.add(new Measurement(rand.nextDouble(), DateTime.now()));

        }

    }

}
