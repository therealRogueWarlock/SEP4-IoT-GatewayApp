package b_model.entities;

import java.util.ArrayList;
import java.util.List;

public class DeviceMeasurement {
	private final String deviceId;
	private final List<Measurement> measurements;

	public DeviceMeasurement(String deviceId) {
		this.deviceId = deviceId;
		this.measurements = new ArrayList<>();
	}

	public String getDeviceId() {
		return deviceId;
	}

	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public void addMeasurement(Measurement newMeasurement) {
		measurements.add(newMeasurement);
	}

}
