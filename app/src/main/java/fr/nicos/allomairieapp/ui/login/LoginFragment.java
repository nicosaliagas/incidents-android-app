package fr.nicos.allomairieapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Objects;

import fr.nicos.allomairieapp.MainActivity;
import fr.nicos.allomairieapp.R;
import fr.nicos.allomairieapp.core.api.NetworkHandler;
import fr.nicos.allomairieapp.core.api.UserApi;
import fr.nicos.allomairieapp.core.models.LoginUser;
import fr.nicos.allomairieapp.core.models.User;
import fr.nicos.allomairieapp.databinding.FragmentLoginBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private LoginViewModel loginViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);

        loginViewModel = viewModelProvider.get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.setLifecycleOwner(this);

        binding.setLoginViewModel(loginViewModel);

        this.observeViewModelUser();

        this.setupListeners();

        getSharedPreference();

        return binding.getRoot();
    }

    private void observeViewModelUser() {
        loginViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<LoginUser>() {
            @Override
            public void onChanged(@Nullable LoginUser loginUser) {
                if(isFormValid(loginUser)) {
                    authUser(loginUser);
                }
            }
        });
    }

    private void setupListeners() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_registerFragment);

                String transactionName = "pageLoginToRegister";
                getParentFragmentManager().beginTransaction()
                        .addToBackStack(transactionName)
                        .commit();
            }
        });
    }

    private boolean isFormValid(LoginUser loginUser) {
        boolean isValid = true;
        String emailField = "Email";
        String passwordField = "Mot de passe";

        if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getEmailAddress())) {
            binding.txtEmailAddress.setError(getString(R.string.validator_field_required, emailField));
            binding.txtEmailAddress.requestFocus();

            isValid = false;
        }
        else if (!loginUser.isEmailValid()) {
            binding.txtEmailAddress.setError(getString(R.string.validator_field_no_valid, emailField));
            binding.txtEmailAddress.requestFocus();

            isValid = false;
        } else {
            binding.txtEmailAddress.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getPassword())) {
            binding.txtPassword.setError(getString(R.string.validator_field_required, passwordField));
            binding.txtPassword.requestFocus();

            isValid = false;
        }
        else if (!loginUser.isPasswordLengthGreaterThan5()) {
            binding.txtPassword.setError("Le " + passwordField + " doit contenir au moins 6 caractères");
            binding.txtPassword.requestFocus();

            isValid = false;
        }
        else {
            binding.txtPassword.setErrorEnabled(false);
        }

        return isValid;
    }

    private void getSharedPreference() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(String.valueOf(R.string.user_connected_preference_key), Context.MODE_PRIVATE);

        String firstName = sharedPreferences.getString("firstName", null);
        String lastName = sharedPreferences.getString("lastName", null);
        String emailAddress = sharedPreferences.getString("emailAddress", null);

        System.out.println("getSharedPreference >> " + emailAddress);
    }

    private void setSharedPreference(User user) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(String.valueOf(R.string.user_connected_preference_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstName", user.getFirstName() );
        editor.putString("lastName", user.getLastName());
        editor.putString("emailAddress", user.getEmailAddress());
        editor.commit();
    }

    private void authUser(LoginUser loginUser) {
        UserApi userApi = NetworkHandler.getRetrofit().create(UserApi.class);

        Call<User> callApi = userApi.authenticateUser(loginUser);

        callApi.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User userAuthenticated = response.body();

                    setSharedPreference(userAuthenticated);

                    redirectToMainActivity();

                    Toast.makeText(requireContext(), "Connecté avec succès !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to connect !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}