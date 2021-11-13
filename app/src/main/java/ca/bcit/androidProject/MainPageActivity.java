package ca.bcit.androidProject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainPageActivity extends AppCompatActivity {

    TextView tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onLogoutClick(MenuItem menu) {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        Intent intent = new Intent(this, LandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onUserProfileClick(MenuItem menu) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent;
        if (currentUser != null) {
            intent = new Intent(this, ProfileActivity.class);
        } else {
            intent = new Intent(this, LandingActivity.class);
        }
        startActivity(intent);
    }

    public void onSeaLevelClick(View view) {
        Intent i = new Intent(this, SeaLevelHistoryActivity.class);
        startActivity(i);
    }

    public void onInformationClick(View view) {
        Intent i = new Intent(this, InformationActivity.class);
        startActivity(i);
    }

    public void onGlobalSeaLevelClick(View view) {
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    public void onViewStatesClick(View view) {
        Intent i = new Intent(this, StateActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}