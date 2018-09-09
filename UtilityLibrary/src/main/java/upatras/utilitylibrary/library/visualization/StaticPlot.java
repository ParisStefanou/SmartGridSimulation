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
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.utilitylibrary.library.measurements.Measurement;
import upatras.utilitylibrary.library.measurements.types.AbstractGenericDouble;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Paris
 */
public class StaticPlot<T extends Measurement> implements Visualizable {

	int splines = 1;

	ArrayList<Measurement> array_dataset = new ArrayList<>();
	DefaultXYDataset plot_dataset = new DefaultXYDataset();
	JFreeChart chart = null;

	JPanel normalchart;

	public ArrayList<Measurement> compress(ArrayList<Measurement> measurements, int timesplices) {

		if (measurements == null || measurements.isEmpty()) {
			return new ArrayList<>();
		}

		Duration totalduration = new Duration(measurements.get(0).time, measurements.get(measurements.size() - 1).time);
		Duration spliceduration = totalduration.dividedBy(timesplices);

		ArrayList<Measurement> finalmeasurements = new ArrayList<>(10000);

		DateTime dtlimit = measurements.get(0).time.plus(spliceduration);

		int valueavgcounter = 0;
		double value = 0;
		double prevavg = 0;

		double data;
		for (Measurement m : measurements) {

			if (m.time.isAfter(dtlimit)) {

				if (valueavgcounter > 0) {
					prevavg = value / valueavgcounter;
				}

				while (m.time.isAfter(dtlimit)) {
					dtlimit = dtlimit.plus(spliceduration);
					finalmeasurements.add(new Measurement(prevavg, dtlimit.minus(spliceduration.dividedBy(2))));
					value = 0;
					valueavgcounter = 0;
				}
			}
			value += getYValue(m);
			valueavgcounter++;

		}
		finalmeasurements.add(new Measurement(value / valueavgcounter, dtlimit.minus(spliceduration.dividedBy(2))));

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

	/**
	 * A StaicGraph will visualize the input dataset only once Far better
	 * performance than a StreamingDataset
	 *
	 * @param measurements measurement to visualize
	 */
	public StaticPlot(ArrayList<Measurement> measurements) {

		array_dataset = measurements;
		drawNormalChart();

	}

	private JFreeChart drawCompressedChart(int final_point_count) {

		JFreeChart generated_chart;

		XYSeries series = new XYSeries("series");
		for (Measurement m : compress(array_dataset, final_point_count)) {
			series.add(m.time.toInstant().getMillis(), getYValue(m));
		}
		plot_dataset.addSeries("", series.toArray());

		generated_chart = ChartFactory.createXYLineChart("", // title
				"Date", // x-axis label
				"Value", // y-axis label
				plot_dataset// data
		);

		generated_chart.setBackgroundPaint(Color.white);

		XYPlot plot = (XYPlot) generated_chart.getPlot();
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
		return generated_chart;
	}

	private void drawNormalChart() {

		if (chart == null) {
			chart = drawCompressedChart(10000);
		}
	}

	/**
	 * Old function for cursor results on graphs
	 *
	 * @param event
	 * @deprecated
	 */
	@Deprecated
	public void chartMouseMoved(ChartMouseEvent event) {
		/*Rectangle2D dataArea = chartpanel.getScreenDataArea();
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
		JFrame toreturn = new JFrame();
		toreturn.add(getPanel(), BorderLayout.CENTER);
		toreturn.setSize(1000, 800);
		toreturn.pack();
		toreturn.setVisible(true);
		toreturn.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		return toreturn;
	}

	@Override
	public JPanel getPanel() {

		ChartPanel static_panel = new ChartPanel(chart);
		static_panel.setPreferredSize(new java.awt.Dimension(1000, 800));
		static_panel.setMouseZoomable(true, false);

		return static_panel;
	}

	@Override
	public JFrame compressedVisualize() {
		JFrame toreturn = new JFrame();
		toreturn.add(getCompressedPanel(), BorderLayout.CENTER);
		toreturn.setSize(1000, 800);
		toreturn.pack();
		toreturn.setVisible(true);
		toreturn.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		return toreturn;
	}

	@Override
	public JPanel getCompressedPanel() {
		ChartPanel static_panel = new ChartPanel(drawCompressedChart(100));
		static_panel.setPreferredSize(new java.awt.Dimension(1000, 800));
		static_panel.setMouseZoomable(true, false);

		return static_panel;
	}

}
