package com.example.iroid.firebasedemo.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.iroid.firebasedemo.fragments.FragBookingHistory;
import com.example.iroid.firebasedemo.fragments.FragCurrentHistory;
import com.example.iroid.firebasedemo.fragments.FragPastHistory;

public class PagerAdapterBookinghistory extends FragmentPagerAdapter {

    Context context;
    private int totalTabs;
    private FragBookingHistory fragBookingHistory;
    public PagerAdapterBookinghistory(FragmentManager fm, Context context, int totalTabs, FragBookingHistory fragBookingHistory) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
        this.fragBookingHistory = fragBookingHistory;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:{
                return new FragCurrentHistory().getInstance(fragBookingHistory);
            }
            case 1:{
                return new FragPastHistory().getInstance(fragBookingHistory);
            }
        }
        return new FragCurrentHistory();
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
