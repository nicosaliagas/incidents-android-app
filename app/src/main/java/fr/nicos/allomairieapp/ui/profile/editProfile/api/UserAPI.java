package fr.nicos.allomairieapp.ui.profile.editProfile.api;

import java.util.List;

import fr.nicos.allomairieapp.ui.profile.editProfile.model.User;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserAPI {
    @GET("users/")
    Call<List<User>> getUsers();
}
