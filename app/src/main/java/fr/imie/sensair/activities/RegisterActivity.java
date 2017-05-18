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

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    EditText usernameText;
    EditText emailText;
    EditText passwordText;
    EditText confirmPasswordText;
    Button registerButton;
    ProgressBar progressBar;
    TextView responseView;

    static final String API_URL = "http://iot.lacji.net/api/customers";

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
        responseView = (TextView) findViewById(R.id.responseView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncPostTask().execute();
            }
        });
    }

    private class AsyncPostTask extends AsyncTask<Void, Void, String> {
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
        Exception exception;
        String email = emailText.getText().toString();
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String confirm_password = confirmPasswordText.getText().toString();

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        protected String doInBackground(Void... urls) {

            if (email == null || !isEmailValid(email)) {
                return getString(R.string.register_unvalid_email);
            } else if (password == null || confirm_password == null || !confirm_password.matches(password)) {
                return getString(R.string.register_password_not_match);
            } else if (username == null) {
                return getString(R.string.register_unvalid_name);
            } else {

                try {
                    URL url = new URL(API_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.connect();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("email", email);
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);

                    DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                    wr.writeBytes(jsonObject.toString());
                    wr.flush();
                    wr.close();

                    BufferedReader bufferedReader;
                    if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                        bufferedReader = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    } else {
                        bufferedReader  = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    String output;
                    while ((output = bufferedReader.readLine()) != null) {
                        stringBuilder.append(output);
                    }
                    return stringBuilder.toString();

                } catch(Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    return null;
                }
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            responseView.setText(response);
        }
    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
