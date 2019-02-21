package com.example.iroid.firebasedemo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iroid.firebasedemo.R;
import com.example.iroid.firebasedemo.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class LoginActivity extends AppCompatActivity {

    private TextView txtForgotPassword,login_button;
    private TextInputEditText txtemail,txtpassword;
    private FirebaseAuth firebaseAuth;
    private Boolean emailFlag;
    private DatabaseReference databaseReferenceUser,databaseReferenceSP;
    private RadioButton radioUser,radioSP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        findViews();


        firebaseAuth = FirebaseAuth.getInstance();
        //forgot password

//        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
//            finish();
//            startActivity(new Intent(LoginActivity.this,ActivityLogin.class));
//        }
        initClick();

    }

    private void initClick() {
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtemail.setText("");
                txtpassword.setText("");
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
            }
        });


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUser(txtemail.getText().toString(),txtpassword.getText().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDatabaseReferences();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        Users user = dataSnapshot1.getValue(Users.class);
                        if (user.userId.equals(id)){
                            startActivity(new Intent(LoginActivity.this,Booking.class));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReferenceSP.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Users serviceProvider = dataSnapshot1.getValue(Users.class);
                        if (serviceProvider.userId.equals(id)){
                            startActivity(new Intent(LoginActivity.this,ServiceProvider.class));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void getDatabaseReferences() {
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferenceSP = FirebaseDatabase.getInstance().getReference("Service Providers");
    }

    private void findViews() {
        txtForgotPassword = findViewById(R.id.txtforgot_password);
        txtemail = findViewById(R.id.txtemail);
        txtpassword = findViewById(R.id.txtpassword);
        login_button = findViewById(R.id.login_button);
        radioUser = findViewById(R.id.radioUser);
        radioSP = findViewById(R.id.radioServiceprovider);
    }

    private void validateUser(String email, String password) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
//        if (user!=null){
//            Toast.makeText(getApplicationContext(), "User already logged in", Toast.LENGTH_SHORT).show();
//        }
//        else {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Logging in...");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        if(checkemailVerification()){
                            finish();
                            if (radioUser.isChecked()) {
                                finish();
                                startActivity(new Intent(LoginActivity.this, Booking.class));
                            }
                            else{
                                finish();
                                startActivity(new Intent(LoginActivity.this,ServiceProvider.class));
                            }
                        }
//                        Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//        }
    }
    //email verification
    private Boolean checkemailVerification(){
        emailFlag = false;
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null) {
            emailFlag = firebaseUser.isEmailVerified();
        }
        if (emailFlag){
            emailFlag = true;
            Toast.makeText(getApplicationContext(),"Email verified of " + firebaseUser + " and " +
                    "signin successfully",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Email should be verified else you won't be able to login" ,Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
        return  emailFlag;
    }
}
