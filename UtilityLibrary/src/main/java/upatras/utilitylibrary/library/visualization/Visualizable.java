/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.visualization;

import javax.swing.*;

/**
 *
 * @author Paris
 */
public interface Visualizable {

    /**This function should return a visible JFrame
     * consider using the getPanel() and adding it to a JFrame
     *
     * @return a decent sized visible JFrame
     */
    JFrame visualize();

    /**Should return a JPanel to be used from others
     * for the purpose of visualization, the JPanel's should be independant
     *
     * @return
     */
    JPanel getPanel();
	
	  /**This function should return a visible JFrame
     * consider using the getPanel() and adding it to a JFrame
     *
     * @return a decent sized visible JFrame
     */
    JFrame compressedVisualize();

    /**Should return a JPanel to be used from others
     * for the purpose of visualization, the JPanel's should be independant
     *
     * @return
     */
    JPanel getCompressedPanel();
	

}
