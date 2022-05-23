package b_model.entities;

public class Measurement {
	private final String timestamp;
	private final double temperature;
	private final int humidity;
	private final int co2;

	public Measurement(String timestamp, double temperature, int humidity, int co2) {
		this.timestamp = timestamp;
		this.temperature = temperature;
		this.humidity = humidity;
		this.co2 = co2;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public double getTemperature() {
		return temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public int getCo2() {
		return co2;
	}
}
