package com.andreaziqing.signlanguagedetectionapp.Tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreaziqing.signlanguagedetectionapp.HelperClasses.Adapters.PracticeAdapter.PracticeAdapter;
import com.andreaziqing.signlanguagedetectionapp.HelperClasses.Adapters.PracticeAdapter.PracticeHelperClass;
import com.andreaziqing.signlanguagedetectionapp.R;

import java.util.ArrayList;

public class PracticeFragment extends Fragment {

    private static final String PRACTICE_FRAGMENT = "PracticeFragment";

    RecyclerView practiceRecycler;
    RecyclerView.Adapter adapter;

    public PracticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(PRACTICE_FRAGMENT, "Starting Practice Fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);

        // Hooks
        practiceRecycler = view.findViewById(R.id.practice_recycler);
        practiceRecycler();

        return view;
    }

    private void practiceRecycler() {
        practiceRecycler.setHasFixedSize(true);
        practiceRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        ArrayList<PracticeHelperClass> games = new ArrayList<>();
        games.add(new PracticeHelperClass(R.drawable.guesslettersign, "Guess the letter sign!", "Guess the letter sign before time runs out. Challenge yourself and try to do it without the clues. You can do it!"));
        games.add(new PracticeHelperClass(R.drawable.spelltheword, "Spell the word with signs!", "Guess all the letters that form the word that appears on the screen. Be careful, time is running out! "));
        games.add(new PracticeHelperClass(R.drawable.makeamatch, "Make a match!", "Match the letter with its sign and make a \"match\". You can do it! This is easier than making a match on Tinder!"));

        adapter = new PracticeAdapter(games);
        practiceRecycler.setAdapter(adapter);
    }
}