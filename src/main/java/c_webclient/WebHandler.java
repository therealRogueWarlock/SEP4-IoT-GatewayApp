package c_webclient;

import b_model.entities.DeviceMeasurement;
import b_model.entities.Settings;
import util.DataConverter;

public class WebHandler {
	private final WebClient webClient;

	// Objects
	private final String MEASUREMENTS = "/measurements";
	private final String SETTINGS = "/settings";

	public WebHandler() {
		webClient = WebClientImpl.getInstance();
	}

	public void addNewMeasurement(DeviceMeasurement measurements) {
		// Call the Put for the new Measurement to the Database Web Service
		String jsonFormat = DataConverter.toJson(measurements);
		webClient.put(MEASUREMENTS, jsonFormat);
	}

	public Settings getSettings(String deviceId) {
		String jsonSettings = (String) webClient.get(SETTINGS + deviceId);
		Settings settings = (Settings) DataConverter.fromJson(jsonSettings, Settings.class);
		return settings;
	}
}
