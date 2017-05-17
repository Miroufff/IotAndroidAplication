package fr.imie.sensair.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fr.imie.sensair.R;
import fr.imie.sensair.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.editTextUsername);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        confirm_password = (EditText) findViewById(R.id.editTextConfirmPassword);
        register = (Button) findViewById(R.id.buttonRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (confirm_password.getText().toString().matches(password.getText().toString())) {
                    User currentUser = new User();
                    currentUser
                        .setUsername(username.getText().toString())
                        .setPassword(password.getText().toString())
                        .setEmail(email.getText().toString())
                        .setFirstname("Toto")
                        .setLastname("Truc");

                    startActivity(new Intent(RegisterActivity.this, SensorActivity.class));
                }
            }
        });
    }
}
