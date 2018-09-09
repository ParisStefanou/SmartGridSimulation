/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.execution.batch;

import upatras.automaticparallelism.tasks.Task;

/**
 *
 * @author Paris
 */
public class VirtualTaskList<T extends Task> {

	final Batch<T> batch;
	final int startindex;
	final int length;

	public VirtualTaskList(Batch<T> batch, int startindex, int length) {
		this.batch = batch;
		this.startindex = startindex;
		this.length = length;
	}

	public T get(int index) {
		if (index < length) {
			return batch.tasks.get(startindex + index);
		} else {
			throw new IndexOutOfBoundsException("Index is " + index + " length is" + length);
		}
	}

	public int getSize() {
		return length;
	}

}
