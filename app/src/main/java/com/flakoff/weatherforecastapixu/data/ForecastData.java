package com.flakoff.weatherforecastapixu.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flakoff.weatherforecastapixu.MainActivity;
import com.flakoff.weatherforecastapixu.control.AppController;
import com.flakoff.weatherforecastapixu.model.Weather;
import com.flakoff.weatherforecastapixu.util.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ForecastData {
    ArrayList<Weather> forecast = new ArrayList<>();

    public void getForecast(final AsyncResponse callback, String location){
        String url = "http://api.apixu.com/v1/forecast.json?key=67eb7910f8734f3b9a385906181606&days=7&q="
                + location;

        Log.d("WEATHER URL", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    Weather weather = new Weather();

                    JSONObject current = response.getJSONObject("current");
                    weather.setDate(current.getString("last_updated"));
                    weather.setTemp(Float.parseFloat(current.getString("temp_c")));
                    if (Integer.parseInt(current.getString("is_day")) == 1) weather.setDay(true);
                    else
                        weather.setDay(false);
                    weather.setWindSpeed(Float.parseFloat(current.getString("wind_kph")));
                    weather.setPressure(Float.parseFloat(current.getString("pressure_mb")));
                    weather.setPrecip(Float.parseFloat(current.getString("precip_mm")));
                    weather.setHumidity(Float.parseFloat(current.getString("humidity")));
                    weather.setClouds(Float.parseFloat(current.getString("cloud")));
                    weather.setVisibility(Float.parseFloat(current.getString("vis_km")));

                    JSONObject condition = current.getJSONObject("condition");
                    weather.setCondId(condition.getInt("code"));
                    weather.setCondition(condition.getString("text"));
                    forecast.add(weather);

                    JSONArray forecastArray = response.getJSONObject("forecast").getJSONArray("forecastday");
                    for (int i = 0; i < forecastArray.length(); i++) {
                        Weather forecastDay = new Weather();
                        JSONObject object = forecastArray.getJSONObject(i);
                        forecastDay.setDate(object.getString("date"));
                        JSONObject day = object.getJSONObject("day");
                        forecastDay.setMaxTemp(Float.parseFloat(day.getString("maxtemp_c")));
                        forecastDay.setMinTemp(Float.parseFloat(day.getString("mintemp_c")));
                        forecastDay.setWindSpeed(Float.parseFloat(day.getString("maxwind_kph")));
                        forecastDay.setPrecip(Float.parseFloat(day.getString("totalprecip_mm")));
                        forecastDay.setVisibility(Float.parseFloat(day.getString("avgvis_km")));
                        forecastDay.setHumidity(Float.parseFloat(day.getString("avghumidity")));
                        JSONObject conditionForecast = day.getJSONObject("condition");
                        forecastDay.setCondition(conditionForecast.getString("text"));
                        forecastDay.setCondId(conditionForecast.getInt("code"));
                        JSONObject astro = object.getJSONObject("astro");
                        forecastDay.setSunrise(astro.getString("sunrise"));
                        forecastDay.setSunset(astro.getString("sunset"));

                        forecast.add(forecastDay);

                        Log.d("SIZEEEE", String.valueOf(forecast.size()));

                    }

                } catch (JSONException e){
                    new MainActivity().errorToast(e);
                    e.printStackTrace();
                }

                callback.processFinished(forecast);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new MainActivity().errorToast(error);


            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
