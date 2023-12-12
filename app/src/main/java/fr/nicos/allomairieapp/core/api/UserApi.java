package fr.nicos.allomairieapp.core.api;

import fr.nicos.allomairieapp.core.models.LoginUser;
import fr.nicos.allomairieapp.core.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("users/")
    Call<User> postUser(@Body User datas);

    @POST("auth/")
    Call<User> authenticateUser(@Body LoginUser datas);
}
