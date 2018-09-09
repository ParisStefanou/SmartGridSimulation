/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.tasks;

/**
 *
 * @author Paris
 */
public abstract class Executor {

    public Executor(int threadcount) {
        this.threadcount = threadcount;
    }

    public final int threadcount;

}
