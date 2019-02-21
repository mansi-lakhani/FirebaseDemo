package com.example.iroid.firebasedemo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iroid.firebasedemo.R;
import com.example.iroid.firebasedemo.model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.PriorityQueue;

public class ActivityLogin extends AppCompatActivity {

    private EditText name,email,phonenumber,password;
    private TextView update,changepassword,bookingId,signout_button;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference,databaseReferenceBooking;
    private ImageView imageView;
    private Query mQuery;
    private String userKey;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = findViewById(R.id.name);
        email = findViewById(R.id.textView2);
        phonenumber = findViewById(R.id.textView3);
        password = findViewById(R.id.editText);
        update = findViewById(R.id.textView4);
        imageView = findViewById(R.id.imageView);
        bookingId = findViewById(R.id.bookingId);
        button = findViewById(R.id.gotoBookingPage);
        changepassword = findViewById(R.id.textView5);
        signout_button = findViewById(R.id.signout_button);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReferenceBooking = FirebaseDatabase.getInstance().getReference("Booking");



        if (firebaseAuth.getUid()!=null) {
             databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
            userKey = firebaseAuth.getUid();
             storageReference = firebaseStorage.getReference(firebaseAuth.getUid());
        }
        if (databaseReference!=null && storageReference!=null){

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user!=null){
                        name.setText(user.getName());
                        email.setText(user.getEmail());
                        phonenumber.setText(user.getPhonenumber());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"Could not get data of user " + databaseError, Toast.LENGTH_SHORT).show();
                }
            });

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).placeholder(R.drawable.ic_launcher_background).into(imageView);
                    Toast.makeText(getApplicationContext(),"image download success",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"image download failed",Toast.LENGTH_SHORT).show();
                }
            });
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this,Booking.class));
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                String emailaddress = email.getText().toString();
                String phone = phonenumber.getText().toString();
                Users user = new Users(username,emailaddress,phone,"","");
                databaseReference.setValue(user);
            }
        });
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String newPassword = "";
                if(!password.getText().toString().equals("")){
                    newPassword = password.getText().toString();
                }
                if (!newPassword.equals("")) {
                    firebaseUser.updatePassword(newPassword);
                }
            }
        });
        mQuery = databaseReferenceBooking.orderByChild("userid").equalTo(userKey);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        String temp = dataSnapshot1.getValue().toString();
                        temp = temp + " ";
                        bookingId.setText(temp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                Toast.makeText(getApplicationContext(), "Successfully signed out", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
