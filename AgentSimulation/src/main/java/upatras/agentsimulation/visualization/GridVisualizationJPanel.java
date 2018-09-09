/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.visualization;

import org.joda.time.DateTime;
import upatras.agentsimulation.enviroment.GridEnviroment;
import upatras.utilitylibrary.library.changelistener.SimulationChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Paris
 */
public class GridVisualizationJPanel extends JPanel implements SimulationChangeListener {

    final GridEnviroment grid;
    final SquareVisualizer grid_visualizer;

    /**
     * Helps visualizing a grid area
     *
     * @param grid
     * @param grid_Visualizer a visualizer extends SquareVisualizer that can translate the grids squares into a color
     */
    public GridVisualizationJPanel(GridEnviroment grid, SquareVisualizer grid_Visualizer) {
        this.grid = grid;
        this.grid_visualizer = grid_Visualizer;
        this.setSize(1200, 800);
        grid.addListener(this);

    }

    @Override
    public void paint(Graphics g) {
        int xdif = Math.max(getWidth() / grid.width, 1);
        int ydif = Math.max(getHeight() / grid.height, 1);

        g.setColor(Color.black);
        for (int x = 1; x < grid.width; x++) {
            g.drawLine(x * xdif, 0, x * xdif, HEIGHT);
        }
        for (int y = 1; y < grid.height; y++) {
            g.drawLine(0, y * ydif, WIDTH, y * ydif);
        }

        for (int x = 0; x < grid.width; x++) {
            for (int y = 0; y < grid.height; y++) {
                g.setColor(grid_visualizer.visualize(grid.get(x, y)));
                g.fillRect(x * xdif, y * ydif, (x + 1) * xdif, (y + 1) * ydif);
            }
        }

    }

    /**
     * Should be called when the state of the grid changes
     *
     * @param instant
     */
    @Override
    public void itemchanged(DateTime instant) {
        repaint();
    }
}
