package com.andreaziqing.signlanguagedetectionapp;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.andreaziqing.signlanguagedetectionapp.UserTabs.ChallengeFragment;
import com.andreaziqing.signlanguagedetectionapp.UserTabs.GlossaryFragment;
import com.andreaziqing.signlanguagedetectionapp.UserTabs.HomeFragment;
import com.andreaziqing.signlanguagedetectionapp.UserTabs.PracticeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_ACTIVITY = "MainActivity";

    HomeFragment homeFragment = new HomeFragment();
    PracticeFragment practiceFragment = new PracticeFragment();
    ChallengeFragment challengeFragment = new ChallengeFragment();
    GlossaryFragment glossaryFragment = new GlossaryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(MAIN_ACTIVITY, "Starting Main Activity");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(homeFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.homeFragment) {
                loadFragment(homeFragment);
                return true;
            }
            if (item.getItemId() == R.id.practiceFragment) {
                loadFragment(practiceFragment);
                return true;
            }
            if (item.getItemId() == R.id.challengeFragment) {
                loadFragment(challengeFragment);
                return true;
            }
            if (item.getItemId() == R.id.glossaryFragment) {
                loadFragment(glossaryFragment);
                return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}