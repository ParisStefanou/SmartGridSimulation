/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.enviroment;

import org.joda.time.DateTime;
import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.agentsimulation.visualization.GridVisualizationJPanel;
import upatras.agentsimulation.visualization.SquareVisualizer;
import upatras.utilitylibrary.library.visualization.Visualizable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @param <T> The type of Objects this grid will contain
 * @author Paris
 */
public class GridEnviroment<T> extends AbstractAgentEnvironment implements Visualizable {

    /**
     * Width of the grid
     */
    final public int width;

    /**
     * Height of the grid
     */
    final public int height;
    Class<T> type;

    /**
     * Objects array of the grid
     */
    public Object[][] items;

    ArrayList<GridVisualizationJPanel> panels_to_update = new ArrayList<>();

    /**
     * A GridEnviroment is a representation of two dimensional space with
     * discrete locations
     *
     * @param asi    AgentSimulationInstance to use for event notifications
     * @param width  Width of the grid
     * @param height Height of the grid
     */
    public GridEnviroment(AgentSimulationInstance asi, int width, int height) {
        super(asi);
        this.width = width;
        this.height = height;
        items = new Object[width][height];
    }

    /**
     * Assign an object to a specific grid location
     *
     * @param x
     * @param y
     * @param object
     */
    public void put(int x, int y, T object, DateTime datetime) {

        items[x][y] = object;
        for (GridVisualizationJPanel panel : panels_to_update) {
            if (panel != null) {
                panel.itemchanged(datetime);
            }
        }
    }

    /**
     * Remove and get an object from a specific grid location
     *
     * @param x
     * @param y
     */
    public T pop(int x, int y, DateTime datetime) {


        T toreturn = (T) items[x][y];
        items[x][y] = null;
        for (GridVisualizationJPanel panel : panels_to_update) {
            if (panel != null) {
                panel.itemchanged(datetime);
            }
        }
        return toreturn;
    }

    /**
     * Get the object of a specific grid location
     *
     * @param x
     * @param y
     * @return
     */
    public T get(int x, int y) {

        return (T) items[x][y];

    }

    @Override
    public JFrame visualize() {

        JFrame jframe = new JFrame();

        jframe.setSize(1200, 800);
        jframe.add(getPanel());
        jframe.repaint();
        jframe.setVisible(true);

        return jframe;
    }

    SquareVisualizer square_visualizer = null;

    /**
     * Adds a SquareVisualizer thats needed when visualization is called
     *
     * @param square_visualizer the SquareVisualizer that translates squares into a color
     */
    public void submitSquareVisualizer(SquareVisualizer<T> square_visualizer) {
        this.square_visualizer = square_visualizer;
    }

    @Override
    public JPanel getPanel() {
        if (square_visualizer == null) {
            try {
                throw new Exception("submit a Square Visualizer first");
            } catch (Exception ex) {
                Logger.getLogger(GridEnviroment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        GridVisualizationJPanel to_return = new GridVisualizationJPanel(this, square_visualizer);
        panels_to_update.add(to_return);
        return to_return;

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
