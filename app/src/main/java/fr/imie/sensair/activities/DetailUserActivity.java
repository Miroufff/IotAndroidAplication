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
import fr.imie.sensair.adapters.UserAdapter;
import fr.imie.sensair.model.User;

public class DetailUserActivity extends AppCompatActivity {
    protected EditText firstnameEditText;
    protected EditText lastnameEditText;
    protected EditText usernameEditText;
    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected EditText confirmPasswordEditText;
    protected Button saveButton;
    protected Button logoutButton;
    private SharedPreferences preferences;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        this.firstnameEditText = (EditText) this.findViewById(R.id.editTextFirstname);
        this.lastnameEditText = (EditText) this.findViewById(R.id.editTextLastname);
        this.usernameEditText = (EditText) this.findViewById(R.id.editTextUsername);
        this.emailEditText = (EditText) this.findViewById(R.id.editTextEmail);
        this.passwordEditText = (EditText) this.findViewById(R.id.editTextPassword);
        this.confirmPasswordEditText = (EditText) this.findViewById(R.id.editTextConfirmPassword);
        this.saveButton = (Button) this.findViewById(R.id.buttonSave);
        this.logoutButton = (Button) this.findViewById(R.id.buttonLogout);

        Gson gson = new Gson();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String json = this.preferences.getString("currentUser", "");
        this.currentUser = gson.fromJson(json, User.class);

        this.firstnameEditText.setText(currentUser.getFirstname());
        this.lastnameEditText.setText(currentUser.getLastname());
        this.usernameEditText.setText(currentUser.getUsername());
        this.emailEditText.setText(currentUser.getEmail());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.setFirstname(firstnameEditText.getText().toString());
                currentUser.setLastname(lastnameEditText.getText().toString());
                currentUser.setUsername(usernameEditText.getText().toString());
                currentUser.setEmail(emailEditText.getText().toString());
                currentUser.setPassword(passwordEditText.getText().toString());

                UserAdapter userAdapter = new UserAdapter();

                if (userAdapter.isUserValid(DetailUserActivity.this, currentUser, confirmPasswordEditText.getText().toString())) {
                    // TODO - Call api to update user
                    SharedPreferences.Editor prefsEditor = DetailUserActivity.this.preferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(currentUser);
                    prefsEditor.putString("currentUser", json);
                    prefsEditor.commit();
                    Toast.makeText(DetailUserActivity.this, "User is saved", Toast.LENGTH_LONG).show();
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailUserActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                SharedPreferences.Editor prefsEditor = DetailUserActivity.this.preferences.edit();
                prefsEditor.putBoolean("connected", false);
                prefsEditor.commit();
                startActivity(intent);
                finish();
            }
        });
    }
}
