package ca.bcit.androidProject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ca.bcit.androidProject.R;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public void onCausesClick(View view) {
        Intent i = new Intent(this, ca.bcit.androidProject.CausesActivity.class);
        startActivity(i);
    }

    public void onPreventionClick(View view) {
        Intent i = new Intent(this, ca.bcit.androidProject.PreventionActivity.class);
        startActivity(i);
    }

    public void onContributeClick(View view) {
        Intent i = new Intent(this, ca.bcit.androidProject.ContributionActivity.class);
        startActivity(i);
    }
}