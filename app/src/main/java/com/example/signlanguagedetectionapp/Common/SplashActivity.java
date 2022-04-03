package com.example.signlanguagedetectionapp.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.signlanguagedetectionapp.LoginActivity;
import com.example.signlanguagedetectionapp.MainActivity;
import com.example.signlanguagedetectionapp.R;

import java.net.Inet4Address;

public class SplashActivity extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;
    private static int SPLASH_TIMER = 5000;

    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        lottieAnimationView = findViewById(R.id.lottie);
        lottieAnimationView.animate().translationY(1400).setDuration(600).setStartDelay(4000);

        new Handler().postDelayed(()-> {

            // Check if it's the first time the user enter the app
            onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
            boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

            if (isFirstTime) {
                SharedPreferences.Editor editor = onBoardingScreen.edit();
                editor.putBoolean("firstTime", false);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), OnBoarding.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }, SPLASH_TIMER);
    }
}