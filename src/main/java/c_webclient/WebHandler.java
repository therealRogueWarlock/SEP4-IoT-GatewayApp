package c_webclient;

import b_model.entities.DeviceMeasurement;
import b_model.entities.Settings;
import org.springframework.web.client.RestClientException;
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
		try {
			webClient.put(MEASUREMENTS, jsonFormat);
		} catch (RestClientException restClientException) {
			restClientException.printStackTrace();
		}

	}

	public Settings getSettings(String deviceId) {
		// Initiate Variables
		String jsonSettings;
		Settings settings = null;

		// Try getting Settings from Rest Service
		try {
			jsonSettings = (String) webClient.get(SETTINGS + deviceId);
			settings = (Settings) DataConverter.fromJson(jsonSettings, Settings.class);
		} catch (RestClientException restClientException) {
			restClientException.printStackTrace();
		}

		// Return new Settings
		return settings;

	}
}
