package fr.nicos.allomairieapp.core.models;

import android.util.Patterns;

public class RegisterUser {

    private String strFirstName;
    private String strLastName;
    private String strEmailAddress;
    private String strPassword;

    public RegisterUser(String FirstName, String LastName, String EmailAddress, String Password) {
        strFirstName = FirstName;
        strLastName = LastName;
        strEmailAddress = EmailAddress;
        strPassword = Password;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public String getStrEmailAddress() {
        return strEmailAddress;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getStrEmailAddress()).matches();
    }


    public boolean isPasswordLengthGreaterThan5() {
        return getStrPassword().length() > 5;
    }

}
