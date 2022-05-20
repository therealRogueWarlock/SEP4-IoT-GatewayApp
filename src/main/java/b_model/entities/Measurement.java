package b_model.entities;

public class Measurement {
	private final String timestamp;
	private final double temperature;
	private final double humidity;
	private final int co2;

	public Measurement(String timestamp, double temperature, double humidity, int co2) {
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

	public double getHumidity() {
		return humidity;
	}

	public int getCo2() {
		return co2;
	}
}
