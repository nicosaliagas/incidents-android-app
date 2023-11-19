package fr.nicos.allomairieapp.ui.profile.pageProfile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.nicos.allomairieapp.R;
import fr.nicos.allomairieapp.database.MyAppDatabase;
import fr.nicos.allomairieapp.database.entity.User;
import fr.nicos.allomairieapp.database.singleton.DatabaseSingleton;
import fr.nicos.allomairieapp.databinding.FragmentPageProfileBinding;

public class PageProfileFragment extends Fragment {

    private MyAppDatabase myAppDatabase;

    private FragmentPageProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /** Instancation de la base de donnée local */
        myAppDatabase = DatabaseSingleton.getInstance(requireContext());

        binding = FragmentPageProfileBinding.inflate(inflater, container, false);
        // binding.executePendingBindings();

        getUsersInBackground();

        setupListeners();

        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_page_profile, container, false);
        return binding.getRoot();
    }

    private void setupListeners() {
        // Source : https://dev.to/mustufa786/textinputlayout-form-validation-using-data-binding-in-android-8gf
        binding.resetProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetProfileInBackground();
            }
        });

        binding.editProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Dans le fragment actuel
                Navigation.findNavController(view).navigate(R.id.action_pageProfileFragment_to_formIncidentFragment);

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

    private void getUsersInBackground() {

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