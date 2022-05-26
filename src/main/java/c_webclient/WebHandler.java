package c_webclient;

import b_model.entities.DeviceMeasurement;
import b_model.entities.Settings;
import org.springframework.web.client.RestClientException;
import util.DataConverter;

public class WebHandler {
	private final WebClient webClient;

	// Objects
	private final String MEASUREMENTS = "/measurements";
	private final String DEVICES = "/Devices/";
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
		String jsonSettings = (String) webClient.get(DEVICES + deviceId + SETTINGS);
		Settings settings = (Settings) DataConverter.fromJson(jsonSettings, Settings.class);
		return settings;

	}
}
