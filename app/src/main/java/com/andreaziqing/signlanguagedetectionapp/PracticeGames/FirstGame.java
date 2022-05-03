package com.andreaziqing.signlanguagedetectionapp.PracticeGames;

import com.andreaziqing.signlanguagedetectionapp.DetectorActivity;
import com.andreaziqing.signlanguagedetectionapp.HelperClasses.SignDictionary;
import com.andreaziqing.signlanguagedetectionapp.R;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Dictionary;
import java.util.Hashtable;

public class FirstGame extends DetectorActivity {

    TextView mFirstLetter, mSecondLetter, mThirdLetter;
    ImageView mFirstLetterImage, mSecondLetterImage, mThirdLetterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_game);

        Dictionary<String, Integer> signDictionary = new Hashtable<>();
        signDictionary.put("A", R.drawable.a);
        signDictionary.put("B", R.drawable.b);
        signDictionary.put("C", R.drawable.c);
        signDictionary.put("D", R.drawable.d);
        signDictionary.put("E", R.drawable.e);
        signDictionary.put("F", R.drawable.f);
        signDictionary.put("G", R.drawable.g);
        signDictionary.put("H", R.drawable.h);
        signDictionary.put("I", R.drawable.i);
        signDictionary.put("J", R.drawable.j);
        signDictionary.put("K", R.drawable.k);
        signDictionary.put("L", R.drawable.l);
        signDictionary.put("M", R.drawable.m);
        signDictionary.put("N", R.drawable.n);
        signDictionary.put("O", R.drawable.o);
        signDictionary.put("P", R.drawable.p);
        signDictionary.put("Q", R.drawable.q);
        signDictionary.put("R", R.drawable.r);
        signDictionary.put("S", R.drawable.s);
        signDictionary.put("T", R.drawable.t);
        signDictionary.put("U", R.drawable.u);
        signDictionary.put("V", R.drawable.v);
        signDictionary.put("W", R.drawable.w);
        signDictionary.put("X", R.drawable.x);
        signDictionary.put("Y", R.drawable.y);
        signDictionary.put("Z", R.drawable.z);

        mFirstLetter = findViewById(R.id.first_letter);
        mSecondLetter = findViewById(R.id.second_letter);
        mThirdLetter = findViewById(R.id.third_letter);
        mFirstLetterImage = findViewById(R.id.first_letter_image);
        mSecondLetterImage = findViewById(R.id.second_letter_image);
        mThirdLetterImage = findViewById(R.id.third_letter_image);

        mFirstLetter.setText("A");
        mSecondLetter.setText("F");
        mThirdLetter.setText("H");

        mFirstLetterImage.setImageResource(signDictionary.get("A"));
        mSecondLetterImage.setImageResource(signDictionary.get("F"));
        mThirdLetterImage.setImageResource(signDictionary.get("H"));
    }
}