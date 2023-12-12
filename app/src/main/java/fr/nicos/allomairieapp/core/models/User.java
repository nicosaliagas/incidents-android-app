package fr.nicos.allomairieapp.core.models;

import android.util.Patterns;

public class User {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;

    public User(String FirstName, String LastName, String EmailAddress, String Password) {
        firstName = FirstName;
        lastName = LastName;
        emailAddress = EmailAddress;
        password = Password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
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
