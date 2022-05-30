package b_model;

import a_websocket.WebSocketClient;
import a_websocket.WebSocketCommunication;
import b_model.entities.DeviceMeasurement;
import b_model.entities.Measurement;
import b_model.entities.Settings;
import c_webclient.WebHandler;
import org.json.JSONException;
import org.json.JSONObject;
import util.ConsoleLogger;
import util.DataConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerModel implements SocketObserver {
	private final String WEB_SOCKET_URL = "wss://iotnet.teracom.dk/app?token=vnoUhAAAABFpb3RuZXQudGVyYWNvbS5ka2v2Q_l1Fej_TK0VFKubjJQ=";
	private final WebSocketCommunication webSocketCommunication;
	private final WebHandler webHandler;
	private final int expectedMeasurementsPrTelegram;
	private final int timeBetweenMeasurements; // In Seconds

	public ServerModel(WebHandler webHandler) {
		// Set Server Communications
		this.webHandler = webHandler;

		// Create SocketConnection
		webSocketCommunication = new WebSocketClient(WEB_SOCKET_URL);
		webSocketCommunication.attachObserver(this);

		// Set other special Data
		expectedMeasurementsPrTelegram = 5;
		timeBetweenMeasurements = 60;
	}

	@Override
	public void receiveData(Object obj) {
		if (!(obj instanceof JSONObject json)) {
			return;
		}

		try {
			// Handle the Incoming Data and retrieve the Device ID of the Sending Unit
			Map<String, String> deviceData = handleData(json);

			// If Device ID is Empty, simply return
			if (deviceData == null) {
				return;
			}

			// Check the Server for new Settings
			Settings newSettings = webHandler.getSettings(deviceData.get("deviceId"));

			// If Settings are new, send to the Sending Unit
			if (newSettings != null) { // FIXME: Always not null?
				ConsoleLogger.clDebug("Settings for device " + deviceData.get("deviceId") + ": \nCO2 Threshold: " + newSettings.co2Threshold() + "\nHumidity Threshold: " + newSettings.humidityThreshold() + "\nTarget Temperature: " + newSettings.targetTemperature() + "\nTemperature Margin: " + newSettings.temperatureMargin());
				String actualDeviceId = DataConverter.deviceIdConverter_nameToEui(deviceData.get("deviceId"));
				webSocketCommunication.sendObject(actualDeviceId, deviceData.get("port"), newSettings);
			} else {
				ConsoleLogger.clLog("Settings were Null");
			}

		} catch (JSONException e) {
			ConsoleLogger.clWarn(e.getMessage());
		}
	}

	private Map<String, String> handleData(JSONObject jsonRawMeasurements) throws JSONException {
		// Return if Command wasn't RX
		if (!"rx".equals(jsonRawMeasurements.getString("cmd"))) {
			return null;
		}

		// Create Return Map
		Map<String, String> returnMap = new HashMap<>();

		// Debug Print
		ConsoleLogger.clLog("Data Received" + jsonRawMeasurements);

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
		ConsoleLogger.clDebug(jsonMeasurementClean);

		// Send the Object through the WebClient to the Web Server
		webHandler.addNewMeasurement(newDeviceMeasurement);

		// Fill return map
		returnMap.put("deviceId", newDeviceMeasurement.getDeviceId());
		returnMap.put("port", "" + jsonRawMeasurements.getInt("port"));

		// Return Device ID so the Server can search for New Settings
		return returnMap;
	}

	private List<Measurement> createMeasurements(String data, long epochTime) {
		ConsoleLogger.clLog("Creating Measurements");
		// Initiate temporary List
		List<Measurement> measurementList = new ArrayList<>();
		// Measurement to fill into list
		Measurement newMeasurement;

		int len = data.length();
		int slice = len / expectedMeasurementsPrTelegram;

		// Fill Loop (Runs the amount of Measurements we expect to see)
		for (int i = 0; i < expectedMeasurementsPrTelegram; i++) {
			// Convert Raw Data into useful Numbers
			String dataSubstring = data.substring(i * slice, (i + 1) * slice);

			ConsoleLogger.clDebug("%d: Data: %s", i, dataSubstring);

			Map<String, Number> dataMap = DataConverter.rawHexStringToMeasurement(dataSubstring);

			// Assumed Measurement Time
			long assumedTime = epochTime - ((long) (expectedMeasurementsPrTelegram - i) * timeBetweenMeasurements * 1000L);

			// Set the new Measurement
			newMeasurement = new Measurement(DataConverter.epochToTimestamp(assumedTime),
			                                 (double) dataMap.get("temperature"),
			                                 (int) dataMap.get("humidity"),
			                                 (int) dataMap.get("co2"));

			// Add new Measurement to temporary List
			measurementList.add(newMeasurement);

			// Logging Data
			ConsoleLogger.clLog(newMeasurement.toString());
		}

		return measurementList;
	}
}

