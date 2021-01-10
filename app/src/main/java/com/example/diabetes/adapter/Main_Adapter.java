package com.example.diabetes.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Main_Adapter extends FragmentPagerAdapter {
    private ArrayList<Fragment>arrayFragment=new ArrayList<>();

    public Main_Adapter(FragmentManager fm) {
        super(fm);
    }
    
    public void addFragment(Fragment fragment){
        arrayFragment.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return arrayFragment.get(position);
    }

    @Override
    public int getCount() {
        return arrayFragment.size();
    }

}