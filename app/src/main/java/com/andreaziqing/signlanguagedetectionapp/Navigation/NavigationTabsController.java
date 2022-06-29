package com.andreaziqing.signlanguagedetectionapp.Navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.andreaziqing.signlanguagedetectionapp.Tabs.AboutFragment;
import com.andreaziqing.signlanguagedetectionapp.Tabs.SettingsFragment;
import com.andreaziqing.signlanguagedetectionapp.R;
import com.andreaziqing.signlanguagedetectionapp.Tabs.GlossaryFragment;
import com.andreaziqing.signlanguagedetectionapp.Tabs.HomeFragment;
import com.andreaziqing.signlanguagedetectionapp.Tabs.PracticeFragment;
import com.andreaziqing.signlanguagedetectionapp.Tabs.RankingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class NavigationTabsController extends AppCompatActivity {

    private static final String NAVIGATION_TABS_CONTROLLER = "NavigationTabsController";

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    HomeFragment homeFragment = new HomeFragment();
    PracticeFragment practiceFragment = new PracticeFragment();
    RankingFragment rankingFragment = new RankingFragment();
    GlossaryFragment glossaryFragment = new GlossaryFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    AboutFragment aboutFragment = new AboutFragment();

    String nextFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_tabs_controller);
        Log.i(NAVIGATION_TABS_CONTROLLER, "Starting Navigation Tabs Controller");

        setToolbar();
        initViews(savedInstanceState);
        initComponentsNavHeader();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(0);
    }

    private void initViews(Bundle savedInstanceState) {
        /**
         * Menu Bottom Navigation
         */

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setNextFragment();

        /**
         * Menu Navigation Drawer
         */

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_48px);
        toggle.syncState();
    }

    private void initComponentsNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.settings:
                        toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        loadFragment(settingsFragment);
                        break;
                    case R.id.about:
                        toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        loadFragment(aboutFragment);
                        break;
                    default:
                        break;
                }

                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void setNextFragment() {
        try {
            Bundle bundle = getIntent().getExtras();
            nextFragment = bundle.getString("nextFragment");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(NAVIGATION_TABS_CONTROLLER, "Next fragment: " + nextFragment);

        if (nextFragment == null) {
            loadFragment(homeFragment);
        } else {
            switch (nextFragment) {
                case "PracticeFragment":
                    loadFragment(practiceFragment);
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    break;
                case "RankingFragment":
                    loadFragment(rankingFragment);
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    break;
                case "GlossaryFragment":
                    loadFragment(glossaryFragment);
                    bottomNavigationView.getMenu().getItem(3).setChecked(true);
                    break;
                default:
                    loadFragment(homeFragment);
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                    break;
            }
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.homeFragment:
                    toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    loadFragment(homeFragment);
                    return true;
                case R.id.practiceFragment:
                    toolbar.setBackgroundColor(Color.parseColor("#283149"));
                    loadFragment(practiceFragment);
                    return true;
                case R.id.rankingFragment:
                    toolbar.setBackgroundColor(Color.parseColor("#283149"));
                    loadFragment(rankingFragment);
                    return true;
                case R.id.glossaryFragment:
                    toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    loadFragment(glossaryFragment);
                    return true;
                default:
                    return false;
            }
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            loadFragment(homeFragment);
        }
    }
}