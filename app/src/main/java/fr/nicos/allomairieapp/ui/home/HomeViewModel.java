package fr.nicos.allomairieapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    public Number count_value = 0;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(count_value.toString());
    }

    public LiveData<String> getText() {
        return mText;
    }
}