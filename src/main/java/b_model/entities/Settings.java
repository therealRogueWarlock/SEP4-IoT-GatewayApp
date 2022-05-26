package b_model.entities;

public class Settings {
	private final double targetTemperature;
	private final int temperatureMargin;
	private final int humidityThreshold;
	private final int co2Threshold;

	public Settings(double targetTemperature, int temperatureMargin, int humidityThreshold, int co2Threshold) {
		this.targetTemperature = targetTemperature;
		this.temperatureMargin = temperatureMargin;
		this.humidityThreshold = humidityThreshold;
		this.co2Threshold = co2Threshold;
	}

	public double getTargetTemperature() {
		return targetTemperature;
	}

	public int getTemperatureMargin() {
		return temperatureMargin;
	}

	public int getHumidityThreshold() {
		return humidityThreshold;
	}

	public int getCo2Threshold() {
		return co2Threshold;
	}

}
