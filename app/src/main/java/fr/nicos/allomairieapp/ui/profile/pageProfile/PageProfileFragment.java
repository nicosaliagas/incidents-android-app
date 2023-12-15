package fr.nicos.allomairieapp.ui.profile.pageProfile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.nicos.allomairieapp.LoginActivity;
import fr.nicos.allomairieapp.R;
import fr.nicos.allomairieapp.core.sharedpreference.LoginSharedPreferenceManager;
import fr.nicos.allomairieapp.database.MyAppDatabase;
import fr.nicos.allomairieapp.database.entity.User;
import fr.nicos.allomairieapp.database.singleton.DatabaseSingleton;
import fr.nicos.allomairieapp.databinding.FragmentPageProfileBinding;

public class PageProfileFragment extends Fragment {

    private MyAppDatabase myAppDatabase;

    private FragmentPageProfileBinding binding;

    private User userProfil;

    private LoginSharedPreferenceManager loginSharedPreferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /** Instancation de la base de donnée local */
        myAppDatabase = DatabaseSingleton.getInstance(requireContext());

        binding = FragmentPageProfileBinding.inflate(inflater, container, false);
        // binding.executePendingBindings();

        loginSharedPreferenceManager = LoginSharedPreferenceManager.getInstance(getActivity());

        getSharedPreference();

        setupListeners();

        initInfosUserAuthenticated();

        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_page_profile, container, false);
        return binding.getRoot();
    }

    private void initInfosUserAuthenticated() {
        TextView profilIdentity = binding.profilIdentity;
        profilIdentity.setText(loginSharedPreferenceManager.getLastName() + " " + loginSharedPreferenceManager.getFirstName());
    }

    private void getSharedPreference() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        String userEmail = sharedPref.getString(String.valueOf(R.string.user_connected_preference_key), null);

        System.out.println("userEmail >>> " + userEmail);

        if(!TextUtils.isEmpty(userEmail)) {
            getUserConnectedInBackground(userEmail);
        }
    }

    private void setupListeners() {
        // Source : https://dev.to/mustufa786/textinputlayout-form-validation-using-data-binding-in-android-8gf
        binding.actionLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();

                redirectToLoginActivity();
            }
        });

        binding.editProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Dans le fragment actuel
                Navigation.findNavController(getView()).navigate(R.id.action_nav_profile_to_nav_edit_profile);

                String transactionName = "pageProfileToEditProfile";
                getParentFragmentManager().beginTransaction()
                        .addToBackStack(transactionName)
                        .commit();

                /*NavDirections action = fr.nicos.allomairieapp.ui.home.HomeFragmentDirections.actionNavHomeToNavFragmentSecond(currentCount);

                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(action);*/


                /*Fragment editProfileFragment = new EditProfileFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_page_profile, editProfileFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();*/
            }
        });
    }

    private void logout() {
        loginSharedPreferenceManager.setUserId(0);
        loginSharedPreferenceManager.setLastName(null);
        loginSharedPreferenceManager.setFirstName(null);
        loginSharedPreferenceManager.setEmailAddress(null);
    }

    private void redirectToLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void getUserConnectedInBackground(String userEmail) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                User user = myAppDatabase.userDao().getUserByEmail(userEmail);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Loaded User >>> " + user.getFirstName());
                    }
                });
            }
        });
    }

    private void resetProfileInBackground() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myAppDatabase.userDao().nukeUserTable();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), "Toutes les informations ont été effacées avec succès !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}