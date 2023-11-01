package fr.nicos.allomairieapp.ui.core.validation;

import android.util.Patterns;

public class FieldValidators {

    public static Boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
