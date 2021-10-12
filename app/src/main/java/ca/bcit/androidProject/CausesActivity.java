package ca.bcit.androidProject;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;


import androidx.appcompat.widget.Toolbar;

import ca.bcit.a3717androidproject.R;

public class CausesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_causes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}