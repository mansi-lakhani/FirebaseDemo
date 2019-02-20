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
import com.example.iroid.firebasedemo.adapter.AdapterServiceTicket;
import com.example.iroid.firebasedemo.model.BookingData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class FragServiceTicket extends Fragment {
    private RecyclerView recyclerView;
    private AdapterServiceTicket adapterServiceTicket;
    private ArrayList<BookingData> arrayBooking = new ArrayList<>();
    String firebaseUserKey;
    private DatabaseReference databaseReferenceBooking;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Query mQuery;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_service_ticket,container,false);
    }

    public FragServiceTicket getInstance(){
        return this;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firebaseUserKey = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReferenceBooking = FirebaseDatabase.getInstance().getReference("Booking");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        findViews();
        initRCV();
    }

    private void findViews() {
        recyclerView = getView().findViewById(R.id.rcvServiceTicket);

    }


    private void initRCV() {
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        //getBooking data
        getBookingData();
    }
    public void getBookingData() {
        arrayBooking.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();

        databaseReferenceBooking.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        if (!Objects.requireNonNull(dataSnapshot1.getValue(BookingData.class)).bookingAccepted) {
                            arrayBooking.add(dataSnapshot1.getValue(BookingData.class));
                        }
                    }
                }
                progressDialog.dismiss();
                adapterServiceTicket = new AdapterServiceTicket(getContext(),arrayBooking,FragServiceTicket.this);
                recyclerView.setAdapter(adapterServiceTicket);
                adapterServiceTicket.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
