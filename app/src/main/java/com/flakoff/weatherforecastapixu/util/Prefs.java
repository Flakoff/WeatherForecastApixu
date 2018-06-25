package com.flakoff.weatherforecastapixu.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
    SharedPreferences preferences;

    public Prefs(Activity activity) {
        preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public void setLocation(String location) {
        preferences.edit().putString("location", location).apply();

    }

    public String getLocation(){
        return preferences.getString("location", "55.55003,37.36985");
    }

    public void setCity(String city) {
        preferences.edit().putString("city", city).apply();
    }

    public String getCity(){
        return preferences.getString("city", "Moscow, Russia");
    }
}
