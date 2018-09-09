package upatras.electricaldevicesimulation.RemoteData;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractElectricalDevice;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class metricon_client {

    public static DateTime dt = new DateTime(DateTimeZone.UTC);
    public static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter dfOut = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static DateTimeFormatter tfOut = DateTimeFormat.forPattern("HH:mm:ss");

    public static void send_device_log(AbstractElectricalDevice device, int id) throws Exception {

        JsonObject jsonObject1 = Json.createObjectBuilder()
                .add("dev_id", id)
                .add("value", device.getAveragePowerConsumed().getActivePower().value)
                .add("units", "Power")
                .add("mtype", device.getClass().getSimpleName())
                .add("date", dfOut.print(dt))
                .add("time", tfOut.print(dt))
                .build();

        URL url = new URL("http://pie1.csl.ece.upatras.gr/pie_000000000000/pie_000000");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("Content-Type", "application/json");
        httpCon.setRequestProperty("Accept", "application/json");
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
        out.write(jsonObject1.toString());
        out.flush();
        out.close();

        System.out.println(httpCon.getResponseCode());
        System.out.println("jsonObject1 -> " + jsonObject1.toString() + "\n\n");
    }
}
