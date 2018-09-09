/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.test;

import upatras.utilitylibrary.library.distributions.Distribution;
import upatras.utilitylibrary.library.distributions.NormalDistribution;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Paris
 */
public class PlotDistribution extends JPanel {

    double[] data;
    Distribution d;
    double offset;
    double normalizer;
    double startx;
    double endx;
         
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        JFrame j = new JFrame();
        JPanel p = new PlotDistribution(new NormalDistribution(1,0), -2, 2);
        j.add(p);

        p.setSize(500, 500);
        j.setSize(500, 500);

        j.setVisible(true);
        p.setVisible(true);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     *
     * @param d
     * @param startx
     * @param endx
     */
    public PlotDistribution(Distribution d, double startx, double endx) {

        this.d=d;
        this.startx=startx;
        this.endx=endx;
        if (startx > endx) {
            double tmp = startx;
            startx = endx;
            endx = tmp;
            //todo
        }

        
        

    }

    private void normalizedata() {

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        
        
        for (int i = 0; i < data.length; i++) {
            if (data[i] > max) {
                max = data[i];
            }
            if (data[i] < min) {
                min = data[i];
            }

        }
        normalizer = max - min;
        offset = min;

        for (int i = 0; i < data.length; i++) {
            data[i] = (data[i] - min) / normalizer;
        }

    }

    @Override
    public void paint(Graphics g) {

        g.setColor(Color.blue);

        int width = getWidth();
        int height = getHeight();
        
        
        double step = (endx - startx)/width ;
        data = new double[width];
        for (int i = 0; i < width; i++) {
            data[i] = d.gety(startx + step * i);
        }

        normalizedata();
        
        for (int i = 0; i < data.length; i++) {
            g.drawLine( i, height, i, (int) (height - data[i] * height));
        }
        
         g.setColor(Color.red);
        for (int i = 0; i < data.length; i++) {
            g.drawOval(i, (int) (height - data[i] * height), 2, 2);
        }
    }
}
