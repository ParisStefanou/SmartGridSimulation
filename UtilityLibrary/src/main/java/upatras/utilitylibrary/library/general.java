/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library;

/**
 *
 * @author Paris
 */
public class general {

    /**Returns the class name without all the packages preceding it
     *
     * @param c Class to get the short name from
     * @return The shortened name
     */
    public static String getClassShortName(Class c) {
        String[] names = c.getSimpleName().split(".");
        if (names.length == 0) {
            return c.getSimpleName();
        } else {
            return names[names.length - 1];
        }

    }
}
