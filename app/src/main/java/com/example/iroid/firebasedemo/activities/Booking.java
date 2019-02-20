package com.example.iroid.firebasedemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.iroid.firebasedemo.fragments.FragBooking;
import com.example.iroid.firebasedemo.R;
import com.example.iroid.firebasedemo.fragments.FragBookingHistory;
import com.example.iroid.firebasedemo.fragments.FragContactus;
import com.example.iroid.firebasedemo.fragments.FragEditProfile;

public class Booking extends AppCompatActivity {

    private LinearLayout lytBookanoilchange,lytBookinghistory,lytContactus,lytProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        findViews();
        initListeners();
        initFrag();
    }
    private void findViews() {
        lytBookanoilchange = findViewById(R.id.lytBookanoilchange);
        lytBookinghistory = findViewById(R.id.lytBookinghistory);
        lytContactus = findViewById(R.id.lytContactus);
        lytProfile = findViewById(R.id.lytProfile);
    }

    private void initListeners() {
        lytBookanoilchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearAllFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(FragBooking.class.getName());
                ft.add(R.id.container, new FragBooking(), FragBooking.class.getName());
                ft.commit();
            }
        });
        lytBookinghistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearAllFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(FragBookingHistory.class.getName());
                ft.add(R.id.container, new FragBookingHistory(), FragBookingHistory.class.getName());
                ft.commit();
            }
        });
        lytContactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearAllFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(FragContactus.class.getName());
                ft.add(R.id.container, new FragContactus(), FragContactus.class.getName());
                ft.commit();
            }
        });
        lytProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearAllFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(FragEditProfile.class.getName());
                ft.add(R.id.container, new FragEditProfile(), FragEditProfile.class.getName());
                ft.commit();
            }
        });
    }



    private void initFrag() {
        ClearAllFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(FragBooking.class.getName());
        ft.add(R.id.container, new FragBooking(), FragBooking.class.getName());
        ft.commit();
    }
    public void ClearAllFragment() {

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

}
