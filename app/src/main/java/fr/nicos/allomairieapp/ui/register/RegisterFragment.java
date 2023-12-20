package fr.nicos.allomairieapp.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import fr.nicos.allomairieapp.LoginActivity;
import fr.nicos.allomairieapp.R;
import fr.nicos.allomairieapp.core.api.NetworkHandler;
import fr.nicos.allomairieapp.core.api.UserApi;
import fr.nicos.allomairieapp.core.models.User;
import fr.nicos.allomairieapp.core.viewModel.UserViewModel;
import fr.nicos.allomairieapp.databinding.FragmentRegisterBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());

        ((LoginActivity) getActivity()).setActionBarTitle("Nouveau compte");

        userViewModel = viewModelProvider.get(UserViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.setLifecycleOwner(this);

        binding.setUserViewModel(userViewModel);

        observeViewModelUser();

        return binding.getRoot();
    }

    private void observeViewModelUser() {
        userViewModel.getErrorUser().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                setErrorToForm(error);
            }
        });

        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                postUser(user);

                binding.txtEmailAddress.setErrorEnabled(false);
                binding.txtPassword.setErrorEnabled(false);
            }
        });
    }

    private void setErrorToForm(String error) {
        binding.txtEmailAddress.setError(error);
        binding.txtEmailAddress.requestFocus();

        binding.txtPassword.setError(error);
        binding.txtPassword.requestFocus();
    }

    /*private boolean isFormValid(User user) {
        boolean isValid = true;
        String emailField = "Email";
        String passwordField = "Mot de passe";

        if (TextUtils.isEmpty(Objects.requireNonNull(user).getEmailAddress())) {
            binding.txtEmailAddress.setError(getString(R.string.validator_field_required, emailField));
            binding.txtEmailAddress.requestFocus();

            isValid = false;
        }
        else if (!user.isEmailValid()) {
            binding.txtEmailAddress.setError(getString(R.string.validator_field_no_valid, emailField));
            binding.txtEmailAddress.requestFocus();

            isValid = false;
        } else {
            binding.txtEmailAddress.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(user).getPassword())) {
            binding.txtPassword.setError(getString(R.string.validator_field_required, passwordField));
            binding.txtPassword.requestFocus();

            isValid = false;
        }
        else if (!user.isPasswordLengthGreaterThan5()) {
            binding.txtPassword.setError("Le " + passwordField + " doit contenir au moins 6 caractères");
            binding.txtPassword.requestFocus();

            isValid = false;
        }
        else {
            binding.txtPassword.setErrorEnabled(false);
        }

        return isValid;
    }*/

    private void postUser(User user) {
        UserApi userApi = NetworkHandler.getRetrofit().create(UserApi.class);

        Call<User> callApi = userApi.postUser(user);

        callApi.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_loginFragment);

                    String transactionName = "pageRegisterToLogin";
                    getParentFragmentManager().beginTransaction()
                            .addToBackStack(transactionName)
                            .commit();

                    Toast.makeText(requireContext(), "Votre compte a été créé avec succès !", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}