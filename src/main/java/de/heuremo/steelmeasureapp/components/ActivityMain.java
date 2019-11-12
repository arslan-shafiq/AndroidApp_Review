// Reviewed by M. Arslan
package de.heuremo.steelmeasureapp.components;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import de.heuremo.R;
import de.heuremo.commons.common.constant.SharedPreferencesConstant;
import de.heuremo.commons.common.helpers.SharedPreferenceHelper;
import de.heuremo.steelmeasureapp.components.measureactivity.ArMeasureActivity;

public class ActivityMain extends AppCompatActivity {

    private static final int REQUEST_PHONE_CALL = 1;
    Toolbar toolbar; // Reviewer: no need to declare as class variable
    private DrawerLayout drawer;
    private TextView customerNumberTextView, customerEmailTextView; // Reviewer: no need to declare as class variable
    private MenuItem supportNumber; // Reviewer: no need to declare as class variable
    private String user_email = ""; // Reviewer: No need to initialize this variable
    private String user_customer_number = ""; // Reviewer: No need to initialize this variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Reviewer: put logically related code gather
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        supportNumber = findViewById(R.id.nav_support_number);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Reviewer: Let's use line break (readability issue)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        NavigationUI.setupWithNavController(navigationView, navController);

        View header = navigationView.getHeaderView(0);
        customerNumberTextView = (TextView) header.findViewById(R.id.customer_number_side_menu_text);
        customerEmailTextView = (TextView) header.findViewById(R.id.email_text_side_menu);
        user_email = SharedPreferenceHelper.getUserEmail(this);
        user_customer_number = SharedPreferenceHelper.getCustomerNumber(this);
        customerEmailTextView.setText(user_email);
        customerNumberTextView.setText(user_customer_number);
        // Extra spaces

    }

    public void openARMeasureActivity(MenuItem item) {
        Intent intent = new Intent(this, ArMeasureActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //if drawer is opened close it first
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void callSupportNumber(MenuItem item) {
        String phone_no = item.getTitle().toString();
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone_no));

        if (ContextCompat.checkSelfPermission(ActivityMain.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityMain.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
            startActivity(callIntent);
        } else {
            startActivity(callIntent);
        }
    }

    public void emailSupportMail(MenuItem item) {
        String email = item.getTitle().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:" + email + "?cc=" + user_email);
        intent.setData(data);
        startActivity(intent);
    }

}
