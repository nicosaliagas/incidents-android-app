package fr.nicos.allomairieapp.core.viewModel;

import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import fr.nicos.allomairieapp.core.models.User;

public class UserViewModel extends ViewModel {
    public boolean skipObservation = false;
    public MutableLiveData<String> FirstName = new MutableLiveData<>();
    public MutableLiveData<String> LastName = new MutableLiveData<>();
    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();

    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<String> errorFormMutableLiveData;

    public MutableLiveData<User> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }

        return userMutableLiveData;
    }

    public void observeUser(Observer<User> observer, LifecycleOwner lifecycleOwner) {
        userMutableLiveData.observe(lifecycleOwner, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (!skipObservation) {
                    observer.onChanged(user);
                }
                skipObservation = false;
            }
        });
    }

    public MutableLiveData<String> getErrorUser() {

        if (errorFormMutableLiveData == null) {
            errorFormMutableLiveData = new MutableLiveData<>();
        }
        return errorFormMutableLiveData;
    }

    public void onClickRegister(View view) {
        skipObservation = false;
        User user = new User(FirstName.getValue(), LastName.getValue(), EmailAddress.getValue(), Password.getValue());

        if(isCheckEmailForm(user) && isCheckPasswordForm(user)) {
            userMutableLiveData.setValue(user);
        }
    }

    public void onClickProfil(View view) {
        skipObservation = false;
        User user = new User(FirstName.getValue(), LastName.getValue(), EmailAddress.getValue(), Password.getValue());

        if(isCheckEmailForm(user)) {
            userMutableLiveData.setValue(user);
        }
    }

    private boolean isCheckEmailForm(User user) {
        String errorMessage = null;

        if(TextUtils.isEmpty(user.getEmailAddress())) {
            // errorMessage = "R.string.validator_field_required";
            errorMessage = "Le champ Email est requis";
        } else if (!user.isEmailValid()) {
            // errorMessage = "R.string.validator_field_no_valid";
            errorMessage = "Le champ Email n'est pas valide";
        }

        if(errorMessage != null) {
            errorFormMutableLiveData.setValue(errorMessage);
            return false;
        }

        return true;
    }

    private boolean isCheckPasswordForm(User user) {
        String errorMessage = null;

        if(TextUtils.isEmpty(user.getPassword())) {
            // errorMessage = "R.string.validator_field_required";
            errorMessage = "Le champ Password est requis";
        } else if (!user.isPasswordLengthGreaterThan5()) {
            // errorMessage = "R.string.validator_field_no_valid";
            errorMessage = "Le Password doit contenir au moins 6 caractères";
        }

        if(errorMessage != null) {
            errorFormMutableLiveData.setValue(errorMessage);
            return false;
        }

        return true;
    }
}