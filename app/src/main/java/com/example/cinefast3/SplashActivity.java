package com.example.cinefast3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String PREF_NAME = "cinefast_session_pref_v3";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.imgLogo);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.logo_circle);
        logo.startAnimation(anim);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false);

            if (isLoggedIn) {
                // If logged in, skip onboarding and login, go straight to Main
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // Otherwise, show onboarding
                startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
            }
            finish();
        }, 3000); // Reduced delay slightly for better UX
    }
}
