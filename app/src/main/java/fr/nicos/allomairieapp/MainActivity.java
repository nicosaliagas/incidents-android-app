package fr.nicos.allomairieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import fr.nicos.allomairieapp.core.api.NetworkHandler;
import fr.nicos.allomairieapp.core.api.UserApi;
import fr.nicos.allomairieapp.core.models.User;
import fr.nicos.allomairieapp.core.sharedpreference.UserSharedPreferenceManager;
import fr.nicos.allomairieapp.core.viewModel.UserViewModel;
import fr.nicos.allomairieapp.database.MyAppDatabase;
import fr.nicos.allomairieapp.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private UserViewModel userViewModel;

    private UserSharedPreferenceManager userSharedPreferenceManager;

    private Toolbar toolbar;

    MyAppDatabase myAppDatabase;

    NavController navController;

    // Menu de navigation
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider viewModelProvider = new ViewModelProvider(this);

        userSharedPreferenceManager = UserSharedPreferenceManager.getInstance(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        toolbar = binding.appBarMain.toolbar;

        userViewModel = viewModelProvider.get(UserViewModel.class);

        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        redirectToWelcomePageIfNotAuthenticated();

        setupListeners();

        /** on mets à jour les valeurs de la vue */
        setInfosUserView();

        /** en écoute sur les changements du view model de l'objet utilisateur */
        observeViewModelUser();

        /** on récupère l'objet Utilisateur en bdd si on est connecté */
        getUserDatasFromBack();

        drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    /**
     * en écoute sur les changements du view model de l'objet utilisateur
     * pour mettre à jour les valeurs dans le sharedPreference
     */
    private void observeViewModelUser() {
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                setSharedPreference(user);

                setInfosUserView();
            }
        });
    }

    private void setSharedPreference(User user) {
        userSharedPreferenceManager.setUser(user);
    }

    private void redirectToWelcomePageIfNotAuthenticated() {
        if (!isUserAuthenticated()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private boolean isUserAuthenticated() {
        return userSharedPreferenceManager.getEmailAddress() != null && userSharedPreferenceManager.getLastName() != null && userSharedPreferenceManager.getLastName() != null;
    }

    private void setInfosUserView() {
        TextView headerFirstNameUser = binding.navView.getHeaderView(0).findViewById(R.id.header_firstNameUser);
        headerFirstNameUser.setText(userSharedPreferenceManager.getFirstName());

        TextView headerEmailAddressUser = binding.navView.getHeaderView(0).findViewById(R.id.header_emailAddressUser);
        headerEmailAddressUser.setText(userSharedPreferenceManager.getEmailAddress());
    }

    /** on récupère l'objet Utilisateur en bdd si on est connecté
     * et on stocke les variables dans le shared preference
     * */
    private void getUserDatasFromBack() {
        UserApi userApi = NetworkHandler.getRetrofit().create(UserApi.class);

        Call<User> callApi = userApi.getUser(userSharedPreferenceManager.getUserId());

        callApi.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    userSharedPreferenceManager.setUser(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setupListeners() {
        // View headerview = binding.navView.getHeaderView(0);
        // headerview.findViewById(R.id.profileImageView).setOnClickListener

        /** Header du menu navigation, Zone avatar profil */
        binding.navView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                navController.navigateUp();
                navController.navigate(R.id.nav_profile);
            }
        });

        /** Icon plus */
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Start a new Activity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setActionBarTitle(String title){
        toolbar.setTitle(title);
    }
}