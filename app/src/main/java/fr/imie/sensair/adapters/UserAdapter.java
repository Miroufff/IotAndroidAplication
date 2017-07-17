package fr.imie.sensair.adapters;

import android.content.Context;
import android.widget.Toast;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import fr.imie.sensair.activities.DetailUserActivity;
import fr.imie.sensair.lib.Utils;
import fr.imie.sensair.model.User;

/**
 * Created by mirouf on 17/05/17.
 */

public class UserAdapter {
    public boolean isUserValid(Context context, User user, String password, String confirmPassword, Boolean isUpdateAction) {
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

        if (!isUpdateAction) {
            if (password.length() > 4) {
                if (!Objects.equals(password, confirmPassword)) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();

                    return false;
                }
            } else {
                Toast.makeText(context, "Passwords length is too short", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (password.length() > 0) {
                if (!Objects.equals(password, confirmPassword)) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();

                    return false;
                }
            }
        }

        return true;
    }
}
