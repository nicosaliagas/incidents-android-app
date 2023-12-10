package fr.nicos.allomairieapp.ui.login;

import android.content.Context;
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

import fr.nicos.allomairieapp.R;
import fr.nicos.allomairieapp.core.models.LoginUser;
import fr.nicos.allomairieapp.databinding.FragmentLoginBinding;

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
                    Toast.makeText(requireContext(), "Login avec succès !", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(requireContext(), "Go to register page !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isFormValid(LoginUser loginUser) {
        boolean isValid = true;
        String emailField = "Email";
        String passwordField = "Mot de passe";

        if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getStrEmailAddress())) {
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

        if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getStrPassword())) {
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
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        String userEmail = sharedPref.getString(String.valueOf(R.string.user_connected_preference_key), null);

        System.out.println("userEmail >>> " + userEmail);

        if(!TextUtils.isEmpty(userEmail)) {

        }
    }
}