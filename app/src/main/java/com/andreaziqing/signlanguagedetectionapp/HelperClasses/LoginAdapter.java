package com.andreaziqing.signlanguagedetectionapp.HelperClasses;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.andreaziqing.signlanguagedetectionapp.Common.LoginTabFragment;
import com.andreaziqing.signlanguagedetectionapp.Common.SignUpTabFragment;

public class LoginAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public LoginAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LoginTabFragment loginTabFragment = new LoginTabFragment();
                return loginTabFragment;
            case 1:
                SignUpTabFragment signupTabFragment = new SignUpTabFragment();
                return signupTabFragment;
            default:
                return null;
        }
    }
}
