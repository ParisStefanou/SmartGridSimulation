/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import upatras.utilitylibrary.library.measurements.Measurement;
import upatras.utilitylibrary.library.visualization.StaticPlot;
import upatras.utilitylibrary.library.visualization.StreamingPlot;

/**
 *
 * @author Paris
 */
public class TestGraph {

	@Test
	public void testStaticGraph() throws InterruptedException {

		ArrayList<Measurement> measurements = new ArrayList<>();

		DateTime starttime = new DateTime();
		for (int i = 0; i < 1000; i++) {

			measurements.add(new Measurement(Math.sin(i * Math.PI / 500.0), starttime));
			starttime = starttime.plusSeconds(1);
		}

		StaticPlot sp = new StaticPlot(measurements);
		sp.visualize().setTitle("Static");

		Thread.sleep(5000);
	}

	@Test
	public void testStreamingGraph() throws InterruptedException {

		StreamingPlot sp = new StreamingPlot();

		sp.visualize().setTitle("Streaming");

		DateTime starttime = new DateTime();
		for (int i = 0; i < 1000; i++) {
			sp.add(new Measurement(Math.sin(i * Math.PI / 500.0), starttime));
			starttime = starttime.plusSeconds(1);
			Thread.sleep(1);
		}
		Thread.sleep(1000);
	}
}
