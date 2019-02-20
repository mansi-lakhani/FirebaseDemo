package com.example.iroid.firebasedemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.iroid.firebasedemo.R;
import com.example.iroid.firebasedemo.fragments.FragCurrentHistory;
import com.example.iroid.firebasedemo.fragments.FragPastHistory;
import com.example.iroid.firebasedemo.model.BookingData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

public class AdapterCurrentHistory extends RecyclerView.Adapter<AdapterCurrentHistory.MyViewHolder> {

    private DatabaseReference databaseReference;
    private String firebaseUserKey;
    Context context;
    private BookingData objbookingData;
    private Query mQuery;
    private String obtainedKey;
    private FragCurrentHistory fragCurrentHistory;
    private FragPastHistory fragPastHistory;
    public Button btnAccept;
    private ArrayList<BookingData> arrayBookingData;
    public AdapterCurrentHistory(Context context, ArrayList<BookingData> arraybookingData, FragCurrentHistory fragCurrentHistory,
                                 FragPastHistory fragPastHistory) {
        this.context = context;
        this.arrayBookingData = arraybookingData;
        this.fragCurrentHistory = fragCurrentHistory;
        this.fragPastHistory = fragPastHistory;
        firebaseUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Booking");

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtVehicleinfo,txtLocationinfo,txtDate,txtTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtVehicleinfo = itemView.findViewById(R.id.txtVehicleinfo);
            txtLocationinfo = itemView.findViewById(R.id.txtLocationinfo);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            BookingData data = arrayBookingData.get(getPosition());
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                if (Objects.requireNonNull(dataSnapshot1.getValue(BookingData.class)).key.equals(data.key)) {
                                    obtainedKey = dataSnapshot1.getKey();
                                    if (obtainedKey!=null) {
                                        databaseReference.child(obtainedKey).child("bookingCompleted").setValue(true);
                                        fragCurrentHistory.refreshData();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
            if (fragCurrentHistory==null && fragPastHistory!=null){
                btnAccept.setVisibility(View.GONE);
            }
            else {
                btnAccept.setVisibility(View.VISIBLE);
            }
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_booking_details,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String vehicleInfo = arrayBookingData.get(i).Year + ", " + arrayBookingData.get(i).Make + ", "
                + arrayBookingData.get(i).Model;
        String addressInfo = arrayBookingData.get(i).Streenname + ", " + arrayBookingData.get(i).Apt;
        String date = arrayBookingData.get(i).Date;
        String time = arrayBookingData.get(i).Time;
        myViewHolder.txtVehicleinfo.setText(vehicleInfo);
        myViewHolder.txtLocationinfo.setText(addressInfo);
        myViewHolder.txtDate.setText(date);
        myViewHolder.txtTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return arrayBookingData.size();
    }
}
