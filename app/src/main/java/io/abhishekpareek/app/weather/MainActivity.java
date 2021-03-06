package io.abhishekpareek.app.weather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private GPSTracker mGPSTracker;
    private double latitude;
    private double longitude;

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

    @Bind(R.id.temperatureLabel) TextView temperatureView;
    @Bind(R.id.timeLabel) TextView timeView;
    @Bind(R.id.currentWeatherImageView) ImageView currentWeatherImageView;
    @Bind(R.id.humidityValue) TextView humidityView;
    @Bind(R.id.precipitationValue) TextView precipitationView;
    @Bind(R.id.summaryLabel) TextView summaryView;
    @Bind(R.id.weatherUpdateBar) ProgressBar mWeatherUpdateBar;
    @Bind(R.id.locationLabel) TextView locationLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGPSTracker = new GPSTracker(MainActivity.this);

        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWeatherUpdateBar.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Fetching current weather data ...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                getCurrentWeather(latitude, longitude);
            }
        });
        */
    }

    private void getCurrentLocation() {
        if (mGPSTracker.canGetLocation()) {
            latitude = mGPSTracker.getLatitude();
            longitude = mGPSTracker.getLongitude();

            Snackbar.make(mCoordinatorLayout, "Your Location is - Lat: " + latitude + ", Long: " + longitude, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            mGPSTracker.showSettingsAlert();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mWeatherUpdateBar.setVisibility(View.INVISIBLE);

        // Get the current device location here
        getCurrentLocation();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Fetching current weather data ...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                getCurrentWeather(latitude, longitude);
            }
        });


        getCurrentWeather(latitude, longitude);
    }

    private void getCurrentWeather(double latitude, double longitude) {
        if (isNetworkAvailable()) {
            mWeatherUpdateBar.setVisibility(View.VISIBLE);
            getWeatherDataFromURL("https://api.forecast.io/forecast/8980c1e00b2ca68801c88d956b9b27d0/" + latitude + "," + longitude);
        } else {
            alertUser("Network Unavailable");
        }
    }

    private boolean isNetworkAvailable() {
        boolean isConnected = false;

        ConnectivityManager cm =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                              activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    void getWeatherDataFromURL(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        //Response response = client.newCall(request).execute();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                alertUser(e.toString());
                //e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                Log.v(TAG, jsonData);
                if (response.isSuccessful()) {
                    getCurrentWeatherData(jsonData);
                } else {
                    alertUser("Something went wrong!");
                }
            }
        });
    }

    private void getCurrentWeatherData(String jsonData) {
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(jsonData);

            JSONObject currentData = jsonResponse.getJSONObject("currently");

            final CurrentWeatherData currentWeatherData = new CurrentWeatherData();
            currentWeatherData.setTimeZone(jsonResponse.getString("timezone"));

            currentWeatherData.setSummary(currentData.getString("summary"));
            currentWeatherData.setIcon(currentData.getString("icon"));
            currentWeatherData.setTemperature((currentData.getDouble("temperature") - 32) * 5 / 9);
            currentWeatherData.setPrecipitationChance(currentData.getDouble("precipProbability"));
            currentWeatherData.setTime(currentData.getLong("time"));
            currentWeatherData.setHumidity(currentData.getDouble("humidity"));

            //currentWeatherData.set(currentData.(""));
            Log.d(TAG, currentWeatherData.toString());
            Log.d(TAG, String.valueOf(currentWeatherData.getIconID()));

            // model data binding to the view
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    locationLabel.setText(currentWeatherData.getTimeZone());
                    temperatureView.setText(currentWeatherData.getFormattedTemperature());
                    Log.d(TAG, temperatureView.getText().toString());
                    timeView.setText(currentWeatherData.getFormattedTime());
                    String uri = "@drawable/" + currentWeatherData.getIconID();
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    Drawable res = getResources().getDrawable(imageResource);
                    currentWeatherImageView.setImageDrawable(res);
                    humidityView.setText(String.valueOf(currentWeatherData.getHumidity()));
                    precipitationView.setText(String.valueOf(currentWeatherData.getPrecipitationChance()) + " %");
                    summaryView.setText(currentWeatherData.getSummary());
                    mWeatherUpdateBar.setVisibility(View.INVISIBLE);
                }
            });

            //JSONArray currentData = jsonResponse.optJSONArray("currently");
        } catch (JSONException e) {
            alertUser(e.toString());
        }
    }

    private void alertUser(String message) {
        Bundle bundle = new Bundle();
        bundle.putString("payload", message);

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setArguments(bundle);

        dialog.show(getFragmentManager(), "error_dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
