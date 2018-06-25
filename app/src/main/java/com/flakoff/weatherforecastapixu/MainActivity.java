package com.flakoff.weatherforecastapixu;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.flakoff.weatherforecastapixu.data.ForecastData;
import com.flakoff.weatherforecastapixu.model.Location;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flakoff.weatherforecastapixu.data.ForecastViewPagerAdapter;
import com.flakoff.weatherforecastapixu.data.LocationData;
import com.flakoff.weatherforecastapixu.model.Weather;
import com.flakoff.weatherforecastapixu.util.AsyncResponse;
import com.flakoff.weatherforecastapixu.util.Prefs;
import com.github.pwittchen.weathericonview.WeatherIconView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ForecastViewPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private ListView listView;
    private EditText searchEditText;
    private ProgressBar bar;
    private TextView currentCity, currentTime, currentTemp,
            pressureText, humidityText, cloudsText,
            precipText, visText, windText;
    private WeatherIconView currentIcon;
    private Prefs prefs;
    private Location location;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = new com.flakoff.weatherforecastapixu.model.Location();

        bar = findViewById(R.id.progressBar);
        viewPager = findViewById(R.id.viewPager);
        currentCity = findViewById(R.id.currentCity);
        currentTime = findViewById(R.id.currentTime);
        currentTemp = findViewById(R.id.currentTemp);

        pressureText = findViewById(R.id.pressureText);
        humidityText = findViewById(R.id.humidityText);
        cloudsText = findViewById(R.id.cloudsText);
        precipText = findViewById(R.id.precipText);
        visText = findViewById(R.id.visText);
        windText = findViewById(R.id.windText);

        currentIcon = findViewById(R.id.currentIcon);
        searchEditText = findViewById(R.id.searchEditText);

        listView = findViewById(R.id.list);
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item);
        listView.setAdapter(adapter);

        prefs = new Prefs(this);
        String prefsLocation = prefs.getLocation();
        getWeather(prefsLocation);


        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    getLocation(searchEditText.getText().toString());
                    return true;
                }
                return false;
            }
        });


    }

    public void errorToast(Exception e) {
        Log.d("RESPONSE ERROR", "Error");
        if (e instanceof TimeoutError || e instanceof NoConnectionError) {
            Toast.makeText(this, "No connection", Toast.LENGTH_LONG).show();
        } else if (e instanceof ServerError) {
            Toast.makeText(this, "Server error", Toast.LENGTH_LONG).show();

        } else if (e instanceof NetworkError) {
            Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();

        } else if (e instanceof ParseError) {
            Toast.makeText(this, "Parse error", Toast.LENGTH_LONG).show();

        }

    }


    private void getCurrentIcon(int condId, boolean isDay) {
        switch (condId) {
            case 1000:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_sunny));
                else currentIcon.setIconResource(getString(R.string.wi_night_clear));
            case 1003:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_cloudy));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_partly_cloudy));
            case 1006:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_cloudy));
                else currentIcon.setIconResource(getString(R.string.wi_cloudy));
            case 1009:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_sunny_overcast));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_partly_cloudy));
            case 1030:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_fog));
                else currentIcon.setIconResource(getString(R.string.wi_night_fog));

            case 1063:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_sprinkle));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_sprinkle));
            case 1066:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_snow));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_snow));
            case 1069:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_sleet));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_sleet));
            case 1072:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_snow_wind));
                else currentIcon.setIconResource(getString(R.string.wi_snow_wind));
            case 1087:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_thunderstorm));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_thunderstorm));

            case 1114:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_snow_wind));
                else currentIcon.setIconResource(getString(R.string.wi_snow_wind));
            case 1117:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_snow_wind));
                else currentIcon.setIconResource(getString(R.string.wi_snow_wind));
            case 1135:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_fog));
                else currentIcon.setIconResource(getString(R.string.wi_fog));
            case 1147:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_fog));
                else currentIcon.setIconResource(getString(R.string.wi_fog));
            case 1150:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_sprinkle));
                else currentIcon.setIconResource(getString(R.string.wi_sprinkle));

            case 1153:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_sprinkle));
                else currentIcon.setIconResource(getString(R.string.wi_sprinkle));
            case 1168:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_sprinkle));
                else currentIcon.setIconResource(getString(R.string.wi_sprinkle));
            case 1171:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_sprinkle));
                else currentIcon.setIconResource(getString(R.string.wi_sprinkle));
            case 1180:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_rain_mix));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_rain_mix));
            case 1183:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_rain));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_rain));

            case 1186:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_rain));
                else currentIcon.setIconResource(getString(R.string.wi_rain));
            case 1189:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_rain));
                else currentIcon.setIconResource(getString(R.string.wi_rain));
            case 1192:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_showers));
                else currentIcon.setIconResource(getString(R.string.wi_showers));
            case 1195:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_showers));
                else currentIcon.setIconResource(getString(R.string.wi_showers));
            case 1198:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_sleet));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_sleet));

            case 1201:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_sleet));
                else currentIcon.setIconResource(getString(R.string.wi_sleet));
            case 1204:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_sleet));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_sleet));
            case 1207:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_sleet));
                else currentIcon.setIconResource(getString(R.string.wi_sleet));
            case 1210:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_snow));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_snow));
            case 1213:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_snow));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_snow));

            case 1216:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_snow));
                else currentIcon.setIconResource(getString(R.string.wi_snow));
            case 1219:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_snow));
                else currentIcon.setIconResource(getString(R.string.wi_snow));
            case 1222:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_snow_wind));
                else currentIcon.setIconResource(getString(R.string.wi_snow_wind));
            case 1225:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_snow_wind));
                else currentIcon.setIconResource(getString(R.string.wi_snow_wind));
            case 1237:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_hail));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_hail));

            case 1240:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_showers));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_showers));
            case 1243:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_showers));
                else currentIcon.setIconResource(getString(R.string.wi_showers));
            case 1246:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_showers));
                else currentIcon.setIconResource(getString(R.string.wi_showers));
            case 1249:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_sleet));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_sleet));
            case 1252:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_sleet));
                else currentIcon.setIconResource(getString(R.string.wi_sleet));

            case 1255:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_snow));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_snow));
            case 1258:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_snow));
                else currentIcon.setIconResource(getString(R.string.wi_snow));
            case 1261:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_hail));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_hail));
            case 1264:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_hail));
                else currentIcon.setIconResource(getString(R.string.wi_hail));
            case 1273:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_day_thunderstorm));
                else currentIcon.setIconResource(getString(R.string.wi_night_alt_thunderstorm));

            case 1276:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_thunderstorm));
                else currentIcon.setIconResource(getString(R.string.wi_thunderstorm));
            case 1279:
                if (isDay)
                    currentIcon.setIconResource(getString(R.string.wi_day_snow_thunderstorm));
                else currentIcon.setIconResource(getString(R.string.wi_night_snow_thunderstorm));
            case 1282:
                if (isDay) currentIcon.setIconResource(getString(R.string.wi_storm_showers));
                else currentIcon.setIconResource(getString(R.string.wi_storm_showers));
        }

    }

    private void getLocation(String userText) {
        bar.setVisibility(View.VISIBLE);
        searchEditText.setActivated(false);
        final ArrayList<String> cities = new ArrayList<>();

        new LocationData().getLocations(new AsyncResponse() {
            @Override
            public void processFinished(final ArrayList locationsList) {

                cities.clear();
                for (Object loc : locationsList) {
                    cities.add(((Location) loc).getCity());
                    Log.d("CITY ADDED", ((Location) loc).getCity());

                }
                listView.setVisibility(View.VISIBLE);
                bar.setVisibility(View.GONE);
                searchEditText.setActivated(true);
                adapter.clear();
                adapter.addAll(cities);
                Log.d("ADAPTER ADDED", cities.get(0));


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        location = (com.flakoff.weatherforecastapixu.model.Location) locationsList.get(i);
                        prefs.setLocation(location.getLat() + "," + location.getLon());
                        prefs.setCity(location.getCity());
                        Log.d("PREFS CITY", prefs.getLocation());
                        listView.setVisibility(View.GONE);
                        getWeather(prefs.getLocation());
                    }
                });

            }


        }, userText);
    }

    public void getWeather(final String location) {

        pagerAdapter = new ForecastViewPagerAdapter(getSupportFragmentManager(),
                getFragments(location));
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);


    }

    private List<Fragment> getFragments(String location) {
        bar.setVisibility(View.VISIBLE);
        final List<Fragment> fragmentList = new ArrayList<>();

        new ForecastData().getForecast(new AsyncResponse() {
            @Override
            public void processFinished(ArrayList weatherList) {
                fragmentList.clear();
                setCurrentWeather((Weather) weatherList.get(0));
                for (int i = 1; i < weatherList.size(); i++) {
                    Weather weatherElement = (Weather) weatherList.get(i);
                    ForecastFragment forecastFragment = ForecastFragment.newInstance(weatherElement);
                    fragmentList.add(forecastFragment);
                }
                bar.setVisibility(View.GONE);
                pagerAdapter.notifyDataSetChanged();
            }
        }, location);
        return fragmentList;
    }

    private void setCurrentWeather(Weather currentWeather) {
        currentCity.setText(prefs.getCity());
        currentTime.setText(currentWeather.getDate());
        currentTemp.setText(String.format("%s°C", String.valueOf(currentWeather.getTemp())));
        getCurrentIcon(currentWeather.getCondId(), currentWeather.isDay());

        pressureText.setText(String.format("%smbar", currentWeather.getPressure()));
        humidityText.setText(String.format("%s%%", currentWeather.getHumidity()));
        cloudsText.setText(String.format("%s%%", currentWeather.getClouds()));
        precipText.setText(String.format("%smm", currentWeather.getPrecip()));
        visText.setText(String.format("%skm", currentWeather.getVisibility()));
        windText.setText(String.format("%skm/h", currentWeather.getWindSpeed()));
    }

}