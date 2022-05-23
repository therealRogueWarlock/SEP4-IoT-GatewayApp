package b_model;

import a_websocket.WebSocketClient;
import a_websocket.WebSocketCommunication;
import b_model.entities.DeviceMeasurement;
import b_model.entities.Measurement;
import b_model.entities.Settings;
import c_webclient.WebHandler;
import org.json.JSONException;
import org.json.JSONObject;
import util.DataConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ServerModel implements SocketObserver {
	private final String WEB_SOCKET_URL = "wss://iotnet.teracom.dk/app?token=vnoUhAAAABFpb3RuZXQudGVyYWNvbS5ka2v2Q_l1Fej_TK0VFKubjJQ=";
	private final WebSocketCommunication webSocketCommunication;
	private final WebHandler webHandler;
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
			// Handle the Incoming Data and retrieve the Device ID of the Sending Unit
			String deviceId = handleData(json);

			// If Device ID is Empty, simply return
			if ("".equals(deviceId)) {
				return;
			}

			// Check the Server for new Settings
			Settings newSettings = webHandler.getSettings(deviceId);

			// If Settings are new, send to the Sending Unit
			if (newSettings != null) {
				webSocketCommunication.sendObject(newSettings);
			} else {
				System.out.println("> Settings were Null");
			}

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
		DeviceMeasurement newDeviceMeasurement = new DeviceMeasurement(jsonRawMeasurements.getString("EUI"));

		// Extrapolate Data into Measurements
		String data = jsonRawMeasurements.getString("data"); // Temperature, Humidity, CO2

		// Convert TS to Timestamp with Date
		long epochTime = jsonRawMeasurements.getLong("ts");

		// Get a converted List of Measurements
		List<Measurement> deviceMeasurementInternalList = createMeasurements(data, epochTime);

		// Add Measurement List to Device Measurement
		newDeviceMeasurement.addMeasurements(deviceMeasurementInternalList);

		// Create a printable Json representation of the Device Measurement
		String jsonMeasurementClean = DataConverter.toJson(newDeviceMeasurement);

		// Debug Print the Device Measurement
		System.out.println(jsonMeasurementClean); // SOUT

		// Send the Object through the WebClient to the Web Server
		webHandler.addNewMeasurement(newDeviceMeasurement);

		// Return Device ID so the Server can search for New Settings
		return newDeviceMeasurement.getDeviceId();
	}

	private List<Measurement> createMeasurements(String data, long epochTime) {
		System.out.println("> Creating Measurements"); // SOUT
		// Initiate temporary List
		List<Measurement> measurementList = new ArrayList<>();
		// Measurement to fill into list
		Measurement newMeasurement;

		// Buffer Variables for Data
		int tempX10, humidityX100, co2;
		double temp;

		// Fill Loop (Runs the amount of Measurements we expect to see)
		for (int i = 0; i < expectedMeasurementsPrTelegram; i++) {
			// Get Raw (scaled) Integer Data
			tempX10 = Integer.parseInt(data.substring(0, 4), 16);
			humidityX100 = Integer.parseInt(data.substring(4, 8), 16);
			co2 = Integer.parseInt(data.substring(8, 12), 16);

			// Convert Temperature a double (Decimal Number)
			temp = new BigDecimal(tempX10 / 10f).setScale(1, RoundingMode.HALF_UP)
			                                    .doubleValue();

			// Set the new Measurement
			newMeasurement = new Measurement(DataConverter.epochToTimestamp(epochTime), temp, humidityX100 / 100, co2);

			// Add new Measurement to temporary List
			measurementList.add(newMeasurement);
		}

		return measurementList;
	}
}
