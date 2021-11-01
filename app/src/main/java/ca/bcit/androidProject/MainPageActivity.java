package ca.bcit.androidProject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;


public class MainPageActivity extends AppCompatActivity {

    TextView tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvLogout = findViewById(R.id.tvLogout);

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut(); // logout
                startActivity(new Intent(getApplicationContext(), LandingActivity.class));
                finish();
            }
        });

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

    public void onProfileClick(View view) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }
}