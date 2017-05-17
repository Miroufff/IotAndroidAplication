package fr.imie.sensair.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.imie.sensair.R;
import fr.imie.sensair.model.Sensor;

public class AddSensorActivity extends AppCompatActivity {

    private EditText uuidEditText;
    private EditText displayNameEditText;
    private Button validateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        uuidEditText = (EditText) findViewById(R.id.editTextUuid);
        displayNameEditText = (EditText) findViewById(R.id.editTextDisplayName);
        validateButton = (Button) findViewById(R.id.buttonValidate);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(uuidEditText.getText().toString().equals(""))) {
                    // TODO - Call api to get sensor
                    Sensor sensor = new Sensor();
                    sensor.setDisplayName("Rasp-Sensor.5");
                    sensor.setEnable(true);
                    sensor.setVendor("Raspberry");
                    sensor.setProduct("Pi");
                    sensor.setVersion(3);
                    sensor.setUuid("5151351-5151-zf4zf-54zfzf-65ezf4zef");

                } else {
                    Toast.makeText(AddSensorActivity.this, "This sensor is not register.", Toast.LENGTH_LONG);
                }
            }
        });
    }
}
