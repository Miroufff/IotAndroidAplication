package fr.imie.sensair.adapters;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.imie.sensair.activities.DetailUserActivity;
import fr.imie.sensair.model.User;

/**
 * Created by mirouf on 17/05/17.
 */

public class UserAdapter {
    public boolean isUserValid(Context context, User user, String confirmPassword) {
        if (user.getFirstname().equals("")) {
            Toast.makeText(context, "Firstname cannot be empty", Toast.LENGTH_SHORT).show();

            return false;
        } else if (user.getLastname().equals("")) {
            Toast.makeText(context, "Lastname cannot be empty", Toast.LENGTH_SHORT).show();

            return false;
        } else if (user.getUsername().equals("")) {
            Toast.makeText(context, "Username cannot be empty", Toast.LENGTH_SHORT).show();

            return false;
        } else if (!this.isEmailValid(user.getEmail())) {
            Toast.makeText(context, "Email not valid", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (!user.getPassword().equals("")) {
            if (!user.getPassword().matches(confirmPassword)) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();

                return false;
            }
        } else {
            Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    public boolean isEmailValid(String email)
    {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
