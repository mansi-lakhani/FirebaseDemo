package com.example.iroid.firebasedemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iroid.firebasedemo.R;
import com.example.iroid.firebasedemo.fragments.FragCurrentHistory;
import com.example.iroid.firebasedemo.fragments.FragPastHistory;
import com.example.iroid.firebasedemo.fragments.FragServiceTicket;
import com.example.iroid.firebasedemo.model.BookingData;
import com.example.iroid.firebasedemo.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterServiceTicket extends RecyclerView.Adapter<AdapterServiceTicket.MyViewHolder> {

    private Context context;
    private ArrayList<BookingData> arrayList;
    private DatabaseReference databaseReference,databaseReferenceUsers;
    private String firebaseUserKey;
    private FragServiceTicket fragServiceTicket;
    private String obtainedKey;
    public AdapterServiceTicket(Context context, ArrayList<BookingData> arrayList , FragServiceTicket fragServiceTicket) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragServiceTicket = fragServiceTicket;
        firebaseUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Booking");
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("Users");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_service_ticket,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        String vehicleInfo = arrayList.get(i).Year + ", " + arrayList.get(i).Make + ", "
                + arrayList.get(i).Model;
        String addressInfo = arrayList.get(i).Streenname + ", " + arrayList.get(i).Apt;
        String date = arrayList.get(i).Date;
        String time = arrayList.get(i).Time;
        final String userId = arrayList.get(i).userid;
        databaseReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Users user = dataSnapshot1.getValue(Users.class);
                    if (user!=null) {
                        if (userId.equals(user.userId)) {
                            Picasso.get().load(user.imageUrl).into(myViewHolder.proPic);
                            myViewHolder.username.setText(user.name);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        myViewHolder.txtVehicleinfo.setText(vehicleInfo);
        myViewHolder.txtLocationinfo.setText(addressInfo);
        myViewHolder.txtDate.setText(date);
        myViewHolder.txtTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView proPic;
        public TextView txtVehicleinfo,txtLocationinfo,txtDate,txtTime,btnAccept,username;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            proPic = itemView.findViewById(R.id.proPic);
            txtVehicleinfo = itemView.findViewById(R.id.txtVehicleinfo);
            txtLocationinfo = itemView.findViewById(R.id.txtLocationinfo);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            username = itemView.findViewById(R.id.username);
            btnAccept = itemView.findViewById(R.id.btnAccept);

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            BookingData data = arrayList.get(getPosition());
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                if (Objects.requireNonNull(dataSnapshot1.getValue(BookingData.class)).key.equals(data.key)) {
                                    obtainedKey = dataSnapshot1.getKey();
                                    if (obtainedKey!=null) {
                                        databaseReference.child(obtainedKey).child("bookingAccepted").setValue(true);
                                        fragServiceTicket.getInstance().getBookingData();
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
        }
    }
}
