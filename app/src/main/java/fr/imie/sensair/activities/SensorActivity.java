package fr.imie.sensair.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fr.imie.sensair.R;
import fr.imie.sensair.adapters.SensorAdapter;
import fr.imie.sensair.model.Sensor;
import fr.imie.sensair.model.User;
import fr.imie.sensair.services.AirQualityExceptionService;

public class SensorActivity extends AppCompatActivity {
    protected Button addSensorButton;
    protected Button detailUserButton;
    protected ListView sensorListView;
    private SharedPreferences prefs;
    private User currentUser;
    private SensorAdapter sensorAdapter;
    private List<Sensor> sensors;

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
        String json = this.prefs.getString("currentUser", "");
        this.currentUser = gson.fromJson(json, User.class);

        this.addSensorButton = (Button) this.findViewById(R.id.addSensorButton);
        this.detailUserButton = (Button) this.findViewById(R.id.detailUserButton);
        this.sensorListView = (ListView) findViewById(R.id.listView);

        this.sensors = retrieveSensors();

        this.sensorAdapter = new SensorAdapter(SensorActivity.this, sensors);
        this.sensorListView.setAdapter(this.sensorAdapter);

        Intent intent = new Intent(this, AirQualityExceptionService.class);
        intent.putExtra("foo", "bar");
        this.bindService(intent, SensorActivity.this.connection, Context.BIND_AUTO_CREATE);

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
                Toast.makeText(SensorActivity.this, "Value : " + SensorActivity.this.service.test(), Toast.LENGTH_LONG).show();
            }
        });

        this.detailUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorActivity.this, DetailUserActivity.class));
                Toast.makeText(SensorActivity.this, "Value : " + SensorActivity.this.service.test(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private AirQualityExceptionService service;

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
    };

    private List<Sensor> retrieveSensors() {
        // TODO - Call api to retrieve sensor
        List<Sensor> sensors = new ArrayList<>();

        Sensor sensor1 = new Sensor();
        sensor1.setDisplayName("Home");
        sensor1.setEnable(true);
        sensor1.setVendor("Raspberry");
        sensor1.setProduct("Pi");
        sensor1.setVersion("3");
        sensor1.setUuid("c10d2fc4-9361-4c24-91f4-c355379cbf44");
        sensor1.setUser(this.currentUser);

        Sensor sensor2 = new Sensor();
        sensor2.setDisplayName("Work");
        sensor2.setEnable(false);
        sensor2.setVendor("Raspberry");
        sensor2.setProduct("Pi");
        sensor2.setVersion("2");
        sensor2.setUuid("086edadc-feaa-495d-bfc3-58d905d3ddcb");
        sensor2.setUser(this.currentUser);

        sensors.add(sensor1);
        sensors.add(sensor2);

        return sensors;
    }
}
