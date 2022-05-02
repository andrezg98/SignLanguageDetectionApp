package com.andreaziqing.signlanguagedetectionapp.Common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.andreaziqing.signlanguagedetectionapp.Databases.SessionManager;
import com.andreaziqing.signlanguagedetectionapp.MainActivity;
import com.andreaziqing.signlanguagedetectionapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginTabFragment extends Fragment {

    private static final String LOGIN_TAB_FRAGMENT = "Login Tab Fragment";

    TextView forgotPassword;
    Button login_button;
    CheckBox rememberMe;
    TextInputEditText emailEditText, passwordEditText;
    float v = 0;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    // * Firebase Auth *
    TextInputLayout mEmail, mPassword;
    private FirebaseAuth firebaseAuth;

    // progress dialog
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container, false);

        // * Firebase Auth *
        // Init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        // Configure progress dialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Logging In");
        progressDialog.setCanceledOnTouchOutside(false);

        mEmail = view.findViewById(R.id.email_login);
        mPassword = view.findViewById(R.id.password_login);

        forgotPassword = view.findViewById(R.id.forget_password);
        login_button = view.findViewById(R.id.login_button);
        rememberMe = view.findViewById(R.id.remember_me);
        emailEditText = view.findViewById(R.id.editEmailLogin);
        passwordEditText = view.findViewById(R.id.editPasswordLogin);

        // Check weather username and password is already saved in Shared Preferences or not
        SessionManager sessionManager = new SessionManager(getContext(), SessionManager.SESSION_REMEMBERME);
        if (sessionManager.checkRememberMe()) {
            HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailFromSession();
            emailEditText.setText(rememberMeDetails.get(SessionManager.KEY_USERNAME_REMEMBER));
            passwordEditText.setText(rememberMeDetails.get(SessionManager.KEY_PASSWORD_REMEMBER));
        }

        // Animation
        mEmail.setTranslationX(800);
        mPassword.setTranslationX(800);
        forgotPassword.setTranslationX(800);
        login_button.setTranslationX(800);

        mEmail.setAlpha(v);
        mPassword.setAlpha(v);
        forgotPassword.setAlpha(v);
        login_button.setAlpha(v);

        mEmail.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        mPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgotPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login_button.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        // Save data in Firebase on button click
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate Login info
                if (!validateEmail() | !validatePassword()) {
                    return;
                } else {
                    // Get values from fields
                    final String email = mEmail.getEditText().getText().toString().trim();
                    final String password = mPassword.getEditText().getText().toString().trim();

                    firebaseLogin(email, password);

                    if (rememberMe.isChecked()) {
                        SessionManager sessionManager = new SessionManager(getContext(), SessionManager.SESSION_REMEMBERME);
                        sessionManager.createRememberMeSession(email, password);
                    }
//                    isUser();
                }
            }
        });

        return view;
    }

    private void checkUser() {
        // Check if user is already logged in
        // if already logged in then open home activity

        // Get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            // User is already logged in
            startActivity(new Intent(getContext(), MainActivity.class));
        }
    }

    // * Useful functions *

    private void firebaseLogin(String email, String password) {
        // Show progress
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // Login success
                progressDialog.dismiss();
                // Get user info
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String email = firebaseUser.getEmail();
                Toast.makeText(getContext(), "Logged In\n" + email, Toast.LENGTH_SHORT).show();

                // Open Home Activity
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Login failed
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateEmail() {
        String value = mEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (value.isEmpty()){
            mEmail.setError("Field cannot be empty");
            return false;
        } else if (!value.matches(emailPattern)) {
            mEmail.setError("Invalid email address");
            return false;
        } else {
            mEmail.setError(null);
            mEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = mPassword.getEditText().getText().toString();

        if (value.isEmpty()) {
            mPassword.setError("Field cannot be empty");
            return false;
        } else {
            mPassword.setError(null);
            mPassword.setErrorEnabled(false);
            return true;
        }
    }

//    private void isUser() {
//        String userEnteredUsername = mEmail.getEditText().getText().toString().trim();
//        String userEnteredPassword = mPassword.getEditText().getText().toString().trim();
//
//        rootNode = FirebaseDatabase.getInstance("https://signlanguagedetectionapp-default-rtdb.europe-west1.firebasedatabase.app/");
//        reference = rootNode.getReference("Users");
//        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
//
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//
//                    mEmail.setError(null);
//                    mEmail.setErrorEnabled(false);
//
//                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);
//
//                    if (passwordFromDB.equals(userEnteredPassword)) {
//                        mEmail.setError(null);
//                        mEmail.setErrorEnabled(false);
//
//                        String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
//                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);
//                        String phoneNumberFromDB = snapshot.child(userEnteredUsername).child("phoneNumber").getValue(String.class);
//
//                        // Create Session
//                        SessionManager sessionManager = new SessionManager(getContext(), SessionManager.SESSION_USERSESSION);
//                        sessionManager.createLoginSession(usernameFromDB, emailFromDB, phoneNumberFromDB, passwordFromDB);
//
//                        Intent intent = new Intent(getContext(), MainActivity.class);
//                        startActivity(intent);
//                    } else {
//                        mPassword.setError("Wrong password");
//                    }
//                } else {
//                    mEmail.setError("No such user exist");
//                    mEmail.requestFocus();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}
