package com.andreaziqing.signlanguagedetectionapp.Common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.andreaziqing.signlanguagedetectionapp.Models.User;
import com.andreaziqing.signlanguagedetectionapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpTabFragment extends Fragment {

    // Variables
    TextInputLayout regUsername, regEmail, regPhoneNumber, regPassword;
    Button signup_button;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_tab, container, false);

        // Hooks
        regUsername = root.findViewById(R.id.username);
        regEmail = root.findViewById(R.id.email);
        regPhoneNumber = root.findViewById(R.id.phone_num);
        regPassword = root.findViewById(R.id.password);
        signup_button = root.findViewById(R.id.signup_button);

        // Save data in Firebase on button click
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance("https://signlanguagedetectionapp-default-rtdb.europe-west1.firebasedatabase.app");
                reference = rootNode.getReference("Users");

                // Get all values
                String username = regUsername.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneNumber = regPhoneNumber.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();

                User user = new User(username, email, phoneNumber, password);

                reference.child(username).setValue(user);
            }
        }); // Register Button Method End
        return root;
    }// onCreate Method End

    private Boolean validateUsername() {
        String value = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (value.isEmpty()){
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (value.length() >= 15) {
            regUsername.setError("Username too long");
            return false;
        } else if (!value.matches(noWhiteSpace)) {
            regUsername.setError("White spaces are not allowed");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = regPassword.getEditText().getText().toString();
        String passwordValue = "";

        if (value.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!value.matches(passwordValue)) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }
}
