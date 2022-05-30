package b_model.entities;

public record Settings(double targetTemperature, int temperatureMargin, int humidityThreshold, int co2Threshold) {
}
