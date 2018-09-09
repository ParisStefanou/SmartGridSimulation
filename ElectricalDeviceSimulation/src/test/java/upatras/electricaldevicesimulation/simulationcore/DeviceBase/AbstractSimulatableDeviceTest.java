package upatras.electricaldevicesimulation.simulationcore.DeviceBase;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Created by Paris on 21-Jun-18.
 */
public class AbstractSimulatableDeviceTest {


    @Test
    public void run() throws Exception {



        PrintStream stdout = System.out;
        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        DeviceSimulationInstance instance = new DeviceSimulationInstance();
        AbstractSimulatableDevice device = new AbstractSimulatableDevice(instance) {
            @Override
            protected void processCommand(Command c) {
                System.out.println(c.name);
            }

            @Override
            public void simulate(TimeStep simulation_step) {
                ;
            }
        };



        DateTime now = DateTime.now();

        device.submitCommand(new Command("5", now.plusSeconds(5)));
        device.submitCommand(new Command("2", now.plusSeconds(2)));
        device.submitCommand(new Command("3", now.plusSeconds(3)));
        device.submitCommand(new Command("0", null));
        device.submitCommand(new Command("4", now.plusSeconds(4)));
        device.submitCommand(new Command("1", now.plusSeconds(1)));

        device.submitCommand(new Command("13", now.plusSeconds(13)));
        device.submitCommand(new Command("12", now.plusSeconds(12)));
        device.submitCommand(new Command("15", now.plusSeconds(15)));

        instance.setPresent(now);
        instance.advanceDuration(Duration.standardSeconds(10));


        device.submitCommand(new Command("10", null));
        device.submitCommand(new Command("14", now.plusSeconds(14)));
        device.submitCommand(new Command("11", now.plusSeconds(11)));

        instance.advanceDuration(Duration.standardSeconds(10));


        final String standardOutput = myOut.toString();

        System.setOut(stdout);


        System.out.println(standardOutput);


        if (standardOutput.replace("\n", "").replace("\r", "").contains("012345101112131415")){
            assertTrue(true);
        }else{
            assertTrue(false);
        }


        instance.shutdown();
    }

}