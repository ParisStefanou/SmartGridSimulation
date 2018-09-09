/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.dataextraction;

import org.joda.time.DateTime;
import upatras.utilitylibrary.library.changelistener.ChangeListener;
import upatras.utilitylibrary.library.changelistener.ChangingMeasurableItem;
import upatras.utilitylibrary.library.measurements.DataExtractor;
import upatras.utilitylibrary.library.visualization.StreamingPlot;

import javax.swing.*;
import java.util.ArrayList;

/**
 *
 * @author Paris
 */
public class MeasurableItemExtractor extends DataExtractor {

	ChangingMeasurableItem ev;

	public MeasurableItemExtractor(ChangingMeasurableItem ev) {
		this.ev = ev;
		ev.addListener(this);
	}

	ArrayList<ChangeListener> listeners = new ArrayList<>();

	@Override
	public void itemchanged(DateTime instant) {
		measurement =  ev.getMeasurement(instant);
		notifyListeners(instant);
		if (debug) {
			System.out.println("sampled variable : " + measurement);
		}
	}

	StreamingPlot sg = null;
	boolean debug = false;

	@Override
	public JFrame visualize() {
		if (sg == null) {
			sg = new StreamingPlot().attachToChangingItem(this);
		}
		return sg.visualize();
	}

	@Override
	public JPanel getPanel() {
		if (sg == null) {
			sg = new StreamingPlot().attachToChangingItem(this);
		}
		return sg.getPanel();
	}

	@Override
	public JFrame compressedVisualize() {
		if (sg == null) {
			sg = new StreamingPlot().attachToChangingItem(this);
		}
		return sg.compressedVisualize();
	}

	@Override
	public JPanel getCompressedPanel() {
		if (sg == null) {
			sg = new StreamingPlot().attachToChangingItem(this);
		}
		return sg.getCompressedPanel();
	}

}
