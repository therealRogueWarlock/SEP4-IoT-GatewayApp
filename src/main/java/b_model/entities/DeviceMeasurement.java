package b_model.entities;

import util.DataConverter;

import java.util.ArrayList;
import java.util.List;

public class DeviceMeasurement {
	private final String deviceId;
	private final List<Measurement> measurements;

	public DeviceMeasurement(String deviceId) {
		this.deviceId = DataConverter.deviceIdConverter_euiToName(deviceId);
		this.measurements = new ArrayList<>();
	}

	public String getDeviceId() {
		return deviceId;
	}

	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public void addMeasurements(List<Measurement> newMeasurements) {
		measurements.addAll(newMeasurements);
	}

	public void addMeasurements(Measurement... newMeasurement) {
		measurements.addAll(List.of(newMeasurement));
	}

	@Override
	public String toString() {
		// "This is gonna be a fun one" ~ Andreas Young
		StringBuilder sb = new StringBuilder();

		// Start JSON String
		sb.append("{\n")
		  .append("  \"deviceId\": \"%s\",\n")
		  .append("  \"measurements\": [\n");

		// Lengths for the different Placeholders
		int lenTs = "_TIMESTAMP_".length();
		int lenT = "_TEMPERATURE_".length();
		int lenH = "_HUMIDITY_".length();
		int lenC = "_CO2_".length();

		// Index Variables for Replacements
		int idxTs, idxT, idxH, idxC;

		// Loop through Measurements to add dynamically
		for (Measurement measurement : measurements) {
			// Append new Measurement
			sb.append("    {\n")
			  .append("      \"timestamp\": \"_TIMESTAMP_\",\n")
			  .append("      \"temperature\": _TEMPERATURE_,\n")
			  .append("      \"humidity\": _HUMIDITY_,\n")
			  .append("      \"co2\": _CO2_\n")
			  .append("    },\n");

			// Get Index of Placeholders
			// Replace Placeholders with Actual Values
			idxTs = sb.indexOf("_TIMESTAMP_");
			sb.replace(idxTs, idxTs + lenTs, "" + measurement.getTimestamp());

			idxT = sb.indexOf("_TEMPERATURE_");
			sb.replace(idxT, idxT + lenT, "" + measurement.getTemperature());

			idxH = sb.indexOf("_HUMIDITY_");
			sb.replace(idxH, idxH + lenH, "" + measurement.getHumidity());

			idxC = sb.indexOf("_CO2_");
			sb.replace(idxC, idxC + lenC, "" + measurement.getCo2());
		}

		// Remove leftover comma
		sb.deleteCharAt(sb.lastIndexOf(","));

		// Close JSON String
		sb.append("  ]\n")
		  .append("}");

		// Return formatted String with Device ID
		return sb.toString()
		         .formatted(deviceId);
	}
}
