package com.flakoff.weatherforecastapixu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flakoff.weatherforecastapixu.model.Weather;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {


    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View forecastView = inflater.inflate(R.layout.forecast_fragment, container, false);
        TextView forecastDate = forecastView.findViewById(R.id.forecastDate);
        TextView forecastDescription = forecastView.findViewById(R.id.forecastDescription);
        TextView minTemp = forecastView.findViewById(R.id.minTempText);
        TextView maxTemp = forecastView.findViewById(R.id.maxTempText);
        TextView windForecast = forecastView.findViewById(R.id.windForecastText);
        TextView humidityForecast = forecastView.findViewById(R.id.humidityForecastText);
        TextView precipForecast = forecastView.findViewById(R.id.precipForecastText);
        TextView visForecast = forecastView.findViewById(R.id.visForecastText);
        TextView sunriseForecast = forecastView.findViewById(R.id.sunriseForecastText);
        TextView sunsetForecast = forecastView.findViewById(R.id.sunsetForecastText);

        Weather weather = (Weather) getArguments().getSerializable("weather");

        forecastDate.setText(weather.getDate());
        forecastDescription.setText(weather.getCondition());
        minTemp.setText(String.format("%s°C", weather.getMinTemp()));
        maxTemp.setText(String.format("%s°C", weather.getMaxTemp()));
        windForecast.setText(String.format("%skm/h", weather.getWindSpeed()));
        humidityForecast.setText(String.format("%s%%", weather.getHumidity()));
        precipForecast.setText(String.format("%smm", weather.getPrecip()));
        visForecast.setText(String.format("%skm", weather.getVisibility()));
        sunriseForecast.setText(weather.getSunrise());
        sunsetForecast.setText(weather.getSunset());

        return forecastView;
    }

    public static final ForecastFragment newInstance(Weather weather) {

        ForecastFragment forecastFragment = new ForecastFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("weather", weather);

        forecastFragment.setArguments(bundle);
        return forecastFragment;

    }

}
