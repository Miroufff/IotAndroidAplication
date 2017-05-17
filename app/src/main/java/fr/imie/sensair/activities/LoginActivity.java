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

    private EditText loginEditText;
    private EditText passwordEditText;
    private CheckBox passwordSaveCheckBox;
    private Button connectionButton;
    private Button registerButton;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = (EditText) findViewById(R.id.editTextLogin);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        passwordSaveCheckBox = (CheckBox) findViewById(R.id.checkBoxPasswordSave);
        connectionButton = (Button) findViewById(R.id.buttonConnect);
        registerButton = (Button) findViewById(R.id.buttonRegister);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.contains(LOGIN_KEY) && prefs.contains(PASSWORD_KEY)) {
            loginEditText.setText(prefs.getString(LOGIN_KEY, ""));
            passwordEditText.setText(prefs.getString(PASSWORD_KEY, ""));
            passwordSaveCheckBox.setChecked(true);
        }

        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordSaveCheckBox.isChecked()) {
                    prefs.edit().putString(LOGIN_KEY, loginEditText.getText().toString()).apply();
                    prefs.edit().putString(PASSWORD_KEY, passwordEditText.getText().toString()).apply();
                } else {
                    prefs.edit().remove(LOGIN_KEY).remove(PASSWORD_KEY).apply();
                }

                // TODO - Call API to login
                if (loginEditText.getText().toString().equals("login") && passwordEditText.getText().toString().equals("password")) {
                    User currentUser = new User();
                    currentUser
                        .setLogin(loginEditText.getText().toString())
                        .setPassword(passwordEditText.getText().toString())
                        .setFirstname("Sylvain")
                        .setLastname("Mirouf");

                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(currentUser);
                    prefsEditor.putString("currentUser", json);
                    prefsEditor.commit();

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