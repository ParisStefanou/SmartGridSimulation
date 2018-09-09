/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.visualization;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import sun.management.Agent;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.event.Event;
import upatras.utilitylibrary.library.visualization.Visualizable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Paris
 */
public class EventRelationGraph implements Visualizable {

    ArrayList<Event> events = new ArrayList<>();
    HashMap<String, Boolean> filter = new HashMap<>();

    boolean graphchanged = true;
    public boolean debug = true;
    SparseMultigraph full_graph = new SparseMultigraph();
    SparseMultigraph compressed_graph = new SparseMultigraph();

    /**
     * Add an event for visualization
     *
     * @param e
     */
    synchronized public void addEvent(Event e) {
        events.add(e);
        graphchanged = true;
    }

    /**
     * Filter result to show a specific event
     *
     * @param eventname event name to show
     * @return
     */
    public EventRelationGraph filter(String eventname) {

        filter.put(eventname, true);
        graphchanged = true;
        return this;
    }

    private void draw() {
        compressedDraw();
    }

    private class Eventcounter {

        Event event;
        int counter = 0;
        AbstractAgent from;
        AbstractAgent to;

        Eventcounter(Event event, AbstractAgent from, AbstractAgent to) {
            this.event = event;
            this.to = to;
            this.from = from;
        }

        void increment() {
            counter++;
        }

    }

    private void compressedDraw() {

        compressed_graph = new SparseMultigraph();


        HashMap<String, AbstractAgent> unique_agents_types = new HashMap<>();
        for (Event event : events) {
            unique_agents_types.put(event.origin.getClass().getSimpleName(), event.origin);
            for (AbstractAgent target_agent : event.targets) {
                unique_agents_types.put(target_agent.getClass().getSimpleName(), target_agent);
            }
        }

        HashMap<String, Eventcounter> counted_events = new HashMap<>();
        for (Event event : events) {
            for (AbstractAgent target_agent : event.targets) {
                String edge_key = event.origin.getClass().getSimpleName() + target_agent.getClass().getSimpleName() + event.name;
                Eventcounter ec = counted_events.get(edge_key);
                if (ec == null) {
                    ec = new Eventcounter(event, event.origin, target_agent);
                    counted_events.put(edge_key, ec);
                    if (debug) System.out.println("new unique key :" + edge_key);
                }
                ec.increment();
            }
        }

        for (AbstractAgent a : unique_agents_types.values()) {
            if (debug) System.out.println("Agent vertice :" + a.getClass().getSimpleName());

            full_graph.addVertex(a.getClass().getSimpleName());
        }


        for (Eventcounter ec : counted_events.values()) {
            if (debug)
                System.out.println("Link between :" + ec.from.getClass().getSimpleName() + " and " + ec.to.getClass().getSimpleName() + " with name " + ec.event.name + ": " + ec.counter);
            compressed_graph.addEdge(ec.event.name + ": " + ec.counter, new Pair(ec.from.getClass().getSimpleName(), ec.to.getClass().getSimpleName()), EdgeType.DIRECTED);
        }


        if (debug) {
            int counter = 0;
            for (Eventcounter ec : counted_events.values()) {
                counter += ec.counter;
            }
            System.out.println("Found " + events.size() + " total events");
            System.out.println("Found " + counted_events.size() + " unique events");
        }

    }

    /**
     * Return a JFrame depicting the graph
     *
     * @return
     */
    @Override
    synchronized public JFrame visualize() {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Event graph visualization");
        frame.getContentPane().add(getPanel());
        frame.pack();
        frame.setVisible(true);

        return frame;
    }

    /**
     * Return a JPanel depicting the graph
     *
     * @return
     */
    @Override
    synchronized public JPanel getPanel() {
        if (graphchanged) {
            //draw();
            compressedDraw();
            graphchanged = false;
        }

        Dimension preferredGraphSize = new Dimension(1200, 800);
        Layout<String, String> layout = new CircleLayout<>(compressed_graph);
        VisualizationViewer<String, String> visualizationViewer = new VisualizationViewer<>(layout, preferredGraphSize);

        visualizationViewer.getRenderContext().setVertexLabelTransformer(
                new ToStringLabeller());
        visualizationViewer.getRenderContext().setEdgeLabelTransformer(
                new ToStringLabeller());

        GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(visualizationViewer);
        scrollPane.add(visualizationViewer);
        scrollPane.setVisible(true);

        return scrollPane;
    }

    //todo

    /**
     * Return a JFrame depicting the graph
     *
     * @return
     */
    @Override
    public JFrame compressedVisualize() {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Compressed Event graph visualization");
        frame.getContentPane().add(getCompressedPanel());
        frame.pack();
        frame.setVisible(true);

        return frame;
    }

    /**
     * Return a JPanel depicting the graph
     *
     * @return
     */

    @Override
    public JPanel getCompressedPanel() {
        if (graphchanged) {
            //  draw();
            compressedDraw();
            graphchanged = false;
        }

        Dimension preferredGraphSize = new Dimension(1200, 800);
        Layout<String, String> layout = new CircleLayout<>(compressed_graph);
        VisualizationViewer<String, String> visualizationViewer = new VisualizationViewer<>(layout, preferredGraphSize);

        visualizationViewer.getRenderContext().setVertexLabelTransformer(
                new ToStringLabeller());
        visualizationViewer.getRenderContext().setEdgeLabelTransformer(
                new ToStringLabeller());

        GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(visualizationViewer);
        scrollPane.add(visualizationViewer);
        scrollPane.setVisible(true);
        return scrollPane;
    }
}
