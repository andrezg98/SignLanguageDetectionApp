package com.andreaziqing.signlanguagedetectionapp.Databases;

import android.content.Context;
import android.content.SharedPreferences;

import com.andreaziqing.signlanguagedetectionapp.Common.LoginTabFragment;

import java.util.HashMap;

public class SessionManager {

    // Variables
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    // Sessions names
    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMBERME = "rememberMe";

    // User session variables
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONENUMBER = "phoneNumber";
    public static final String KEY_PASSWORD = "password";

    // Remember me variables
    private static final String IS_REMEMBERME = "IsRememberMe";
    public static final String KEY_USERNAME_REMEMBER = "username";
    public static final String KEY_PASSWORD_REMEMBER = "password";

    public SessionManager(Context _context, String sessionName) {
        context = _context;
        usersSession = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        editor = usersSession.edit(); // to update the data
    }

    // Login Session - Users
    public void createLoginSession(String username, String email, String phoneNumber, String password) {
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONENUMBER, phoneNumber);
        editor.putString(KEY_PASSWORD, password);

        editor.commit();
    }

    public HashMap<String, String> getUsersDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME, null));
        userData.put(KEY_EMAIL, usersSession.getString(KEY_EMAIL, null));
        userData.put(KEY_PHONENUMBER, usersSession.getString(KEY_PHONENUMBER, null));
        userData.put(KEY_PASSWORD, usersSession.getString(KEY_PASSWORD, null));

        return userData;
    }

    public boolean checkLogin() {
        if (usersSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else {
            return false;
        }
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }

    // Remember Me - Session Functions
    public void createRememberMeSession(String username, String password) {
        editor.putBoolean(IS_REMEMBERME, true);
        editor.putString(KEY_USERNAME_REMEMBER, username);
        editor.putString(KEY_PASSWORD_REMEMBER, password);

        editor.commit();
    }

    public HashMap<String, String> getRememberMeDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_USERNAME_REMEMBER, usersSession.getString(KEY_USERNAME_REMEMBER, null));
        userData.put(KEY_PASSWORD_REMEMBER, usersSession.getString(KEY_PASSWORD_REMEMBER, null));

        return userData;
    }

    public boolean checkRememberMe() {
        if (usersSession.getBoolean(IS_REMEMBERME, false)) {
            return true;
        } else {
            return false;
        }
    }
}
