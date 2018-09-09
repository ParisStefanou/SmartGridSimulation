/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.utilitylibrary.library.randomnumbergenerators;

import java.util.Random;

/**
 *
 * @author Paris
 */
public class RandomGenerator {

    static Random rand = new Random(0);

    public static Random getRandom() {
        return new Random(rand.nextInt());
    }

    public static void reseed(int value) {
        rand = new Random(value);
    }

}
