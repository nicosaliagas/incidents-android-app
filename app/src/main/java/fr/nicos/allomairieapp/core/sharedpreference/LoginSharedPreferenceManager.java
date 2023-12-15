package fr.nicos.allomairieapp.core.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import fr.nicos.allomairieapp.R;

public class LoginSharedPreferenceManager {

    private static LoginSharedPreferenceManager instance;
    private SharedPreferences sharedPreferences;

    private int id;
    private String firstName;
    private String lastName;
    private String emailAddress;

    private LoginSharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.user_connected_preference_key), Context.MODE_PRIVATE);
    }

    public static synchronized LoginSharedPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new LoginSharedPreferenceManager(context);
        }
        return instance;
    }

    public int getUserId() {
        if (id == 0) {
            id = sharedPreferences.getInt("id", 0);
        }
        return id;
    }

    public void setUserId(int id) {
        sharedPreferences.edit().putInt("id", id).apply();

        id = id;
    }

    public String getFirstName() {
        if (firstName == null) {
            firstName = sharedPreferences.getString("firstName", null);
        }
        return firstName;
    }

    public void setFirstName(String value1) {
        sharedPreferences.edit().putString("firstName", value1).apply();

        firstName = value1;
    }

    public String getLastName() {
        if (lastName == null) {
            lastName = sharedPreferences.getString("lastName", null);
        }
        return lastName;
    }

    public void setLastName(String value1) {
        sharedPreferences.edit().putString("lastName", value1).apply();

        lastName = value1;
    }

    public String getEmailAddress() {
        if (emailAddress == null) {
            emailAddress = sharedPreferences.getString("emailAddress", null);
        }
        return emailAddress;
    }

    public void setEmailAddress(String value1) {
        sharedPreferences.edit().putString("emailAddress", value1).apply();

        emailAddress = value1;
    }
}

