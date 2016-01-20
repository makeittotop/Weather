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

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public double getPrecipitationChance() {
        return mPrecipitationChance;
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

        return String.format("%.2f", getTemperature()) + " at " + format.format(date);
    }

    public Icons getIconID() {
        String icon = getIcon();
        Icons iconID;

        switch (icon.toString()) {
            //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
            case "clear-night":
                iconID = Icons.CLEAR_NIGHT;
                break;
            case "rain":
                iconID = Icons.RAIN;
                break;
            case "snow":
                iconID = Icons.SNOW;
                break;
            case "sleet":
                iconID = Icons.SLEET;
                break;
            case "wind":
                iconID = Icons.WIND;
                break;
            case "fog":
                iconID = Icons.FOG;
                break;
            case "cloudy":
                iconID = Icons.CLOUDY;
                break;
            case "partly-cloudy-night":
                iconID = Icons.PARTLY_CLOUDY_NIGHT;
                break;
            case "partly-cloudy-day":
                iconID = Icons.PARTLY_CLOUDY_DAY;
                break;
            default:
                iconID = Icons.CLEAR_DAY;
        }

        return iconID;
    }
}
