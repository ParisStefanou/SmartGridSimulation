/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.visualization;

import java.awt.*;

/**
 *
 * @author Paris
 * @param <T> The object type this square will contain
 */
public abstract class SquareVisualizer<T> {

    /**
     * Implement this function to paint each individual square based on its
     * status
     *
     * @param gridsquare it should return a color that will be used for the grids visualization
     * @return
     */
    public abstract Color visualize(T gridsquare);
}
