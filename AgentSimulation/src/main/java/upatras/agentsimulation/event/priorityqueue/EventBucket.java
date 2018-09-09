/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.event.priorityqueue;

import org.joda.time.DateTime;
import upatras.agentsimulation.event.TimeEvent;

import java.util.ArrayList;

/**
 *
 * @author Paris
 */
public class EventBucket extends ArrayList<TimeEvent> implements Comparable<EventBucket> {

	public final DateTime bucket_start_time;
	public final DateTime bucket_end_time;
	public final DateTime middle_time;

	public EventBucket(TimeEvent creation_event) {
		this.bucket_start_time = creation_event.event_time.minusSeconds(1);
		this.bucket_end_time = creation_event.event_time.plusSeconds(1);
		this.middle_time = creation_event.event_time;
		add(creation_event);
	}

	public int compareTo(TimeEvent o) {
		if (o.event_time.isBefore(bucket_start_time)) {
			return -1;
		} else if (o.event_time.isAfter(bucket_end_time)) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int compareTo(EventBucket o) {
		if (o.middle_time.isBefore(middle_time)) {
			return -1;
		} else if (o.middle_time.isAfter(middle_time)) {
			return 1;
		} else {
			return 0;
		}
	}

	TimeEvent remove() {
		return remove(size() - 1);
	}

	TimeEvent peek() {
		return get(size() - 1);
	}

}
