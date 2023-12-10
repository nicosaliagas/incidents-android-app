package fr.nicos.allomairieapp.ui.register;

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

import java.util.Objects;

import fr.nicos.allomairieapp.LoginActivity;
import fr.nicos.allomairieapp.R;
import fr.nicos.allomairieapp.core.models.RegisterUser;
import fr.nicos.allomairieapp.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private RegisterViewModel registerViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);

        ((LoginActivity) getActivity()).setActionBarTitle("Nouveau compte");

        registerViewModel = viewModelProvider.get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.setLifecycleOwner(this);

        binding.setRegisterViewModel(registerViewModel);

        this.observeViewModelUser();

        this.setupListeners();

        return binding.getRoot();
    }

    private void observeViewModelUser() {
        registerViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<RegisterUser>() {
            @Override
            public void onChanged(@Nullable RegisterUser registerUser) {
                if(isFormValid(registerUser)) {
                    Toast.makeText(requireContext(), "Register user avec succès !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isFormValid(RegisterUser registerUser) {
        boolean isValid = true;
        String emailField = "Email";
        String passwordField = "Mot de passe";

        if (TextUtils.isEmpty(Objects.requireNonNull(registerUser).getStrEmailAddress())) {
            binding.txtEmailAddress.setError(getString(R.string.validator_field_required, emailField));
            binding.txtEmailAddress.requestFocus();

            isValid = false;
        }
        else if (!registerUser.isEmailValid()) {
            binding.txtEmailAddress.setError(getString(R.string.validator_field_no_valid, emailField));
            binding.txtEmailAddress.requestFocus();

            isValid = false;
        } else {
            binding.txtEmailAddress.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(registerUser).getStrPassword())) {
            binding.txtPassword.setError(getString(R.string.validator_field_required, passwordField));
            binding.txtPassword.requestFocus();

            isValid = false;
        }
        else if (!registerUser.isPasswordLengthGreaterThan5()) {
            binding.txtPassword.setError("Le " + passwordField + " doit contenir au moins 6 caractères");
            binding.txtPassword.requestFocus();

            isValid = false;
        }
        else {
            binding.txtPassword.setErrorEnabled(false);
        }

        return isValid;
    }

    private void setupListeners() {

    }
}