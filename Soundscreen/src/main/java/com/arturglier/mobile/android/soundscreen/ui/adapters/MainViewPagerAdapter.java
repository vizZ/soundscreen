package com.arturglier.mobile.android.soundscreen.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.IconPagerAdapter;

public class MainViewPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    private final SherlockFragmentActivity activity;
    private final Class[] fragments;

    public MainViewPagerAdapter(SherlockFragmentActivity activity, Class[] fragments) {
        super(activity.getSupportFragmentManager());

        this.activity = activity;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return Fragment.instantiate(activity, fragments[i].getName());
    }

    @Override
    public int getIconResId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
