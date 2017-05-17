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

import com.google.gson.Gson;

import fr.imie.sensair.R;
import fr.imie.sensair.model.User;

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
            startActivity(new Intent(this, SensorActivity.class));
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

                // TODO - Call API to login
                if (usernameEditText.getText().toString().equals("login") && passwordEditText.getText().toString().equals("password")) {
                    User currentUser = new User();
                    currentUser
                        .setUsername(usernameEditText.getText().toString())
                        .setPassword(passwordEditText.getText().toString())
                        .setFirstname("Sylvain")
                        .setLastname("Mirouf");

                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(currentUser);
                    prefsEditor.putString("currentUser", json);
                    prefsEditor.putBoolean("connected", true);
                    prefsEditor.commit();
                    finish();
                    startActivity(new Intent(LoginActivity.this, SensorActivity.class));
                }
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