package b_model.entities;

import java.util.Date;

public class Measurement {
	private final Date timestamp;
	private final double temperature;
	private final double humidity;
	private final int co2;

	public Measurement(Date timestamp, double temperature, double humidity, int co2) {
		this.timestamp = timestamp;
		this.temperature = temperature;
		this.humidity = humidity;
		this.co2 = co2;
	}

	public Date getTimestamp() {
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
