package fr.nicos.allomairieapp.ui.profile.editProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import fr.nicos.allomairieapp.MainActivity;
import fr.nicos.allomairieapp.R;
import fr.nicos.allomairieapp.core.api.NetworkHandler;
import fr.nicos.allomairieapp.core.api.UserApi;
import fr.nicos.allomairieapp.core.models.User;
import fr.nicos.allomairieapp.core.sharedpreference.UserSharedPreferenceManager;
import fr.nicos.allomairieapp.core.viewModel.UserViewModel;
import fr.nicos.allomairieapp.databinding.FragmentEditProfileBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

    private UserViewModel userViewModel;
    private UserSharedPreferenceManager userSharedPreferenceManager;

    private FragmentEditProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());

        userSharedPreferenceManager = UserSharedPreferenceManager.getInstance(getActivity());

        ((MainActivity) getActivity()).setActionBarTitle("Modification de mes infos");

        userViewModel = viewModelProvider.get(UserViewModel.class);

        setValuesViewModel();

        binding = FragmentEditProfileBinding.inflate(inflater, container, false);

        binding.setLifecycleOwner(this);

        binding.setUserViewModel(userViewModel);

        observeViewModelUser();

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setValuesViewModel() {
        userViewModel.skipObservation = true;
        userViewModel.FirstName.setValue(userSharedPreferenceManager.getFirstName());
        userViewModel.LastName.setValue(userSharedPreferenceManager.getLastName());
        userViewModel.EmailAddress.setValue(userSharedPreferenceManager.getEmailAddress());
    }

    private void observeViewModelUser() {
        userViewModel.getErrorUser().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                setErrorToForm(error);
            }
        });

        userViewModel.observeUser(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // editUser(user);
                binding.txtEmailAddress.setErrorEnabled(false);

                Toast.makeText(requireContext(), "Vos données sont correctement à jours !", Toast.LENGTH_SHORT).show();
            }
        }, getViewLifecycleOwner());
    }

    private void setErrorToForm(String error) {
        // binding.txtEmailAddress.setError(getString(error, "Email"));
        binding.txtEmailAddress.setError(error);
        binding.txtEmailAddress.requestFocus();
    }

    private void editUser(User user) {
        UserApi userApi = NetworkHandler.getRetrofit().create(UserApi.class);

        Call<User> callApi = userApi.postUser(user);

        callApi.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User userRegistred = response.body();

                    Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_loginFragment);

                    String transactionName = "pageRegisterToLogin";
                    getParentFragmentManager().beginTransaction()
                            .addToBackStack(transactionName)
                            .commit();

                    Toast.makeText(requireContext(), "Compte créé avec succès !", Toast.LENGTH_SHORT).show();
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