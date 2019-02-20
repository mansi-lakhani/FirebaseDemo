package com.example.iroid.firebasedemo.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.iroid.firebasedemo.R;
import com.example.iroid.firebasedemo.fragments.FragBooking;
import com.example.iroid.firebasedemo.fragments.FragBookingHistory;
import com.example.iroid.firebasedemo.fragments.FragContactus;
import com.example.iroid.firebasedemo.fragments.FragEditProfile;
import com.example.iroid.firebasedemo.fragments.FragServiceTicket;

public class ServiceProvider extends AppCompatActivity {

    private LinearLayout lytServiceTicket,lytBookinghistory,lytProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);
        findViews();
        initListeners();
        initFrag();
    }
    private void findViews() {
        lytServiceTicket = findViewById(R.id.lytServiceTicket);
        lytBookinghistory = findViewById(R.id.lytBookinghistory);
        lytProfile = findViewById(R.id.lytProfile);
    }

    private void initListeners() {
        lytServiceTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearAllFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(FragServiceTicket.class.getName());
                ft.add(R.id.containerSP, new FragServiceTicket(), FragServiceTicket.class.getName());
                ft.commit();
            }
        });
        lytBookinghistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearAllFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(FragBookingHistory.class.getName());
                ft.add(R.id.containerSP, new FragBookingHistory(), FragBookingHistory.class.getName());
                ft.commit();
            }
        });

        lytProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearAllFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(FragEditProfile.class.getName());
                ft.add(R.id.containerSP, new FragEditProfile(), FragEditProfile.class.getName());
                ft.commit();
            }
        });
    }



    private void initFrag() {
        ClearAllFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(FragServiceTicket.class.getName());
        ft.add(R.id.containerSP, new FragServiceTicket(), FragServiceTicket.class.getName());
        ft.commit();
    }
    public void ClearAllFragment() {

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

}
