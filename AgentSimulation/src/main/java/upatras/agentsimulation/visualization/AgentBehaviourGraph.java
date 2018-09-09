/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.visualization;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.agent.Population;
import upatras.agentsimulation.behaviour.AbstractBehaviour;
import upatras.utilitylibrary.library.visualization.Visualizable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Paris
 * @param <T> Any class that has extended Agent
 */
public class AgentBehaviourGraph<T extends AbstractAgent> implements Visualizable {

    DirectedSparseGraph g;
    boolean graphchanged = true;
    

    /**
     * A graph depicting agent , event and behavior connections
     *
     */
    public AgentBehaviourGraph() {
        
    }
    
    /**
     * A graph depicting agent , event and behavior connections
     *
     * @param population the population from which to create a graph of behaviors
     */
    public AgentBehaviourGraph(Population<T> population) {
        for(T a: population){
            addAgent(a);
        }
    }

    ArrayList<T> agents = new ArrayList<>();

    /**
     * Add an agent to be scanned in the graph
     *
     * @param agent
     */
    public void addAgent(T agent) {
        agents.add(agent);
        graphchanged = true;
    }

    private void draw() {
        g = new DirectedSparseGraph();

        int edgecounter = 0;

        for (AbstractAgent a : agents) {
            g.addVertex(a);
            for (LinkedList<AbstractBehaviour> blist : a.behaviors.values()) {
                for (AbstractBehaviour b : blist) {
                    g.addVertex(a.toString() + b.toString());
                    g.addEdge(edgecounter++, a, a.toString() + b.toString());
                }
            }
        }

    }

    /**Returns a JFrame depicting the graph
     *
     * @return 
     */
    @Override
    public JFrame visualize() {

        JFrame frame = new JFrame("Simple Graph View 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(getPanel());
        frame.pack();
        frame.setSize(1200,800);
        frame.setVisible(true);

        return frame;
    }

    /**Returns a JPanel depicting the graph 
     *
     * @return
     */
    @Override
    public JPanel getPanel() {

        if (graphchanged) {
            draw();
        }
        Dimension preferredGraphSize = new Dimension(1000, 600);
        Layout<String, String> layout = new FRLayout<>(g, preferredGraphSize);
        VisualizationViewer<String, String> visualizationViewer = new VisualizationViewer<>(layout, preferredGraphSize);

        visualizationViewer.getRenderContext().setVertexLabelTransformer(
                new ToStringLabeller());
        visualizationViewer.getRenderContext().setEdgeLabelTransformer(
                new ToStringLabeller());

        GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(visualizationViewer);
        scrollPane.add(visualizationViewer);
        return scrollPane;
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
