package fr.imie.sensair.activities;

import android.os.AsyncTask;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import fr.imie.sensair.R;
import fr.imie.sensair.adapters.UserAdapter;
import fr.imie.sensair.api.UserApiService;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.imie.sensair.lib.Utils;
import fr.imie.sensair.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstnameText;
    private EditText lastnameText;
    private EditText usernameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstnameText = (EditText) findViewById(R.id.editTextFirstname);
        lastnameText = (EditText) findViewById(R.id.editTextLastname);
        usernameText = (EditText) findViewById(R.id.editTextUsername);
        emailText = (EditText) findViewById(R.id.editTextEmail);
        passwordText = (EditText) findViewById(R.id.editTextPassword);
        confirmPasswordText = (EditText) findViewById(R.id.editTextConfirmPassword);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Button registerButton = (Button) findViewById(R.id.buttonRegister);

        if (prefs.getBoolean("connected", false)) {
            finish();
            return;
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new AsyncPostTask().execute();
            }
        });
    }

    private class AsyncPostTask extends AsyncTask<Void, Void, String> {
        String firstname = firstnameText.getText().toString();
        String lastname = lastnameText.getText().toString();
        String username = usernameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirm_password = confirmPasswordText.getText().toString();

        protected String doInBackground(Void... urls) {

            if (email == null || !Utils.isEmailValid(email)) {
                return getString(R.string.register_unvalid_email);
            } else if (password == null || confirm_password == null || !confirm_password.matches(password)) {
                return getString(R.string.register_password_not_match);
            } else if (username == null) {
                return getString(R.string.register_unvalid_name);
            } else {

                User currentUser = new User();
                currentUser
                    .setFirstname(firstname)
                    .setLastname(lastname)
                    .setUsername(username)
                    .setEmail(email);

                UserAdapter userAdapter = new UserAdapter();
                if (userAdapter.isUserValid(RegisterActivity.this, currentUser, password, confirm_password, false)) {
                    UserApiService userApiService = new UserApiService();
                    userApiService.register(currentUser, password, RegisterActivity.this);

                    try {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
                        SharedPreferences.Editor prefsEditor = prefs.edit();
                        Gson gson = new Gson();
                        String jsonCurrentUser = gson.toJson(currentUser);

                        prefsEditor.putString("currentUser", jsonCurrentUser);
                        prefsEditor.putBoolean("connected", true);
                        prefsEditor.commit();
                        finish();

                        startActivity(new Intent(RegisterActivity.this, SensorActivity.class));
                    } catch (Exception e) {
                        Log.e("ERROR", e.getMessage(), e);
                        return null;
                    }
                } else {
                    return null;
                }
            }
            return null;
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "Error";
            }

            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
        }
    }
}
