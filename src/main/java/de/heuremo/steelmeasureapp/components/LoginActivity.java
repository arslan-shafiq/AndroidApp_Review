package de.heuremo.steelmeasureapp.components;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import de.heuremo.R;
import de.heuremo.commons.common.constant.SharedPreferencesConstant;
import de.heuremo.commons.common.helpers.SharedPreferenceHelper;

public class LoginActivity extends FragmentActivity {

    private boolean IsUserRegister, IsUserLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        IsUserRegister = SharedPreferenceHelper.getUserRegisteredKey(this);
        IsUserLogIn = SharedPreferenceHelper.getUserLoggedInKey(this);

        if (savedInstanceState == null) {
            if (IsUserRegister) {
                navController.navigate(R.id.loginFragment);
                if (IsUserLogIn) {
                    Intent intent = new Intent(this, ActivityMain.class);
                    startActivity(intent);
                } else {
                    navController.navigate(R.id.loginFragment);
                }
            } else {
                navController.navigate(R.id.registrationFragment);
            }
        }
    }
}
