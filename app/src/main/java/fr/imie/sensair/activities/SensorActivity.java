package fr.imie.sensair.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.imie.sensair.R;
import fr.imie.sensair.adapters.SensorAdapter;
import fr.imie.sensair.model.Sensor;
import fr.imie.sensair.services.AirQualityExceptionService;

public class SensorActivity extends AppCompatActivity {
    protected Button addSensorButton;
    protected ListView sensorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        this.addSensorButton = (Button) this.findViewById(R.id.addSensorButton);
        this.sensorList = (ListView) findViewById(R.id.listView);

        ArrayList<Sensor> sensors = generateSensors();

        final SensorAdapter adapter = new SensorAdapter(SensorActivity.this, sensors);
        this.sensorList.setAdapter(adapter);

        Intent intent = new Intent(this, AirQualityExceptionService.class);
        intent.putExtra("foo", "bar");
        this.bindService(intent, SensorActivity.this.connection, Context.BIND_AUTO_CREATE);

        this.sensorList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View v, int position, long id){
                startActivity(new Intent(SensorActivity.this, DetailSensorActivity.class));
            }
        });

        this.addSensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorActivity.this, AddSensorActivity.class));
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

    private ArrayList<Sensor> generateSensors() {
        /*ArrayList<Sensor> sensors = SensorAdapter.getAll();*/
        ArrayList<Sensor> sensors = new ArrayList<>();

        /*if (sensors.size() == 0) {*/
            Sensor sensor = new Sensor();
            sensor.setDisplayName("Home");
            sensor.setEnable(true);
            sensor.setVendor("Raspberry");
            sensor.setProduct("Pi");
            sensor.setVersion(3);
            sensor.setUuid("c10d2fc4-9361-4c24-91f4-c355379cbf44");
            /*sensor.setUser();
            sensor.save();*/

            Sensor sensor2 = new Sensor();
            sensor2.setDisplayName("Work");
            sensor2.setEnable(false);
            sensor2.setVendor("Raspberry");
            sensor2.setProduct("Pi");
            sensor2.setVersion(2);
            sensor2.setUuid("086edadc-feaa-495d-bfc3-58d905d3ddcb");
            /*sensor2.setUser();
            sensor2.save();

            sensors = SensorAdapter.getAll()

        }*/
            sensors.add(sensor);
            sensors.add(sensor2);

        return sensors;
    }
}
