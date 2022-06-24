package com.andreaziqing.signlanguagedetectionapp.UserTabs;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andreaziqing.signlanguagedetectionapp.Common.LoginActivity;
import com.andreaziqing.signlanguagedetectionapp.HelperClasses.Adapters.HomeAdapter.LessonsAdapter;
import com.andreaziqing.signlanguagedetectionapp.HelperClasses.Adapters.HomeAdapter.LessonsHelperClass;
import com.andreaziqing.signlanguagedetectionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String HOME_FRAGMENT = "HomeFragment";

    RecyclerView lessonsRecycler;
    RecyclerView.Adapter adapter;

    // * Firebase Auth *
    TextView mUsernameEmail;
    ImageButton mLogoutButton;
    private FirebaseAuth firebaseAuth;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(HOME_FRAGMENT, "Starting Home Fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // * Firebase Auth *
        mUsernameEmail = view.findViewById(R.id.hi_username);
        final String usernameEmail = mUsernameEmail.getText().toString().trim();
        // Init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        // Hooks
        lessonsRecycler = view.findViewById(R.id.lessons_recycler);
        lessonsRecycler();

        // Logout user
        mLogoutButton = view.findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                checkUser();
            }
        });

        return view;
    }

    private void checkUser() {
        // Check if user is not logged in then move to login activity

        // Get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            // User is not logged in, move to login screen
            startActivity(new Intent(getContext(), LoginActivity.class));
        } else {
            // User logged in, get info and set username
            for (UserInfo profile : firebaseUser.getProviderData()) {
                String username = profile.getDisplayName();
                mUsernameEmail.setText(username);
            }
        }
    }

    private void lessonsRecycler() {
        lessonsRecycler.setHasFixedSize(true);
        lessonsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<LessonsHelperClass> lessons = new ArrayList<>();
        lessons.add(new LessonsHelperClass(R.drawable.atoh, "Lesson 1", "We will go deeper into learning the letters of the alphabet between A and H. "));
        lessons.add(new LessonsHelperClass(R.drawable.itop, "Lesson 2", "We will go deeper into learning the letters of the alphabet between I and P. "));
        lessons.add(new LessonsHelperClass(R.drawable.qtoz, "Lesson 3", "We will go deeper into learning the letters of the alphabet between Q and Z. "));

        adapter = new LessonsAdapter(lessons);
        lessonsRecycler.setAdapter(adapter);
    }
}