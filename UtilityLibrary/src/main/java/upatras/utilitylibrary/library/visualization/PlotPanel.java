package upatras.utilitylibrary.library.visualization;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCrosshairLabelGenerator;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;

/**
 * An example of a time series chart. For the most part, default settings are
 * used, except that the renderer is modified to show filled shapes (as well as
 * lines) at each data point.
 */
public class PlotPanel extends JPanel implements ChartMouseListener {

    /**
     * ChartPanel to add cursor following to
     *
     */
    public ChartPanel chartpanel;
    private Crosshair yCrosshair;
    private Crosshair xCrosshair;
    int splines;

    /**
     * DateTime of Measurement will appear here depending on cursor position
     *
     */
    public JTextField datetime_textfield = new JTextField();

    /**
     * Used for adding a cursor to a ChartPanel
     *
     * @param chartpanel
     */
    public PlotPanel(ChartPanel chartpanel) {

        add(chartpanel);
        setVisible(true);

    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        chartpanel.setSize(width - 10, height - 110);
    }

    private JPanel createContent(XYSeriesCollection dataset) {

        chartpanel.addChartMouseListener(this);
        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        yCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        //xCrosshair.setLabelGenerator());

        xCrosshair.setLabelVisible(false);
        yCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);
        chartpanel.addOverlay(crosshairOverlay);
        return chartpanel;
    }

    /**
     * On click method, does nothing
     *
     * @param event
     */
    @Override
    public void chartMouseClicked(ChartMouseEvent event) {
        // ignore
    }

    /**
     * For drawing the lines and taking the data on cursor movement
     *
     * @param event
     */
    @Override
    public void chartMouseMoved(ChartMouseEvent event) {
        Rectangle2D dataArea = chartpanel.getScreenDataArea();
        JFreeChart chart = event.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea,
                RectangleEdge.BOTTOM);
        double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);

        String date = new StandardCrosshairLabelGenerator("{0}", new SimpleDateFormat("MM-dd HH-mm").getNumberFormat()).generateLabel(xCrosshair);

        xCrosshair.setValue(x);
        yCrosshair.setValue(y);
    }

    public void show() {

        JFrame jf = new JFrame();
        jf.add(this);
        jf.setSize(this.getSize());
        jf.setVisible(true);

    }

}
