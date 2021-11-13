package ca.bcit.androidProject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // sets the title to the toolbar
        getSupportActionBar().setTitle("Profile");



    }

    /**
     * This will set the Profile menu item to invisible
     * @param menu menu
     * @return true
     */
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        if (menu.findItem(R.id.userProfileButton) != null) {
            menu.findItem(R.id.userProfileButton).setVisible(false);
        }
        return true;
    }

    /**
     * This will inflate the menu_appbar to the activity.
     * @param menu menu
     * @return menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This will allow user to logout to the app.
     * @param menu menu
     */
    public void onLogoutClick(MenuItem menu) {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        Intent intent = new Intent(this, LandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    /**
     * This will display back button and allow user to go back to previous activity.
     * @return true
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}