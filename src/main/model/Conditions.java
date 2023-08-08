package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Conditions {
    private int timeUnix;
    private String time;
    private String summary;
    private String icon;
    private double precipIntensity;
    private double uvIndex;
    private double temperature;
    private double apparentTemperature;
    private double dewPoint;
    private int humidity;
    private int pressure;
    private double windSpeed;
    private double windBearing;

    public Conditions(int time, String summary, String icon, double precipIntensity, double uvIndex,
                      double temperature, double apparentTemperature, double dewPoint, int humidity,
                      int pressure, double windSpeed, int windBearing) {
        this.timeUnix = time;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time = dateFormat.format(new Date((long)time * 1000));

        this.summary = summary;
        this.icon = icon;
        this.precipIntensity = precipIntensity;
        this.uvIndex = uvIndex;
        this.temperature = temperature;
        this.apparentTemperature = apparentTemperature;
        this.dewPoint = dewPoint;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windBearing = windBearing;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public double getPrecipIntensity() {
        return precipIntensity;
    }

    public double getUvIndex() {
        return uvIndex;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getApparentTemperature() {
        return apparentTemperature;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getWindBearing() {
        return windBearing;
    }
}
