package c_webclient;

import b_model.entities.DeviceMeasurement;
import b_model.entities.Settings;
import com.google.gson.Gson;

public class WebHandler {
	private final WebClient webClient;
	private final Gson gson;

	// Objects
	private final String MEASUREMENTS = "/measurements";
	private final String SETTINGS = "/settings";

	public WebHandler() {
		webClient = WebClientImpl.getInstance();
		gson = new Gson();
	}

	public void addNewMeasurement(DeviceMeasurement measurements) {
		// Call the Put for the new Measurement to the Database Web Service
		String jsonFormat = gson.toJson(measurements, DeviceMeasurement.class);
		webClient.put(MEASUREMENTS, jsonFormat);
	}

	public Settings getSettings(String deviceId) {
		return null;
	}
}
