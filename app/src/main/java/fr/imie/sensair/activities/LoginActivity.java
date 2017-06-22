package fr.imie.sensair.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fr.imie.sensair.R;
import fr.imie.sensair.api.UserApiService;
import fr.imie.sensair.model.User;
import fr.imie.sensair.properties.ApiProperties;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_KEY = "LOGIN_KEY";
    public static final String PASSWORD_KEY = "PASSWORD_KEY";

    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox passwordSaveCheckBox;
    private Button connectionButton;
    private Button registerButton;
    private SharedPreferences prefs;

    @Override
    protected void onResume() {
        super.onResume();

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (this.prefs.getBoolean("connected", false)) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (this.prefs.getBoolean("connected", false)) {
            startActivity(new Intent(this, SensorActivity.class));
            finish();
        }

        usernameEditText = (EditText) findViewById(R.id.editTextUsername);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        passwordSaveCheckBox = (CheckBox) findViewById(R.id.checkBoxPasswordSave);
        connectionButton = (Button) findViewById(R.id.buttonConnect);
        registerButton = (Button) findViewById(R.id.buttonRegister);

        if (prefs.contains(LOGIN_KEY) && prefs.contains(PASSWORD_KEY)) {
            usernameEditText.setText(prefs.getString(LOGIN_KEY, ""));
            passwordEditText.setText(prefs.getString(PASSWORD_KEY, ""));
            passwordSaveCheckBox.setChecked(true);
        }

        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordSaveCheckBox.isChecked()) {
                    prefs.edit().putString(LOGIN_KEY, usernameEditText.getText().toString()).apply();
                    prefs.edit().putString(PASSWORD_KEY, passwordEditText.getText().toString()).apply();
                } else {
                    prefs.edit().remove(LOGIN_KEY).remove(PASSWORD_KEY).apply();
                }

                UserApiService userApiService = new UserApiService();
                userApiService.login(usernameEditText.getText().toString(), passwordEditText.getText().toString(), LoginActivity.this);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}