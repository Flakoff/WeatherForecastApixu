package com.flakoff.weatherforecastapixu.data;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class ForecastViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    public ForecastViewPagerAdapter(FragmentManager fm, List<Fragment> f) {
        super(fm);
        this.fragments = f;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}