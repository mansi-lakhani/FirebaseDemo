package com.example.iroid.firebasedemo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iroid.firebasedemo.R;
import com.example.iroid.firebasedemo.model.BookingData;
import com.example.iroid.firebasedemo.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragBooking extends Fragment {
    private TextView name,email,phonenumber;
    private EditText txtYear,txtMake,txtModel;
    private EditText txtStreet,txtApt;
    private EditText txtDate,txtHour1;
    private TextView lblBookService;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String year,make,model,street,apt,date,hour,message;
    private String userName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_booking,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        findViews();
        initClick();
        super.onActivityCreated(savedInstanceState);
    }

    private void initClick() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        if (firebaseAuth.getUid()!=null) {
//            databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
            databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
        }
        if (databaseReference!=null){

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user!=null){
                        name.setText(user.getName());
                        userName = user.getName();
                        email.setText(user.getEmail());
                        phonenumber.setText(user.getPhonenumber());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(),"Could not get data of user " + databaseError, Toast.LENGTH_SHORT).show();
                }
            });

        }
        lblBookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBookingData();
            }
        });
    }

    private void findViews() {
        name = getView().findViewById(R.id.txtNameReq);
        email = getView().findViewById(R.id.txtEmailReq);
        phonenumber = getView().findViewById(R.id.txtPhoneNumber);
        //vehicle
        txtYear = getView().findViewById(R.id.txtYear);
        txtMake = getView().findViewById(R.id.txtMake);
        txtModel = getView().findViewById(R.id.txtModel);
        //address
        txtStreet = getView().findViewById(R.id.txtStreet);
        txtApt = getView().findViewById(R.id.txtApt);
        //date and time
        txtDate = getView().findViewById(R.id.txtDate);
        txtHour1 = getView().findViewById(R.id.txtHour1);
        //button
        lblBookService = getView().findViewById(R.id.lblBookService);

    }

    private void uploadBookingData() {
        year = txtYear.getText().toString();
        make = txtMake.getText().toString();
        model = txtModel.getText().toString();
        street = txtStreet.getText().toString();
        apt = txtApt.getText().toString();
        date = txtDate.getText().toString();
        hour = txtHour1.getText().toString();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        final DatabaseReference databaseReferencebooking = FirebaseDatabase.getInstance().getReference("Booking");

        if (firebaseAuth.getUid()!=null){

            String pushKey = databaseReferencebooking.push().getKey();

            final BookingData bookingData = new BookingData(apt,date,make,model,street,hour,year,false,databaseReference.getKey(),pushKey,false,
                    userName);
//            bookingDataMap.put(id.toString(),bookingData);
            if (pushKey!=null) {
                databaseReferencebooking.child(pushKey).setValue(bookingData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Successfully added booking", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "failed adding booking", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }

    }
}
