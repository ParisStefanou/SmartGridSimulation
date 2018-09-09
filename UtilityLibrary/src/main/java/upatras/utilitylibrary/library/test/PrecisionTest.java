/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.test;

/**
 *
 * @author Paris
 */
public class PrecisionTest {

    /**A test about double precision when used for discrete steps
     *
     * @param args
     */
    public static void main(String[] args){
        
        
        double n1=10000000000d;
        long n2=10000000000L;
        
        System.out.println("diff is :"+(n2-n1));
        
    }
}
