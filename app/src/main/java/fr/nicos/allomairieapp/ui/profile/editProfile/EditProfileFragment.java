package fr.nicos.allomairieapp.ui.profile.editProfile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.nicos.allomairieapp.R;
import fr.nicos.allomairieapp.core.validation.FieldValidators;
import fr.nicos.allomairieapp.database.MyAppDatabase;
import fr.nicos.allomairieapp.database.entity.User;
import fr.nicos.allomairieapp.database.singleton.DatabaseSingleton;
import fr.nicos.allomairieapp.databinding.FragmentEditProfileBinding;
import fr.nicos.allomairieapp.ui.profile.editProfile.viewModels.UserViewModel;

public class EditProfileFragment extends Fragment {

    private UserViewModel mViewModel;

    private FragmentEditProfileBinding binding;

    MyAppDatabase myAppDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        /** Instancation de la base de donnée local */
        myAppDatabase = DatabaseSingleton.getInstance(requireContext());

        mViewModel = new UserViewModel();

        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        binding.setUserViewModel(mViewModel);
        binding.executePendingBindings();

        setupListeners();

        getActivity().setTitle("Edit Profile");

        return binding.getRoot();
    }

    private void setupListeners() {
        // Source : https://dev.to/mustufa786/textinputlayout-form-validation-using-data-binding-in-android-8gf
        binding.emailText.addTextChangedListener(new TextFieldValidation(binding.emailText));

        binding.validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User user = new User();
                user.setFirstName("Nicos");
                user.setLastName("Aliagas");

                addUserInBackground(user);

                if(isValidate()) {
                    mViewModel.sendFormData();

                    getUsersInBackground();
                }
            }
        });
    }

    public void addUserInBackground(User user) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myAppDatabase.userDao().addUser(user);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), "Yoyoyo New User !!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void getUsersInBackground() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<User> users = myAppDatabase.userDao().getAll();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        users.forEach(user -> {
                            System.out.println("Loaded User >>> " + user.getFirstName());
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*mUser = new User("Nicos", "Aliagas", "0000000000", "mail@gmail.com");
        binding.setUser(mUser);*/
    }

    private Boolean isValidate() {
        return validateEmail();
    };

    /**
     * 1) field must not be empty
     * 2) text should matches email address format
     */
    private boolean validateEmail() {
        String email = binding.emailText.getText().toString().trim();
        String field = "Email";
        if (email.isEmpty()) {
            binding.emailTextLayout.setError(getString(R.string.validator_field_required, field));
            binding.emailText.requestFocus();
            return false;
        } else if (!FieldValidators.isValidEmail(email)) {
            binding.emailTextLayout.setError(getString(R.string.validator_field_no_valid, field));
            binding.emailText.requestFocus();
            return false;
        } else {
            binding.emailTextLayout.setErrorEnabled(false);
        }
        return true;
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null) {
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public class TextFieldValidation implements TextWatcher {
        private final View view;

        public TextFieldValidation(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // checking ids of each text field and applying functions accordingly.
            if(view.getId() == R.id.emailText) {
                validateEmail();
            }
        }
    }
}