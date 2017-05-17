package fr.imie.sensair.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

                if (!isEmailValid(email.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_not_valid_email), Toast.LENGTH_LONG).show();
                }

                if (confirm_password.getText().toString().matches(password.getText().toString())) {
                    User currentUser = new User();
                    currentUser
                        .setUsername(username.getText().toString())
                        .setPassword(password.getText().toString())
                        .setEmail(email.getText().toString())
                        .setFirstname("Toto")
                        .setLastname("Truc");

                    startActivity(new Intent(RegisterActivity.this, SensorActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_password_not_match), Toast.LENGTH_LONG).show();
                }
            }
        });
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

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
}

