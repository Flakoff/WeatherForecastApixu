package com.flakoff.weatherforecastapixu.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flakoff.weatherforecastapixu.MainActivity;
import com.flakoff.weatherforecastapixu.R;
import com.flakoff.weatherforecastapixu.control.AppController;
import com.flakoff.weatherforecastapixu.model.Location;
import com.flakoff.weatherforecastapixu.util.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class LocationData  {
    ArrayList<Location> locations = new ArrayList<>();


    public void getLocations(final AsyncResponse callback, String userText){

        userText = userText.replaceAll(" ", "%20");

        String url = "http://dev.virtualearth.net/REST/v1/Locations?q=" + userText +
                "&maxResults=10&key=AniSuEduMeICdSyL_24gwVmlYlcjgsrnWVVQ9dF4Q1WzKi46izxmCPgxrxOwKvgc";
        Log.d("URL", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    JSONArray resourses = response.getJSONArray("resourceSets")
                            .getJSONObject(0)
                            .getJSONArray("resources");
                    for (int i = 0; i < resourses.length(); i++) {
                        Location location = new Location();
                        JSONObject object = resourses.getJSONObject(i);
                        location.setCity(object.getString("name"));
                        JSONObject address = object.getJSONObject("address");
                        //location.setRegion(address.getString("adminDistrict"));
                        location.setCountry(address.getString("countryRegion"));
                        JSONArray coordinates = object.getJSONObject("point")
                                .getJSONArray("coordinates");
                        location.setLat(Float.parseFloat(coordinates.getString(0)));
                        location.setLon(Float.parseFloat(coordinates.getString(1)));
                        Log.d("URL", coordinates.getString(0));
                        locations.add(location);

                    }
                }
                catch (JSONException e) {
                    new MainActivity().errorToast(e);
                    e.printStackTrace();
                }

                callback.processFinished(locations);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new MainActivity().errorToast(error);
                error.printStackTrace();

            }
        });


        /* APIXU request: URL = http://api.apixu.com/v1/search.json?key=67eb7910f8734f3b9a385906181606&q=

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(JsonArrayRequest.Method.GET, url,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.d("CITIES", response.getString(0));
                            for (int i = 0; i < response.length(); i++){
                                JSONObject locationObject = response.getJSONObject(i);
                                Location location = new Location();
                                location.setCity(locationObject.getString("name"));
                                location.setRegion(locationObject.getString("region"));
                                location.setCountry(locationObject.getString("country"));
                                location.setLat(Float.parseFloat(locationObject.getString("lat")));
                                location.setLon(Float.parseFloat(locationObject.getString("lon")));
                                locations.add(location);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        callback.processFinished(locations);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESPONSE ERROR", "Error");
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.d("RESPONSE ERROR", "noconnection");
                } else if (error instanceof ServerError) {
                    Log.d("RESPONSE ERROR", "server");

                } else if (error instanceof NetworkError) {
                    Log.d("RESPONSE ERROR", "network");

                } else if (error instanceof ParseError) {
                    Log.d("RESPONSE ERROR", "parse");

                }


            }
        });

        */

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }
}
