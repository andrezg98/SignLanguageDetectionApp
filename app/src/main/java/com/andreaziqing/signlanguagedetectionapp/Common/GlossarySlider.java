package com.andreaziqing.signlanguagedetectionapp.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.andreaziqing.signlanguagedetectionapp.HelperClasses.Adapters.GlossaryAdapter.GlossarySliderAdapter;
import com.andreaziqing.signlanguagedetectionapp.PracticeGames.SecondGame;
import com.andreaziqing.signlanguagedetectionapp.R;
import com.andreaziqing.signlanguagedetectionapp.UserTabs.GlossaryFragment;
import com.andreaziqing.signlanguagedetectionapp.UserTabs.NavigationTabsController;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GlossarySlider extends AppCompatActivity {

    private static final String GLOSSARY_SLIDER = "GlossarySlider";

    LottieAnimationView lottieAnimationView;

    // Variables
    ViewPager viewPager;
    LinearLayout dotsLayout;
    GlossarySliderAdapter glossarySliderAdapter;
    TextView[] dots;

    public int positionLetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary_slider);

        // Hooks
        viewPager = findViewById(R.id.glossary_slider);
        dotsLayout = findViewById(R.id.dots_glossary);

        lottieAnimationView = findViewById(R.id.lottie_swipe);
        lottieAnimationView.animate().setDuration(100).setStartDelay(3000);

        // Call adapter
        glossarySliderAdapter = new GlossarySliderAdapter(this);
        viewPager.setAdapter(glossarySliderAdapter);

        // Recojo la posición de la letra
        Bundle bundle = getIntent().getExtras();
        positionLetter = bundle.getInt("position");

        // Dots
        addDots(positionLetter);
        viewPager.setCurrentItem(positionLetter);
        viewPager.addOnPageChangeListener(changeListener);
    }

    public void closeGlossary(View view) {
        Intent intent = new Intent(getApplicationContext(), NavigationTabsController.class);
        intent.putExtra("nextFragment", "GlossaryFragment");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    public void nextLetter(View view){
        viewPager.setCurrentItem(positionLetter + 1);
    }

    public void previewLetter(View view){
        viewPager.setCurrentItem(positionLetter - 1);
    }

    private void addDots(int position) {
        dots = new TextView[26];

        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextColor(getResources().getColor(R.color.gray_blue));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);

            Log.d(GLOSSARY_SLIDER, "Dot pintado");
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.neon_pink));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            positionLetter = position;
            lottieAnimationView.setTranslationY(1400);
            lottieAnimationView.cancelAnimation();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}