/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.automaticparallelism.main;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 *
 * @author Paris
 */
public class TimeStep {

    public final DateTime step_start;
    public final DateTime step_end;
    public final DateTime middle_point;
    public final Duration duration;

    public TimeStep(DateTime current, DateTime next) {
        this.step_start = current;
        this.step_end = next;
        this.duration = new Duration(current, next);
        long median = (step_start.getMillis() + step_end.getMillis()) / 2;
        middle_point = new DateTime(median);
    }

    public TimeStep(DateTime previous, Duration duration) {
        this.step_start = previous;
        this.step_end = previous.plus(duration);
        this.duration = duration;
        long median = (step_start.getMillis() + step_end.getMillis()) / 2;
        middle_point = new DateTime(median);
    }

}
