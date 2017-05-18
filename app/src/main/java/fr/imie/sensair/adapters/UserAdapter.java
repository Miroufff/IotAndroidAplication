package fr.imie.sensair.adapters;

import android.content.Context;
import android.widget.Toast;

import fr.imie.sensair.activities.DetailUserActivity;
import fr.imie.sensair.lib.Utils;
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
        } else if (!Utils.isEmailValid(user.getEmail())) {
            Toast.makeText(context, "Email not valid", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (user.getPassword().length() > 0) {
            if (!user.getPassword().matches(confirmPassword)) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();

                return false;
            }
        }

        return true;
    }
}
