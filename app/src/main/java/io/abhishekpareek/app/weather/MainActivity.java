package io.abhishekpareek.app.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        if (isNetworkAvailable()) {
            getWeatherDataFromURL("https://api.forecast.io/forecast/8980c1e00b2ca68801c88d956b9b27d0/25.1019,55.1678");
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

        return;
    }

    private void getCurrentWeatherData(String jsonData) {
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(jsonData);

            JSONObject currentData = jsonResponse.getJSONObject("currently");

            final CurrentWeatherData currentWeatherData = new CurrentWeatherData();

            currentWeatherData.setSummary(currentData.getString("summary"));
            currentWeatherData.setTime(currentData.getLong("time"));
            currentWeatherData.setIcon(currentData.getString("icon"));
            currentWeatherData.setTemperature((currentData.getDouble("temperature") - 32) * 5 / 9);
            //currentWeatherData.setVisibility(currentData.getDouble("visibility"));
            //currentWeatherData.set(currentData.(""));
            Log.d(TAG, currentWeatherData.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tView = (TextView) findViewById(R.id.current_temp);
                    Log.d(TAG, tView.getText().toString());

                    tView.setText(String.format("%.2f", currentWeatherData.getTemperature()));
                    Log.d(TAG, tView.getText().toString());
                }
            });

            //JSONArray currentData = jsonResponse.optJSONArray("currently");
        } catch (JSONException e) {
            alertUser(e.toString());
        }

        return;
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