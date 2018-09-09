/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.execution.batch;

import upatras.automaticparallelism.tasks.Task;

import java.util.ArrayList;

/**
 * @author Paris
 */
public class Batch<T extends Task> {

    protected ArrayList<T> tasks = new ArrayList<>();

    private int currloc = 0;

    /**
     * Adds a task to the execution queue
     *
     * @param t
     */
    public synchronized void addTask(T t) {
        tasks.add(t);
    }

    /**
     * Adds an array of tasks to the execution queue more efficient than single
     * additions
     *
     * @param tasks
     */
    public synchronized void addTasks(ArrayList<T> tasks) {

        this.tasks = tasks;

    }

    public VirtualTaskList<T> getTasks(int count) {

        synchronized (tasks) {
            if (currloc == tasks.size()) {
                return null;
            } else {
                if (currloc + count < tasks.size()) {

                    VirtualTaskList<T> toreturn = new VirtualTaskList(this, currloc, count);
                    currloc += count;
                    return toreturn;
                } else {
                    VirtualTaskList<T> toreturn = new VirtualTaskList(this, currloc, tasks.size() - currloc);
                    currloc = tasks.size();
                    return toreturn;
                }

            }
        }
    }

    public VirtualTaskList<T> getPercentage(float percent, int minimum) {

        synchronized (tasks) {

            int count = (int) Math.max(getRemainingSize() * percent, minimum);

            int size=tasks.size();
            if (currloc == size) {
                return null;
            } else {
                if (currloc + count < size) {

                    VirtualTaskList<T> toreturn = new VirtualTaskList(this, currloc, count);
                    currloc += count;
                    return toreturn;
                } else {
                    VirtualTaskList<T> toreturn = new VirtualTaskList(this, currloc, size - currloc);
                    currloc = tasks.size();
                    return toreturn;
                }

            }
        }
    }


    Integer flatcount = null;

    public VirtualTaskList<T> getAll(int threadcount) {

        synchronized (tasks) {


            int size=tasks.size();
            if (flatcount == null) {
                flatcount = getRemainingSize() / threadcount;
            }
            if (currloc == size) {
                return null;
            } else {
                if (currloc + flatcount < size) {

                    VirtualTaskList<T> toreturn = new VirtualTaskList(this, currloc, flatcount);
                    currloc += flatcount;
                    return toreturn;
                } else {
                    VirtualTaskList<T> toreturn = new VirtualTaskList(this, currloc, size - currloc);
                    currloc = tasks.size();
                    return toreturn;
                }

            }
        }
    }

    public VirtualTaskList<T> getFlat(int count) {

        synchronized (tasks) {

            int size=tasks.size();
            if (currloc == size) {
                return null;
            } else {
                if (currloc + count < size) {

                    VirtualTaskList<T> toreturn = new VirtualTaskList(this, currloc, count);
                    currloc += count;
                    return toreturn;
                } else {
                    VirtualTaskList<T> toreturn = new VirtualTaskList(this, currloc, size - currloc);
                    currloc = tasks.size();
                    return toreturn;
                }

            }
        }
    }

    public VirtualTaskList<T> getAllTasks() {

        synchronized (tasks) {
            if (currloc == tasks.size()) {
                return null;
            } else {
                VirtualTaskList<T> toreturn = new VirtualTaskList(this, currloc, tasks.size() - currloc);
                currloc = tasks.size();
                return toreturn;
            }
        }
    }

    public int getSize() {

        return tasks.size();
    }

    public int getRemainingSize() {

        return tasks.size() - currloc;
    }

    public void resetLoc() {
        currloc = 0;
    }
}
