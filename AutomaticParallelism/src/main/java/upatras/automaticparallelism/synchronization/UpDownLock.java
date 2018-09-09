/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.synchronization;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paris
 */
public class UpDownLock {

    private volatile Integer value;

    boolean debug = false;

    public UpDownLock(Integer value) {
        this.value = value;
    }

    public void waitUntillValueAndSubtract(int amount) {

        boolean passed = false;

        while (!passed) {
            synchronized (this) {

                if (value >= amount) {
                    value -= amount;
                    passed = true;
                } else {
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(UpDownLock.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }

    }

    public void waitUntillValue(int amount) {

        boolean passed = false;

        while (!passed) {
            synchronized (this) {

                if (debug) {
                    System.out.println("trying to pass value was " + value + " and need: " + amount);
                }

                if (value >= amount) {
                    passed = true;
                } else {
                    if (debug) {
                        System.out.println("failed to pass value was " + value + " and needed: " + amount);
                    }
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(UpDownLock.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (debug) {
                        System.out.println("thread awoke");
                    }
                }

            }
        }
        if (debug) {
            System.out.println("suceeded in passing");
        }

    }

    public void subtract(int amount) {
        synchronized (this) {
            if (debug) {
                System.out.println("subtracted " + amount + " from : " + value);
            }
            value -= amount;
        }
    }

    public void add(int amount) {
        synchronized (this) {

            if (debug) {
                System.out.println("added " + amount + " to : " + value);
            }
            value += amount;
            this.notifyAll();
        }
    }

    public void set(int amount) {
        synchronized (this) {

            boolean worthwaking = amount > value;

            if (debug) {
                System.out.println("set value to " + amount);
            }

            value = amount;
            if (worthwaking) {
                this.notifyAll();
            }
        }
    }

}
