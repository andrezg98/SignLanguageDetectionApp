package com.andreaziqing.signlanguagedetectionapp.UserTabs;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String HOME_FRAGMENT = "HomeFragment";

    RecyclerView lessonsRecycler;
    RecyclerView.Adapter adapter;

    // * Firebase Auth *
    TextView mUsernameEmail;
    ImageButton mLogoutButton;
    private FirebaseAuth firebaseAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            // Get user name and display in screen
            DocumentReference docRef = db.collection("userstats").document(firebaseUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            mUsernameEmail.setText(document.getString("name"));
                        } else {
                            Log.d(HOME_FRAGMENT, "No such document");
                        }
                    } else {
                        Log.d(HOME_FRAGMENT, "get failed with ", task.getException());
                    }
                }
            });
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