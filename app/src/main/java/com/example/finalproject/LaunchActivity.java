package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {
        FirebaseAuth auth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        auth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
                public void run() {
                if (auth.getCurrentUser() != null) {
                    startActivity(new Intent(LaunchActivity.this, AuthenticationActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(LaunchActivity.this, SliderActivity.class));
                    finish();
                }
                    }
        },3000);
    }
}
