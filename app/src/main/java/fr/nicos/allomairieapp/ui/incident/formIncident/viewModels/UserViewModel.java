package fr.nicos.allomairieapp.ui.incident.formIncident.viewModels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import fr.nicos.allomairieapp.BR;
import fr.nicos.allomairieapp.ui.incident.formIncident.model.User;

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

        if(isValid) {
            setToastMessage(successMessage);
        } else {
            setToastMessage(errorMessage);
        }
    }
}