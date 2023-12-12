package fr.nicos.allomairieapp.core.models;

import android.util.Patterns;

public class LoginUser {

    private String emailAddress;
    private String password;

    public LoginUser(String EmailAddress, String Password) {
        emailAddress = EmailAddress;
        password = Password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getEmailAddress()).matches();
    }


    public boolean isPasswordLengthGreaterThan5() {
        return getPassword().length() > 5;
    }

}
