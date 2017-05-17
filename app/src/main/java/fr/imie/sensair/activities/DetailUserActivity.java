package fr.imie.sensair.activities;

import android.content.Intent;
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
import fr.imie.sensair.model.User;

public class DetailUserActivity extends AppCompatActivity {
    protected EditText firstnameEditText;
    protected EditText lastnameEditText;
    protected EditText loginEditText;
    protected EditText passwordEditText;
    protected EditText confirmPasswordEditText;
    protected Button saveButton;
    protected Button logoutButton;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        this.firstnameEditText = (EditText) this.findViewById(R.id.editTextFirstname);
        this.lastnameEditText = (EditText) this.findViewById(R.id.editTextLastname);
        this.loginEditText = (EditText) this.findViewById(R.id.editTextLogin);
        this.passwordEditText = (EditText) this.findViewById(R.id.editTextPassword);
        this.confirmPasswordEditText = (EditText) this.findViewById(R.id.editTextConfirmPassword);
        this.saveButton = (Button) this.findViewById(R.id.buttonSave);
        this.logoutButton = (Button) this.findViewById(R.id.buttonLogout);

        Gson gson = new Gson();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String json = this.preferences.getString("currentUser", "");
        User currentUser = gson.fromJson(json, User.class);

        Toast.makeText(DetailUserActivity.this, currentUser.getFirstname() + " " + currentUser.getLastname(), Toast.LENGTH_LONG);

        this.firstnameEditText.setText(currentUser.getFirstname());
        this.lastnameEditText.setText(currentUser.getLastname());
        this.loginEditText.setText(currentUser.getLogin());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor prefsEditor = DetailUserActivity.this.preferences.edit();
                prefsEditor.remove("currentUser");
                Intent logoutIntent = new Intent(DetailUserActivity.this, LoginActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();
            }
        });
    }
}
