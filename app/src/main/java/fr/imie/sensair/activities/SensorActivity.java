package fr.imie.sensair.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import fr.imie.sensair.R;
import fr.imie.sensair.adapters.SensorAdapter;
import fr.imie.sensair.model.Sensor;
import fr.imie.sensair.model.User;
import fr.imie.sensair.properties.ApiProperties;
import fr.imie.sensair.services.AirQualityExceptionService;

public class SensorActivity extends AppCompatActivity {
    protected Button addSensorButton;
    protected Button detailUserButton;
    protected ListView sensorListView;

    private RequestQueue queue;
    private SharedPreferences prefs;
    private User currentUser;
    private SensorAdapter sensorAdapter;
    private ArrayList<Sensor> sensors = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();

        if (!this.prefs.getBoolean("connected", false)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (!this.prefs.getBoolean("connected", false)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();

            return;
        }

        Gson gson = new Gson();
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String json = this.prefs.getString("currentUser", "");
        this.currentUser = gson.fromJson(json, User.class);

        this.addSensorButton = (Button) this.findViewById(R.id.addSensorButton);
        this.detailUserButton = (Button) this.findViewById(R.id.detailUserButton);
        this.sensorListView = (ListView) findViewById(R.id.listView);

        //SensorApiService sensorApiService = new SensorApiService(this, this.currentUser);
        //JSONArray jsonObject = sensorApiService.getSensorsByUser();

        this.queue = Volley.newRequestQueue(this);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                AsyncTaskSensors threadA = new AsyncTaskSensors();

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
                            sensor.setUser(SensorActivity.this.currentUser);
                            SensorActivity.this.sensors.add(sensor);
                        }

                        SensorActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final SensorAdapter adapter = new SensorAdapter(SensorActivity.this, SensorActivity.this.sensors);
                                SensorActivity.this.sensorListView.setAdapter(adapter);
                            }
                        });
                    }
                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

        /*Intent intent = new Intent(this, AirQualityExceptionService.class);
        intent.putExtra("foo", "bar");
        this.bindService(intent, SensorActivity.this.connection, Context.BIND_AUTO_CREATE);*/

        this.sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View v, int position, long id) {
                Sensor sensor = SensorActivity.this.sensors.get(position);
                Intent intent = new Intent(SensorActivity.this, DetailSensorActivity.class);
                intent.putExtra("idSensor", sensor.getId());

                startActivity(intent);
            }
        });

        this.addSensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorActivity.this, AddSensorActivity.class));
                //Toast.makeText(SensorActivity.this, "Value : " + SensorActivity.this.service.test(), Toast.LENGTH_LONG).show();
            }
        });

        this.detailUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorActivity.this, DetailUserActivity.class));
                //Toast.makeText(SensorActivity.this, "Value : " + SensorActivity.this.service.test(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*private AirQualityExceptionService service;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AirQualityExceptionService.AirQualityExceptionBinder binder = (AirQualityExceptionService.AirQualityExceptionBinder) service;
            SensorActivity.this.service = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            SensorActivity.this.service = null;
        }
    };*/

    private class AsyncTaskSensors extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... params) {
            final RequestFuture<JSONArray> future = RequestFuture.newFuture();
            JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                ApiProperties.getInstance().addressServer + ApiProperties.getInstance().apiPath + "/sensors?filters[customer]=" + SensorActivity.this.currentUser.getId(),
                null,
                future,
                future
            );

            SensorActivity.this.queue.add(request);

            try {
                return future.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
