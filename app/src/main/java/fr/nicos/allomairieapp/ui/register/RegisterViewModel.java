package fr.nicos.allomairieapp.ui.register;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import fr.nicos.allomairieapp.core.models.User;

public class RegisterViewModel extends ViewModel {

    public MutableLiveData<String> FirstName = new MutableLiveData<>();
    public MutableLiveData<String> LastName = new MutableLiveData<>();
    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();

    private MutableLiveData<User> userMutableLiveData;

    public MutableLiveData<User> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }

    public void onClick(View view) {
        User user = new User(FirstName.getValue(), LastName.getValue(), EmailAddress.getValue(), Password.getValue());

        userMutableLiveData.setValue(user);
    }
}