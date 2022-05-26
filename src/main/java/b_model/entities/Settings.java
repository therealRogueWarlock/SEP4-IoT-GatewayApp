package b_model.entities;

public class Settings {
  private int temperatureMargin;
  private int co2Threshold;
  private float targetTemperature;
  private int humidityThreshold;

  public Settings(int temperatureMargin, int co2Threshold, float targetTemperature, int humidityThreshold) {
    this.temperatureMargin = temperatureMargin;
    this.co2Threshold = co2Threshold;
    this.targetTemperature = targetTemperature;
    this.humidityThreshold = humidityThreshold;
  }

  public int getTemperatureMargin()
  {
    return temperatureMargin;
  }

  public int getCo2Threshold()
  {
    return co2Threshold;
  }

  public float getTargetTemperature()
  {
    return targetTemperature;
  }

  public int getHumidityThreshold()
  {
    return humidityThreshold;
  }

}
