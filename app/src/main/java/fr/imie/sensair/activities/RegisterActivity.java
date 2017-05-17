package fr.imie.sensair.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.imie.sensair.R;
import fr.imie.sensair.adapters.UserAdapter;
import fr.imie.sensair.model.User;

public class RegisterActivity extends AppCompatActivity {
    protected EditText firstnameEditText;
    protected EditText lastnameEditText;
    protected EditText usernameEditText;
    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected EditText confirmPasswordEditText;
    private Button registerButton;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (this.prefs.getBoolean("connected", false)) {
            finish();

            return;
        }

        this.firstnameEditText = (EditText) this.findViewById(R.id.editTextFirstname);
        this.lastnameEditText = (EditText) this.findViewById(R.id.editTextLastname);
        this.usernameEditText = (EditText) this.findViewById(R.id.editTextUsername);
        this.emailEditText = (EditText) this.findViewById(R.id.editTextEmail);
        this.passwordEditText = (EditText) this.findViewById(R.id.editTextPassword);
        this.confirmPasswordEditText = (EditText) this.findViewById(R.id.editTextConfirmPassword);
        this.registerButton = (Button) this.findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User currentUser = new User();
                currentUser
                    .setFirstname(firstnameEditText.getText().toString())
                    .setLastname(lastnameEditText.getText().toString())
                    .setUsername(usernameEditText.getText().toString())
                    .setEmail(emailEditText.getText().toString())
                    .setPassword(passwordEditText.getText().toString());

                UserAdapter userAdapter = new UserAdapter();

                if (userAdapter.isUserValid(RegisterActivity.this, currentUser, confirmPasswordEditText.getText().toString())) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(currentUser);
                    prefsEditor.putString("currentUser", json);
                    prefsEditor.putBoolean("connected", true);
                    prefsEditor.commit();
                    finish();
                    startActivity(new Intent(RegisterActivity.this, SensorActivity.class));
                }
            }
        });
    }
}

