/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.test;

import org.joda.time.DateTime;
import upatras.agentsimulation.agent.Population;
import upatras.agentsimulation.event.TimeEvent;
import upatras.agentsimulation.event.priorityqueue.EventBucket;
import upatras.agentsimulation.event.priorityqueue.BucketRBTree;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author Paris
 */
public class RedBlackTreeTest {

	public static void main(String[] args) {
		BucketRBTree rbtree = new BucketRBTree();

		PriorityQueue<TimeEvent> queue = new PriorityQueue();

		int spread =800000000;
		int count = 100000;

		DateTime now = DateTime.now();

		Random rand;

		rand = new Random(1);
		long start ;

		ArrayList<String> temp = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			rbtree.add(new TimeEvent("Test " + i, null, new Population(), now.plusMillis(rand.nextInt() % spread)));
		}

		System.out.println("clusters " + rbtree.size());
		while (!rbtree.isEmpty()) {
			EventBucket te = rbtree.removeBucket();
			for (TimeEvent e : te) {
				temp.add(e.name);
			}
		}
		temp.clear();

		for (int k = 0; k < 10; k++) {

			rand = new Random(1);
			start = System.currentTimeMillis();

			temp = new ArrayList<>();

			for (int i = 0; i < count; i++) {
				rbtree.add(new TimeEvent("Test " + i, null, new Population(), now.plusMillis(rand.nextInt() % spread)));
			}

			//System.out.println("clusters "+rbtree.size());
			while (!rbtree.isEmpty()) {
				EventBucket te = rbtree.removeBucket();
				for (TimeEvent e : te) {

					temp.add(e.name);
				}
			}

			System.out.println("rb tree took " + (System.currentTimeMillis() - start) / 1000.0);
			temp.clear();

			rand = new Random(1);

			start = System.currentTimeMillis();
			for (int i = 0; i < count; i++) {
				queue.add(new TimeEvent("Test " + i, null, new Population(), now.plusMillis(rand.nextInt() % spread)));
			}

			for (int i = 0; i < count; i++) {
				temp.add(queue.remove().name);
			}
			System.out.println("queue took " + (System.currentTimeMillis() - start) / 1000.0);
			temp.clear();
		}
	}
}
