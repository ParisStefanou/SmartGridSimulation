/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.utilitylibrary.library.changelistener.ChangingMeasurableItem;
import upatras.utilitylibrary.library.changelistener.SimulationChangeListener;
import upatras.utilitylibrary.library.measurements.Measurement;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Paris
 */
public class StreamingPlot implements Visualizable, SimulationChangeListener {

    JFreeChart chart;

    int splines = 1;
    boolean debug = false;

    StreamingDataset dataset = new StreamingDataset();

    ArrayList<JPanel> streaming_panel_containers = new ArrayList<>();

    ChangingMeasurableItem changing_item = null;
    RefreshThread refreshthread = new RefreshThread(new Duration(100), this);


    public StreamingPlot() {
        refreshthread.start();
    }

    /**
     * A limit for how often the JPanels should refresh even if data is getting
     * added faster
     *
     * @return
     */
    public StreamingPlot setMinimumRefreshInterval(Duration duration) {
        refreshthread.die = true;
        refreshthread = new RefreshThread(duration, this);
        return this;
    }


    /**
     * Adding a new Measurement to the StreamingDataset
     *
     * @param measurement
     */
    synchronized public void add(Measurement measurement) {
        dataset.addMeasurement(measurement);
        if (debug) {
            System.out.println("Measured :" + measurement);
        }
    }

    /**
     * Adding many measurement to the StreamingDataset
     *
     * @param measurements Collection of Measurements
     */
    synchronized public void add(Collection<Measurement> measurements) {

        for (Measurement m : measurements) {
            dataset.addMeasurement(m);
        }
    }

    /**
     * Attaches the StreamingPlot to a ChangingMeasurableItem getting a
     * Measurement whenever that item changes
     *
     * @param changing_item A ChangingMeasurableItem to extract data from
     * @return A StreamingPlot that updates whenever the item changes
     */
    public StreamingPlot attachToChangingItem(ChangingMeasurableItem changing_item) {
        changing_item.addListener(this);
        this.changing_item = changing_item;
        return this;
    }


    int olditems = 0;

    synchronized protected void refresh() {

        if (dataset.getSize() != olditems) {
            updateChart();
            olditems = dataset.getSize();
        }
    }


    private void updateChart() {

        chart = ChartFactory.createXYLineChart(
                "", // title
                "Date", // x-axis label
                "Value", // y-axis label
                dataset // data
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(0xffffe0));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(false);

        DateAxis dateAxis = new DateAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss"));
        plot.setDomainAxis(dateAxis);

        //XYSplineRenderer renderer = new XYSplineRenderer(splines);
        XYItemRenderer xyitemrenderer = plot.getRenderer();

        if (xyitemrenderer instanceof XYLineAndShapeRenderer) {
            final XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyitemrenderer;
            if (false) {
                renderer.setBaseShapesFilled(true);
                renderer.setBaseShapesVisible(true);
                renderer.setShapesVisible(true);
                renderer.setDrawOutlines(true);

                //sets the joint level size means the dot size which join two points on a graph 2f,3f
                Stroke stroke = new BasicStroke(
                        0.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
                renderer.setBaseOutlineStroke(stroke);
            }
        }

        for (JPanel jp : streaming_panel_containers) {
            JPanel cp = new ChartPanel(chart);

            jp.removeAll();
            jp.add(cp, BorderLayout.CENTER);

            jp.revalidate();
            Container p = jp.getParent();
            if (p instanceof JFrame) {
                JFrame parent = (JFrame) p;
                parent.pack();
            }
            cp.setVisible(true);

        }
        System.gc();
    }

    @Deprecated
    private void chartMouseMoved(ChartMouseEvent event) {
        /*  Rectangle2D dataArea = chartpanel.getScreenDataArea();
        JFreeChart chart = event.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea,
                RectangleEdge.BOTTOM);
        double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);

        String date = new StandardCrosshairLabelGenerator("{0}", new SimpleDateFormat("MM-dd HH-mm").getNumberFormat()).generateLabel(xCrosshair);

        xCrosshair.setValue(x);
        yCrosshair.setValue(y);*/
    }

    @Override
    public JFrame visualize() {

        if (chart == null) {
            updateChart();
        }

        JFrame streamingJFrame = new JFrame();
        streamingJFrame.add(getPanel(), BorderLayout.CENTER);

        streamingJFrame.setSize(1000, 800);
        streamingJFrame.pack();

        streamingJFrame.setVisible(true);
        streamingJFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        return streamingJFrame;
    }

    @Override
    public JPanel getPanel() {

        if (chart == null) {
            updateChart();
        }

        ChartPanel streaming_panel = new ChartPanel(chart);
        streaming_panel.setPreferredSize(new java.awt.Dimension(1000, 800));
        streaming_panel.setMouseZoomable(true, false);
        JPanel streaming_panel_container = new JPanel();
        streaming_panel_container.add(streaming_panel, BorderLayout.CENTER);

        streaming_panel_containers.add(streaming_panel_container);
        return streaming_panel_container;
    }

    @Override
    public void itemchanged(DateTime datetime) {
        add(changing_item.getMeasurement(datetime));
    }

    @Override
    public JFrame compressedVisualize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JPanel getCompressedPanel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

class RefreshThread extends Thread {
    Duration minimum_refresh_interval;
    StreamingPlot to_refresh;
    boolean die = false;

    public RefreshThread(Duration minimum_refresh_interval, StreamingPlot to_refresh) {
        this.minimum_refresh_interval = minimum_refresh_interval;
        this.to_refresh = to_refresh;
    }

    public void run() {
        while (!die) {
            try {
                Thread.sleep(minimum_refresh_interval.getMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            to_refresh.refresh();
        }
    }
}