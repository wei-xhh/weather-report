package com.example.weixhhweather;

public class ForecastData {
    private String date;
    private String high;
    private String low;
    private String type;

    public ForecastData(String date, String high, String low, String type) {
        this.date = date;
        this.high = high;
        this.low = low;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
