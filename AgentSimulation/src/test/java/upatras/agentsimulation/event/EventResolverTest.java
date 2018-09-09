/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.event;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.agent.Population;
import upatras.agentsimulation.behaviour.AbstractSelfBehaviour;
import upatras.agentsimulation.main.AgentSimulationInstance;

/**
 *
 * @author Paris
 */
public class EventResolverTest {

	public EventResolverTest() {
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

	private class MessageEvent extends TimeEvent {

		String message;

		@Override
		public void preprocessing() {
			super.preprocessing(); //To change body of generated methods, choose Tools | Templates.
			System.out.print("pre" + message + "-");
		}

		public MessageEvent(AbstractAgent origin, Population pop, DateTime event_time, String message) {
			super(origin, pop, event_time);
			this.message = message;
		}

		@Override
		public void postprocessing() {
			super.postprocessing(); //To change body of generated methods, choose Tools | Templates.
			System.out.print("-post" + message + "-");
		}
	}

	/**
	 * Test of addEvents method, of class EventResolver.
	 */
	@Test
	public void testSimpleEvents() {

		DateTime starttime = new DateTime();
		AgentSimulationInstance asi = new AgentSimulationInstance(starttime);
		//asi.event_resolver.rb_mode = true;

		int agentcounter = 2;
		AbstractAgent[] dummy_agents = new AbstractAgent[agentcounter];
		Population pop = new Population();
		for (int i = 0; i < agentcounter; i++) {
			dummy_agents[i] = new AbstractAgent(asi) {

			};
			pop.add(dummy_agents[i]);
			dummy_agents[i].attachBehaviour(MessageEvent.class, new AbstractSelfBehaviour(dummy_agents[i]) {
				@Override
				public void execute(Event event) {
					System.out.print(((MessageEvent) event).message);
				}
			});
		}

		AbstractAgent instigator = new AbstractAgent(asi) {

		};

		asi.submitEvent(new MessageEvent(instigator, pop, starttime.plusMillis(1), "1"));
		asi.submitEvent(new MessageEvent(instigator, pop, starttime.plusMillis(2), "1"));

		asi.submitEvent(new MessageEvent(instigator, pop, starttime.plusMinutes(1).plusMillis(1), "2"));
		asi.submitEvent(new MessageEvent(instigator, pop, starttime.plusMinutes(1).plusMillis(1), "2"));

		outContent.reset();
		asi.advanceToEnd();

		String out = outContent.toString();
		String expected = "pre1-11-post1-pre1-11-post1-pre2-22-post2-pre2-22-post2-";

		assertEquals(expected, out);

		//System.out.println("out : " + out);
		//System.err.println("exp : " + out);
		asi.shutdown();

	}

	/**
	 * Test of addEvents method, of class EventResolver.
	 */
	@Test
	public void testParallelEvents() {

		DateTime starttime = new DateTime();
		AgentSimulationInstance asi = new AgentSimulationInstance(starttime);
		//asi.event_resolver.rb_mode = true;

		int agentcounter = 5;
		AbstractAgent[] dummy_agents = new AbstractAgent[agentcounter];
		Population pop = new Population();
		for (int i = 0; i < agentcounter; i++) {
			dummy_agents[i] = new AbstractAgent(asi) {

			};
			pop.add(dummy_agents[i]);
			dummy_agents[i].attachBehaviour(MessageEvent.class, new AbstractSelfBehaviour(dummy_agents[i]) {
				@Override
				public void execute(Event event) {
					System.out.print(((MessageEvent) event).message);
				}
			});
		}

		AbstractAgent instigator = new AbstractAgent(asi) {

		};

		asi.submitEvent(new MessageEvent(instigator, pop, starttime.plusMillis(1), "1").enableParrallel());
		asi.submitEvent(new MessageEvent(instigator, pop, starttime.plusMillis(2), "1").enableParrallel());

		asi.submitEvent(new MessageEvent(instigator, pop, starttime.plusMinutes(1).plusMillis(1), "2").enableParrallel());
		asi.submitEvent(new MessageEvent(instigator, pop, starttime.plusMinutes(1).plusMillis(1), "2").enableParrallel());

		outContent.reset();
		asi.advanceToEnd();

		String out = outContent.toString();
		String expected = "pre1-11111-post1-pre1-11111-post1-pre2-22222-post2-pre2-22222-post2-";

		assertEquals(expected, out);

		//System.out.println("out : " + out);
		//System.err.println("exp : " + out);
		asi.shutdown();

	}

	private class MessageEventSimple extends TimeEvent {

		String message;

		public MessageEventSimple(AbstractAgent origin, Population pop, DateTime event_time, String message) {
			super(origin, pop, event_time);
			this.message = message;
		}

	}

	/**
	 * Test of addEvents method, of class EventResolver.
	 */
	@Test
	public void testSimultaneousEvents() {

		DateTime starttime = new DateTime();
		AgentSimulationInstance asi = new AgentSimulationInstance(starttime, true);

		int agentcounter = 5;
		AbstractAgent[] dummy_agents = new AbstractAgent[agentcounter];
		Population pop = new Population();
		for (int i = 0; i < agentcounter; i++) {
			dummy_agents[i] = new AbstractAgent(asi) {

			};
			pop.add(dummy_agents[i]);
			dummy_agents[i].attachBehaviour(MessageEventSimple.class, new AbstractSelfBehaviour(dummy_agents[i]) {
				@Override
				public void execute(Event event) {
					System.out.print(((MessageEventSimple) event).message);
				}
			});
		}

		AbstractAgent instigator = new AbstractAgent(asi) {

		};

		asi.submitEvent(new MessageEventSimple(instigator, pop, starttime.plusMillis(1), "1"));
		asi.submitEvent(new MessageEventSimple(instigator, pop, starttime.plusMinutes(1).plusMillis(1), "2"));
		asi.submitEvent(new MessageEventSimple(instigator, pop, starttime.plusMillis(1), "1"));
		asi.submitEvent(new MessageEventSimple(instigator, pop, starttime.plusMinutes(1).plusMillis(1), "2"));

		asi.submitEvent(new MessageEventSimple(instigator, pop, starttime.plusMillis(2), "1"));
		asi.submitEvent(new MessageEventSimple(instigator, pop, starttime.plusMinutes(1).plusMillis(1), "2"));
		asi.submitEvent(new MessageEventSimple(instigator, pop, starttime.plusMillis(1), "1"));
		asi.submitEvent(new MessageEventSimple(instigator, pop, starttime.plusMinutes(1).plusMillis(1), "2"));

		asi.submitEvent(new MessageEventSimple(instigator, pop, starttime.plusMinutes(2).plusMillis(2), "3").enableParrallel());
		asi.submitEvent(new MessageEventSimple(instigator, pop, starttime.plusMinutes(2).plusMillis(1), "3").enableParrallel());
		asi.submitEvent(new MessageEventSimple(instigator, pop, starttime.plusMinutes(2).plusMillis(2), "3").enableParrallel());
		asi.submitEvent(new MessageEventSimple(instigator, pop, starttime.plusMinutes(2).plusMillis(1), "3").enableParrallel());

		
		outContent.reset();
		asi.advanceToEnd();

		String out = outContent.toString();
		String expected = "111111111111111111112222222222222222222233333333333333333333";

		assertEquals(expected, out);

		asi.shutdown();
	}
}
