/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.agent;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import upatras.agentsimulation.behaviour.AbstractBehaviour;
import upatras.agentsimulation.event.Event;
import upatras.agentsimulation.event.TimeEvent;
import upatras.agentsimulation.main.AgentSimulationInstance;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Paris
 */
public class AbstractAgentTest {

	public AbstractAgentTest() {
	}

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void restoreStreams() {
		System.setOut(System.out);
		System.setErr(System.err);
	}

	@Test
	public void testSimpleEvent() {

		AgentSimulationInstance asi = new AgentSimulationInstance(DateTime.now());

		AbstractAgent a1 = new AbstractAgent(asi) {

		};
		AbstractBehaviour b = new AbstractBehaviour() {
			@Override
			public void execute(Event event) {
				System.out.print(event.origin + " says hello to " + event.targets.get(0));
			}

		};

		AbstractAgent a2 = new AbstractAgent(asi) {

		};

		a2.attachBehaviour("hello", b);

		asi.event_resolver.addEvent(new TimeEvent("hello", a1, a2.toPopulation(), asi.getPresent()) {

		});

		outContent.reset();
		asi.advanceEventCount(1);

		assertEquals("Agent type: id:0 says hello to Agent type: id:1", outContent.toString());

		asi.shutdown();

	}

	@Test
	public void testMultipleEvents() {

		AgentSimulationInstance asi = new AgentSimulationInstance(DateTime.now());

		AbstractAgent a1 = new AbstractAgent(asi) {

		};
		AbstractBehaviour b1 = new AbstractBehaviour() {
			@Override
			public void execute(Event event) {
				System.out.print("ev1");
			}

		};
		AbstractBehaviour b2 = new AbstractBehaviour() {
			@Override
			public void execute(Event event) {
				System.out.print("ev2");
			}

		};
		AbstractBehaviour b3 = new AbstractBehaviour() {
			@Override
			public void execute(Event event) {
				System.out.print("ev3");
			}

		};
		AbstractBehaviour b4 = new AbstractBehaviour() {
			@Override
			public void execute(Event event) {
				System.out.print("ev4");
			}

		};

		AbstractAgent a2 = new AbstractAgent(asi) {

		};

		a2.attachBehaviour("t1", b1);
		a2.attachBehaviour("t2", b2);
		a2.attachBehaviour("t3", b3);
		a2.attachBehaviour("t4", b4);

		asi.event_resolver.addEvent(new TimeEvent("t4", a1, a2.toPopulation(), asi.getPresent().plusMinutes(4)) {
		});
		asi.event_resolver.addEvent(new TimeEvent("t2", a1, a2.toPopulation(), asi.getPresent().plusMinutes(2)) {
		});
		asi.event_resolver.addEvent(new TimeEvent("t3", a1, a2.toPopulation(), asi.getPresent().plusMinutes(3)) {
		});
		asi.event_resolver.addEvent(new TimeEvent("t1", a1, a2.toPopulation(), asi.getPresent().plusMinutes(1)) {
		});
		asi.event_resolver.addEvent(new TimeEvent("t3", a1, a2.toPopulation(), asi.getPresent().plusMinutes(3)) {
		});

		outContent.reset();
		asi.advanceToEnd();

		assertEquals("ev1ev2ev3ev3ev4", outContent.toString());

		asi.shutdown();

	}
}
