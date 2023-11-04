package fr.nicos.allomairieapp.ui.profile.editProfile.viewModels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.List;

import fr.nicos.allomairieapp.BR;
import fr.nicos.allomairieapp.core.api.NetworkHandler;
import fr.nicos.allomairieapp.ui.profile.editProfile.api.UserAPI;
import fr.nicos.allomairieapp.ui.profile.editProfile.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends BaseObservable {
    private User user;
    private String successMessage = "Vos informations ont correctement été mise à jour";
    private String errorMessage = "Erreur dans la mise à jour de vos informations";

    public UserViewModel() {
        user = new User("", "", "", "");
    }

    @Bindable
    private String toastMessage = null;


    public String getToastMessage() {
        return toastMessage;
    }

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    public void setUserEmail(String email) {
        user.setEmail(email);
        notifyPropertyChanged(BR.userEmail);
    }

    @Bindable
    public String getUserEmail() {
        return user.getEmail();
    }

    public void setUserFirstName(String firstName) {
        user.setFirstName(firstName);
        notifyPropertyChanged(BR.userFirstName);
    }

    @Bindable
    public String getUserFirstName() {
        return user.getFirstName();
    }

    public void setUserLastName(String lastName) {
        user.setLastName(lastName);
        notifyPropertyChanged(BR.userLastName);
    }

    @Bindable
    public String getUserLastName() {
        return user.getLastName();
    }

    public void setUserPhone(String phone) {
        user.setPhone(phone);
        notifyPropertyChanged(BR.userPhone);
    }

    @Bindable
    public String getUserPhone() {
        return user.getPhone();
    }

    public void sendFormData(Boolean isValid) {
        System.out.println("Datas >> " + this.getUserEmail());

        // https://stackoverflow.com/a/42155739

        UserAPI userAPI = NetworkHandler.getRetrofit().create(UserAPI.class);

        Call<List<User>> call = userAPI.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()) {
                    List<User> changesList = response.body();

                    changesList.forEach(change -> System.out.println("Prénom >>> "+change.getFirstName()));

                    setToastMessage(successMessage);
                } else {
                    System.out.println(response.errorBody());

                    setToastMessage(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();

                setToastMessage(errorMessage);
            }
        });
    }
}