package com.andreaziqing.signlanguagedetectionapp.UserTabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.andreaziqing.signlanguagedetectionapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationTabsController extends AppCompatActivity {

    private static final String NAVIGATION_TABS_CONTROLLER = "NavigationTabsController";

    HomeFragment homeFragment = new HomeFragment();
    PracticeFragment practiceFragment = new PracticeFragment();
    ChallengeFragment challengeFragment = new ChallengeFragment();
    GlossaryFragment glossaryFragment = new GlossaryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_tabs_controller);
        Log.i(NAVIGATION_TABS_CONTROLLER, "Starting Navigation Tabs Controller");

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