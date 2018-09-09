/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.execution.dag;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import upatras.automaticparallelism.tasks.DependantTask;
import upatras.utilitylibrary.library.general;
import upatras.utilitylibrary.library.visualization.Visualizable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Paris
 */
final public class Dag implements Visualizable {

	/**
	 */
	protected ArrayList<ArrayList<DependantTask>> ordered = new ArrayList<>();

	/**
	 *
	 */
	protected int tasksize;

	/**
	 * An array with levels representing the order in which the events can run
	 *
	 */
	public Dag() {
	}

	/**
	 * Get amount of tasks inside the Dag
	 *
	 * @return the amount of tasks inside the Dag
	 */
	public int getSize() {
		return tasksize;
	}

	public ArrayList<DependantTask> getTasks(int depth) {
		return ordered.get(depth);

	}

	/**
	 * get the number of tasks at a specific depth
	 *
	 * @param depth the depth to get the task count from
	 * @return The number of tasks at a specific depth
	 */
	public long depthSize(int depth) {
		return ordered.get(depth).size();
	}

	/**
	 * get the total depth after the dag generation is finished
	 *
	 * @return The total depth
	 */
	public long depthCount() {
		return ordered.size();
	}

	/**
	 * Returns a task of a specific location from the categorized tree
	 *
	 * @param depth
	 * @param location
	 * @return
	 */
	public DependantTask get(int depth, int location) {
		return ordered.get(depth).get(location);
	}

	/**
	 * This function will visualize the declared dependencies in a graph
	 *
	 * @return a SparseMultigraph that can be visualized
	 */
	private DirectedSparseMultigraph dependencyTreeToGraph() {

		DirectedSparseMultigraph g = new DirectedSparseMultigraph();

		for (ArrayList<DependantTask> levelarray : ordered) {
			if (levelarray.size() > 0) {
				for (DependantTask task : levelarray) {
					g.addVertex(task);
				}
			}
		}
		for (ArrayList<DependantTask> levelarray : ordered) {
			for (DependantTask task : levelarray) {
				for (DependantTask childtask : task.depends_on_tasks) {
					g.addEdge(task.name + " - " + childtask.name, task, childtask);
				}
			}
		}

		return g;

	}

	/**
	 * This function will visualize the declared dependencies in a graph while
	 * joining similar classes into a single edge or vertice
	 *
	 * @return a SparseMultigraph that can be visualized
	 */
	private DirectedSparseMultigraph dependencyTreeToSimpleGraph() {

		DirectedSparseMultigraph g = new DirectedSparseMultigraph();

		HashMap<Class, HashMap<Class, Integer>> classmap = new HashMap<>();

		for (ArrayList<DependantTask> levelarray : ordered) {
			if (levelarray.size() > 0) {
				for (DependantTask task : levelarray) {

					HashMap<Class, Integer> classconnectionsmap;
					classconnectionsmap = classmap.get(task.getClass());

					if (classconnectionsmap == null) {
						classconnectionsmap = new HashMap<>();
						classmap.put(task.getClass(), classconnectionsmap);
					}
					for (DependantTask childtask : task.depends_on_tasks) {
						Integer classconnections;
						classconnections = classconnectionsmap.get(childtask.getClass());

						if (classconnections == null) {
							classconnectionsmap.put(childtask.getClass(), 1);
						} else {
							classconnectionsmap.put(childtask.getClass(), classconnections + 1);
						}

					}

				}
			}
		}

		for (Map.Entry<Class, HashMap<Class, Integer>> vertice : classmap.entrySet()) {
			g.addVertex(general.getClassShortName(vertice.getKey()));
		}

		for (Map.Entry<Class, HashMap<Class, Integer>> vertice : classmap.entrySet()) {

			for (Map.Entry<Class, Integer> edge : vertice.getValue().entrySet()) {
				EdgeName edgename = new EdgeName(edge.getValue().toString());
				g.addEdge(edgename, general.getClassShortName(edge.getKey()), general.getClassShortName(vertice.getKey()));
			}

		}
		return g;

	}

	private class EdgeName {

		String name;

		public EdgeName(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}

	}

	@Override
	public String toString() {
		String toreturn = "";

		for (int i = 0; i < ordered.size(); i++) {
			toreturn += "At depth " + i + " found " + ordered.get(i).size() + " tasks \n";
			for (int k = 0; k < ordered.get(i).size(); k++) {
				toreturn += ordered.get(i).get(k) + "\n";
			}
		}

		return toreturn;
	}

	@Override
	public JFrame visualize() {

		JFrame jf = new JFrame();
		jf.getContentPane().add(getPanel(), BorderLayout.CENTER);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.setTitle("Dag visualization");
		jf.setSize(1000, 720);
		jf.setVisible(true);
		return jf;

	}

	@Override
	public JPanel getPanel() {
		DirectedSparseMultigraph g = dependencyTreeToGraph();
		Layout l = new CircleLayout(g);
		VisualizationViewer vv = new VisualizationViewer(l);
		vv.getRenderingHints().remove(RenderingHints.KEY_ANTIALIASING);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		// add a listener for ToolTips
		vv.setVertexToolTipTransformer(new ToStringLabeller());

		GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);

		final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

		vv.setGraphMouse(graphMouse);
		return panel;

	}

	/**
	 * Visualize the dependencies while compressing Objects that belong to the
	 * same class into a single object
	 *
	 * @return A JFrame depicting the compressed objects
	 */
	@Override
	public JFrame compressedVisualize() {

		JFrame jf = new JFrame();
		jf.getContentPane().add(getCompressedPanel(), BorderLayout.CENTER);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.setTitle("Compressed Dag visualization");
		jf.setSize(1000, 720);
		jf.setVisible(true);
		return jf;
	}

	/**
	 * Visualize the dependencies while compressing Objects that belong to the
	 * same class into a single object
	 *
	 * @return A JPanel depicting the compressed objects
	 */

	@Override
	public JPanel getCompressedPanel() {
		DirectedSparseMultigraph g = dependencyTreeToSimpleGraph();
		Layout l = new CircleLayout(g);
		VisualizationViewer vv = new VisualizationViewer(l);
		vv.getRenderingHints().remove(RenderingHints.KEY_ANTIALIASING);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);

		final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

		vv.setGraphMouse(graphMouse);
		return panel;
	}

}
