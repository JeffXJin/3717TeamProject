package ca.bcit.androidProject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ca.bcit.a3717androidproject.R;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }

    public void onLoginClick(View view) {
        Intent i = new Intent(this, MainPageActivity.class);
        startActivity(i);
    }

    public void onCreateAccountClick(View view) {
        Intent i = new Intent(this, CreateAccountActivity.class);
        startActivity(i);
    }
}