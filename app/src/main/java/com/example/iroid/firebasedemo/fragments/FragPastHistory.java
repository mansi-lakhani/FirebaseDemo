package com.example.iroid.firebasedemo.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iroid.firebasedemo.R;
import com.example.iroid.firebasedemo.adapter.AdapterCurrentHistory;
import com.example.iroid.firebasedemo.model.BookingData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FragPastHistory extends Fragment {

    private String firebaseUserKey;
    private DatabaseReference databaseReferenceBooking;
    private Query mQuery;
    private ArrayList<BookingData> arrayBooking = new ArrayList<>();
    private RecyclerView recyclerView;
    private FragBookingHistory fragBookingHistory;
    private AdapterCurrentHistory adapterCurrentHistory;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_past_history,container,false);
    }

    public FragPastHistory getInstance(FragBookingHistory fragBookingHistory){
        this.fragBookingHistory = fragBookingHistory;
        return this;
    }
    public FragPastHistory getInstance(AdapterCurrentHistory adapterCurrentHistory){
        this.adapterCurrentHistory = adapterCurrentHistory;
        return this;
    }
    public FragPastHistory refreshData(){
        getBookingData();
        return  this;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firebaseUserKey = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReferenceBooking = FirebaseDatabase.getInstance().getReference("Booking");

        findViews();
        setRecyclerview();
    }

    private void findViews() {
        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.rcvPastHistory);
    }

    private void setRecyclerview() {
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //getBooking data
        getBookingData();

    }

    public void getBookingData() {
        arrayBooking.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        mQuery = databaseReferenceBooking.orderByChild("userid").equalTo(firebaseUserKey);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        if(Objects.requireNonNull(dataSnapshot1.getValue(BookingData.class)).bookingCompleted &&
                            Objects.requireNonNull(dataSnapshot1.getValue(BookingData.class)).bookingAccepted){
                            arrayBooking.add(dataSnapshot1.getValue(BookingData.class));
                        }

                    }
                }
                progressDialog.dismiss();
                adapterCurrentHistory = new AdapterCurrentHistory(getActivity(),arrayBooking,null,FragPastHistory.this);
                recyclerView.setAdapter(adapterCurrentHistory);
                adapterCurrentHistory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
