package ru.mgvk.simplegateway;

import java.sql.Timestamp;

public class TemperatureData {

    private Timestamp time;
    private float     temp;
    private float     humidity;

    public TemperatureData(Timestamp time, float temp, float humidity) {
        this.time = time;
        this.temp = temp;
        this.humidity = humidity;
    }

    public TemperatureData(float temp, float humidity) {
        this.temp = temp;
        this.humidity = humidity;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }


}
