package io.abhishekpareek.app.weather;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by apareek on 1/20/16.
 */
public class CurrentWeatherData {
    private long mTime;
    private String mSummary;
    private String mIcon;
    private  double mTemperature;
    private double mPrecipitationChance;
    private String mTimeZone;
    private double mHumidity;

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public int getPrecipitationChance() {
        return (int) Math.round(mPrecipitationChance * 100);
    }

    public void setPrecipitationChance(double precipitationChance) {
        mPrecipitationChance = precipitationChance;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getFormattedTime() {
        long time = getTime() * (long) 1000;
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm a");
        format.setTimeZone(TimeZone.getTimeZone(mTimeZone));

        String formattedTime = format.format(date);

        return "At " + formattedTime + " it is";
    }

    public String getFormattedTemperature() {
        return String.valueOf(Math.round(getTemperature())); // + " at " + format.format(date);
    }

    public String getIconID() {
        String icon = getIcon();
        String iconID;

        switch (icon.toString()) {
            //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
            case "clear-night":
                iconID = "clear_night";
                break;
            case "rain":
                iconID = "rain";
                break;
            case "snow":
                iconID = "snow";
                break;
            case "sleet":
                iconID = "sleet";
                break;
            case "wind":
                iconID = "wind";
                break;
            case "fog":
                iconID = "fog";
                break;
            case "cloudy":
                iconID = "cloudy";
                break;
            case "partly-cloudy-night":
                iconID = "cloudy_night";
                break;
            case "partly-cloudy-day":
                iconID = "partly_cloudy";
                break;
            default:
                iconID = "clear_day";
        }

        return iconID;
    }
}
