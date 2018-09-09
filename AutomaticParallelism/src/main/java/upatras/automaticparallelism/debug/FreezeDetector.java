/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.debug;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paris
 */
public class FreezeDetector extends Thread {

	private volatile boolean ok = false;
	private final long time_ms;
	private final String message;

	/**
	 * A debugging class for the executors
	 *
	 * @param wait_ms time to wait till announcing an error
	 * @param message output
	 */
	public FreezeDetector(long wait_ms, String message) {
		this.time_ms = wait_ms;
		this.message = message;
	}

	/**
	 * repeated check
	 *
	 */
	public void ok() {
		ok = true;
	}

	@Override
	public void run() {
		try {
			sleep(time_ms);

		} catch (InterruptedException ex) {
			Logger.getLogger(FreezeDetector.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (!ok) {
			System.err.println(message);
		}

	}

}
