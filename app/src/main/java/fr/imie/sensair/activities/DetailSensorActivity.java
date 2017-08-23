package fr.imie.sensair.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import fr.imie.sensair.R;
import fr.imie.sensair.adapters.SensorAdapter;
import fr.imie.sensair.model.Sensor;
import fr.imie.sensair.properties.ApiProperties;

public class DetailSensorActivity extends AppCompatActivity {
    protected TextView displayNameTextView;
    protected TextView vendorTextView;
    protected TextView productTextView;
    protected TextView versionTextView;
    protected TextView textViewTemperatureValue;
    protected TextView textViewHumidityValue;
    protected TextView textViewAirQualityValue;
    protected TextView textViewDustValue;
    private RequestQueue queue;
    private RequestQueue queue2;
    private Sensor sensor;
    private JSONObject temperature;
    private JSONObject humidity;
    private JSONObject airquality;
    private JSONObject dust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sensor);

        this.displayNameTextView = (TextView) this.findViewById(R.id.textViewDisplayName);
        this.vendorTextView = (TextView) this.findViewById(R.id.textViewVendor);
        this.productTextView = (TextView) this.findViewById(R.id.textViewProduct);
        this.versionTextView = (TextView) this.findViewById(R.id.textViewVersion);

        this.textViewTemperatureValue = (TextView) this.findViewById(R.id.textViewTemperatureValue);
        this.textViewHumidityValue = (TextView) this.findViewById(R.id.textViewHumidityValue);
        this.textViewAirQualityValue = (TextView) this.findViewById(R.id.textViewAirQualityValue);
        this.textViewDustValue = (TextView) this.findViewById(R.id.textViewDustValue);

        this.sensor = new Sensor();
        this.sensor.setId(this.getIntent().getIntExtra("idSensor", 0));

        this.queue = Volley.newRequestQueue(this);
        this.queue2 = Volley.newRequestQueue(this);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                DetailSensorActivity.AsyncTaskSensors threadA = new DetailSensorActivity.AsyncTaskSensors();

                try {
                    JSONArray jsonArray = threadA.execute().get();
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Sensor sensor = new Sensor();
                            sensor.setId(jsonObject.getInt("id"));
                            sensor.setDisplayName(jsonObject.getString("displayname"));
                            sensor.setUuid(jsonObject.getString("uuid"));
                            sensor.setVendor(jsonObject.getString("vendor"));
                            sensor.setProduct(jsonObject.getString("product"));
                            sensor.setVersion(jsonObject.getString("version"));
                            sensor.setEnable(jsonObject.getBoolean("enable"));
                            DetailSensorActivity.this.sensor = sensor;
                        }

                        DetailSensorActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DetailSensorActivity.this.displayNameTextView.setText(DetailSensorActivity.this.sensor.getDisplayName());
                                DetailSensorActivity.this.vendorTextView.setText(DetailSensorActivity.this.sensor.getVendor());
                                DetailSensorActivity.this.productTextView.setText(DetailSensorActivity.this.sensor.getProduct());
                                DetailSensorActivity.this.versionTextView.setText(DetailSensorActivity.this.sensor.getVersion());
                            }
                        });
                    }
                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                DetailSensorActivity.AsyncTaskDataSensors threadB = new DetailSensorActivity.AsyncTaskDataSensors();

                try {
                    JSONObject jsonArray = threadB.execute().get();
                    if (jsonArray != null) {
                        DetailSensorActivity.this.temperature = (JSONObject) jsonArray.get("temperature");
                        DetailSensorActivity.this.humidity = (JSONObject) jsonArray.get("humidity");
                        DetailSensorActivity.this.airquality = (JSONObject) jsonArray.get("airquality");
                        DetailSensorActivity.this.dust = (JSONObject) jsonArray.get("dust");

                        DetailSensorActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    DetailSensorActivity.this.textViewTemperatureValue.setText(DetailSensorActivity.this.temperature.get("last").toString() + " °C");
                                    DetailSensorActivity.this.textViewHumidityValue.setText(DetailSensorActivity.this.humidity.get("last").toString() + "%");

                                    if ((int) DetailSensorActivity.this.airquality.get("last") == 3) {
                                        DetailSensorActivity.this.textViewAirQualityValue.setText("Air fresh");
                                    } else if ((int) DetailSensorActivity.this.airquality.get("last") == 2) {
                                        DetailSensorActivity.this.textViewAirQualityValue.setText("Low pollution");
                                    } else if ((int) DetailSensorActivity.this.airquality.get("last") == 1) {
                                        DetailSensorActivity.this.textViewAirQualityValue.setText("High pollution");
                                    } else {
                                        DetailSensorActivity.this.textViewAirQualityValue.setText("No data received");
                                    }

                                    DetailSensorActivity.this.textViewDustValue.setText(DetailSensorActivity.this.dust.get("last").toString() + " pcs/0.01cf");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
        t2.start();
    }

    private class AsyncTaskSensors extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... params) {
            final RequestFuture<JSONArray> future = RequestFuture.newFuture();
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    ApiProperties.getInstance().addressServer + ApiProperties.getInstance().apiPath + "/sensors?filters[id]=" + DetailSensorActivity.this.sensor.getId(),
                    null,
                    future,
                    future
            );

            DetailSensorActivity.this.queue.add(request);

            try {
                return future.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class AsyncTaskDataSensors extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            final RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    ApiProperties.getInstance().addressServer + ApiProperties.getInstance().apiPath + "/datasensors/" + DetailSensorActivity.this.sensor.getId(),
                    null,
                    future,
                    future
            );

            DetailSensorActivity.this.queue2.add(request);

            try {
                return future.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
