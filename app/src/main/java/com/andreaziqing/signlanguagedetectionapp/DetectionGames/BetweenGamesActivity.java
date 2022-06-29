package com.andreaziqing.signlanguagedetectionapp.PracticeGames;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.andreaziqing.signlanguagedetectionapp.Lessons.FirstGame;
import com.andreaziqing.signlanguagedetectionapp.Lessons.SecondGame;
import com.andreaziqing.signlanguagedetectionapp.R;
import com.andreaziqing.signlanguagedetectionapp.Navigation.NavigationTabsController;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BetweenGamesActivity extends AppCompatActivity {

    private static final String BETWEEN_GAMES = "BetweenGamesActivity";

    LottieAnimationView lottieAnimationView;
    private static int SPLASH_TIMER = 5000;

    TextView mTitle, mDesc;
    Button mButtonNext, mButtonRepeat;

    String mPreviousActivity;
    public int mPositionGroup;
    String[] arrGroupOfLetters;
    String mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_between_games);

        lottieAnimationView = findViewById(R.id.lottie_welldone);
        mTitle = findViewById(R.id.between_games_title);
        mDesc = findViewById(R.id.between_games_desc);
        mButtonNext = findViewById(R.id.next_button);
        mButtonRepeat = findViewById(R.id.repeat_button);

        mState = "SUCCESS";

        // Recojo la actividad de donde provengo, para actuar en consecuencia.
        // También la posición del grupo de letras que ha elegido el usuario
        Bundle bundle = getIntent().getExtras();
        mPreviousActivity = bundle.getString("previousActivity");
        arrGroupOfLetters = bundle.getStringArray("arrGroupOfLetters");
        mPositionGroup = bundle.getInt("position");
        mState = bundle.getString("state");

        if (mState.equals("FAIL")) {
            mTitle.setText("Oh, no!");
            mButtonNext.setText("Try again");
            mButtonRepeat.setVisibility(View.INVISIBLE);
            lottieAnimationView.setAnimation(R.raw.fail);
            /*lottieAnimationView.getLayoutParams().height = 100;
            lottieAnimationView.setLayoutParams(lottieAnimationView.getLayoutParams());*/
        }

        lottieAnimationView.animate().setDuration(600).setStartDelay(4000);

        new Handler().postDelayed(()-> {

            mButtonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    if (mState.equals("FAIL")) {
                        repeatActivity(intent);
                    } else {
                        switch (mPreviousActivity) {
                            case "SecondGame":
                                intent = new Intent(getApplicationContext(), NavigationTabsController.class);
                                intent.putExtra("nextFragment", "HomeFragment");
                            case "FullSecondGame":
                            case "ThirdGame":
                                intent = new Intent(getApplicationContext(), NavigationTabsController.class);
                                intent.putExtra("nextFragment", "PracticeFragment");
                                break;
                            default:
                                break;
                        }

                        startActivity(intent);
                        finish();
                    }
                }
            });

            mButtonRepeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    repeatActivity(intent);
                }
            });

        }, SPLASH_TIMER);
    }

    private void repeatActivity(Intent intent) {
        switch (mPreviousActivity) {
            case "FirsGame":
                intent = new Intent(getApplicationContext(), FirstGame.class);
                intent.putExtra("position", mPositionGroup);
                break;
            case "SecondGame":
                intent = new Intent(getApplicationContext(), SecondGame.class);
                intent.putExtra("position", mPositionGroup);
                intent.putExtra("arrGroupOfLetters", arrGroupOfLetters);
                break;
            case "ThirdGame":
                intent = new Intent(getApplicationContext(), ThirdGame.class);
                break;
            case "FullSecondGame":
                intent = new Intent(getApplicationContext(), FullSecondGame.class);
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
        finish();
    }

    public void close(View view) {
        Intent intent;
        switch (mPreviousActivity) {
            case "ThirdGame":
            case "FullSecondGame":
                intent = new Intent(getApplicationContext(), NavigationTabsController.class);
                intent.putExtra("nextFragment", "PracticeFragment");
                break;
            default:
                intent = new Intent(getApplicationContext(), NavigationTabsController.class);
                intent.putExtra("nextFragment", "HomeFragment");
                break;
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
}