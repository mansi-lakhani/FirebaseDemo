package com.example.iroid.firebasedemo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iroid.firebasedemo.R;
import com.example.iroid.firebasedemo.adapter.PagerAdapterBookinghistory;

public class FragBookingHistory extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapterBookinghistory pagerAdapterBookinghistory;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_bookinghistory,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();

        tabLayout.addTab(tabLayout.newTab().setText("Current"));
        tabLayout.addTab(tabLayout.newTab().setText("Past"));

        pagerAdapterBookinghistory = new PagerAdapterBookinghistory(getChildFragmentManager(),getActivity(),tabLayout.getTabCount(),this);
        viewPager.setAdapter(pagerAdapterBookinghistory);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void findViews() {
        tabLayout = getView().findViewById(R.id.tabLayoutBH);
        viewPager = getView().findViewById(R.id.viewPagerBH);
    }

    public void refreshBookingHistory(){
        FragCurrentHistory fragCurrentHistory = (FragCurrentHistory) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPagerBH + ":" + 0);
        FragPastHistory fragPastHistory = (FragPastHistory) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPagerBH + ":" + 1);
        if (fragCurrentHistory!=null)
            fragCurrentHistory.getBookingData();
        if (fragPastHistory!=null)
            fragPastHistory.getBookingData();
    }
}
