package b_model;

import a_websocket.WebSocketClient;
import a_websocket.WebSocketCommunication;
import b_model.entities.DeviceMeasurement;
import b_model.entities.Measurement;
import c_webclient.WebHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ServerModel implements SocketObserver {
	private final String WEB_SOCKET_URL = "wss://iotnet.teracom.dk/app?token=vnoUhAAAABFpb3RuZXQudGVyYWNvbS5ka2v2Q_l1Fej_TK0VFKubjJQ=";
	private final WebSocketCommunication webSocketCommunication;
	private final WebHandler webHandler;
	private final Gson gson = new GsonBuilder().setPrettyPrinting()
	                                           .create();
	private final int expectedMeasurementsPrTelegram;

	public ServerModel(WebHandler webHandler) {
		// Set Server Communications
		this.webHandler = webHandler;

		// Create SocketConnection
		webSocketCommunication = new WebSocketClient(WEB_SOCKET_URL);
		webSocketCommunication.attachObserver(this);

		// Set other special Data
		expectedMeasurementsPrTelegram = 1;
	}

	@Override
	public void receiveData(Object obj) {
		if (!(obj instanceof JSONObject json)) {
			return;
		}

		try {
			String deviceId = handleData(json);
			if ("".equals(deviceId)) {
				return;
			}

			webSocketCommunication.sendObject(webHandler.getSettings(deviceId));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private String handleData(JSONObject jsonRawMeasurements) throws JSONException {
		// Return if Command wasn't RX
		if (!"rx".equals(jsonRawMeasurements.getString("cmd"))) {
			return "";
		}

		// Debug Print
		System.out.println("Data Received");

		// Create Device Measurement
		DeviceMeasurement dm = new DeviceMeasurement(jsonRawMeasurements.getString("EUI"));

		// Extrapolate Data into Measurements
		String data = jsonRawMeasurements.getString("data"); // Temperature, Humidity, CO2
		// Convert TS to Timestamp with Date
		long epochTime = jsonRawMeasurements.getLong("ts");
		// Get a converted List of Measurements
		List<Measurement> newMeasurements = createMeasurements(data, epochTime);
		// Add Measurement List to Device Measurement
		dm.addMeasurements(newMeasurements);

		// Create a printable Json representation of the Device Measurement
		String jsonMeasurementClean = gson.toJson(dm);

		// Debug Print the Device Measurement
		System.out.println(jsonMeasurementClean); // SOUT

		// TODO: Send the Object through the WebClient to the Web Server

		return dm.getDeviceId();
	}

	private List<Measurement> createMeasurements(String data, long epochTime) {
		System.out.println("> Creating Measurements"); // SOUT
		// Initiate temporary List
		List<Measurement> measurementList = new ArrayList<>();
		// Measurement to fill into list
		Measurement newMeasurement;

		// Buffer Variables for Data
		int tempX10, humidityX100, co2;
		double temp, humidity;

		// Fill Loop (Runs the amount of Measurements we expect to see)
		for (int i = 0; i < expectedMeasurementsPrTelegram; i++) {
			// Get Raw (scaled) Integer Data
			tempX10 = Integer.parseInt(data.substring(0, 4), 16);
			humidityX100 = Integer.parseInt(data.substring(4, 8), 16);
			co2 = Integer.parseInt(data.substring(8, 12), 16);

			// Convert Temperature and Humidity to Floats
			temp = new BigDecimal(tempX10 / 10f).setScale(1, RoundingMode.HALF_UP)
			                                    .doubleValue();
			humidity = new BigDecimal(humidityX100 / 100f).setScale(1, RoundingMode.HALF_UP)
			                                              .doubleValue();

			// Set the new Measurement
			newMeasurement = new Measurement(epochToTimestamp(epochTime), temp, humidity, co2);

			// Add new Measurement to temporary List
			measurementList.add(newMeasurement);
		}

		return measurementList;
	}

	private String epochToTimestamp(long epochTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:000'Z'", Locale.ENGLISH);

		return sdf.format(new Date(epochTime));
	}
}
