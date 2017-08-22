package fr.imie.sensair.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import fr.imie.sensair.R;
import fr.imie.sensair.adapters.SensorAdapter;
import fr.imie.sensair.api.SensorApiService;
import fr.imie.sensair.model.Sensor;
import fr.imie.sensair.model.User;

public class AddSensorActivity extends AppCompatActivity {
    private EditText uuidEditText;
    private EditText displayNameEditText;
    private Button validateButton;
    private SharedPreferences prefs;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        uuidEditText = (EditText) findViewById(R.id.editTextUuid);
        displayNameEditText = (EditText) findViewById(R.id.editTextDisplayName);
        validateButton = (Button) findViewById(R.id.buttonValidate);

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String json = this.prefs.getString("currentUser", "");
        this.currentUser = gson.fromJson(json, User.class);


        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(uuidEditText.getText().toString().equals(""))) {
                    SensorApiService sensorApiService = new SensorApiService();
                    sensorApiService.updateCustomer(AddSensorActivity.this, currentUser, uuidEditText.getText().toString(), displayNameEditText.getText().toString());
                } else {
                    Toast.makeText(AddSensorActivity.this, "This sensor is not register.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
