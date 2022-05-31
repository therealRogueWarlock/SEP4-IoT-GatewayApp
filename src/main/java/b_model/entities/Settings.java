package b_model.entities;

import java.util.Objects;

public final class Settings {
	private double targetTemperature;
	private int temperatureMargin;
	private int humidityThreshold;
	private int co2Threshold;

	public Settings(double targetTemperature, int temperatureMargin, int humidityThreshold, int co2Threshold) {
		this.targetTemperature = targetTemperature;
		this.temperatureMargin = temperatureMargin;
		this.humidityThreshold = humidityThreshold;
		this.co2Threshold = co2Threshold;
	}

	public double getTargetTemperature() {
		return targetTemperature;
	}

	public void setTargetTemperature(double targetTemperature) {
		this.targetTemperature = targetTemperature;
	}

	public int getTemperatureMargin() {
		return temperatureMargin;
	}

	public void setTemperatureMargin(int temperatureMargin) {
		this.temperatureMargin = temperatureMargin;
	}

	public int getHumidityThreshold() {
		return humidityThreshold;
	}

	public void setHumidityThreshold(int humidityThreshold) {
		this.humidityThreshold = humidityThreshold;
	}

	public int getCo2Threshold() {
		return co2Threshold;
	}

	public void setCo2Threshold(int co2Threshold) {
		this.co2Threshold = co2Threshold;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (Settings) obj;
		return Double.doubleToLongBits(this.targetTemperature) == Double.doubleToLongBits(that.targetTemperature) && this.temperatureMargin == that.temperatureMargin && this.humidityThreshold == that.humidityThreshold && this.co2Threshold == that.co2Threshold;
	}

	@Override
	public int hashCode() {
		return Objects.hash(targetTemperature, temperatureMargin, humidityThreshold, co2Threshold);
	}

	@Override
	public String toString() {
		return "Settings[" + "targetTemperature=" + targetTemperature + ", " + "temperatureMargin=" + temperatureMargin + ", " + "humidityThreshold=" + humidityThreshold + ", " + "co2Threshold=" + co2Threshold + ']';
	}

}
