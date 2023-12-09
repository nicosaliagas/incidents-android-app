package fr.nicos.allomairieapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import fr.nicos.allomairieapp.databinding.ActivityLoginBinding;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_login);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.loginFragment)
                .build();
        NavigationUI.setupWithNavController(binding.toolbarRegister, navController, appBarConfiguration);
    }
}