package ca.bcit.androidProject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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