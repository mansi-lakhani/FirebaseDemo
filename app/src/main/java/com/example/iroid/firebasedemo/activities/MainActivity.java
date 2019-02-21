package com.example.iroid.firebasedemo.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iroid.firebasedemo.R;
import com.example.iroid.firebasedemo.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button mSendData,updateUser1,deleteUser2,register;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Users users[];
    private ImageView profile;
    private EditText fullname;
    private String email,password,phonenumber,name,userToken;
    private TextInputEditText txtemail,txtphonenumber,txtpassword;
    private TextView signup,txtLogin;
    private String userid[];
    private FirebaseAuth firebaseAuth;
    private Boolean isUser;
    private int PICK_IMAGE = 22;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference userDatabase;
    private RadioButton user,serviceProvider;
    private DatabaseReference mRef1,mRef;
    private Uri path;
    private String DownloadUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        getDataBaseReferences();

        //create Notification channel
        createNotificationChannel();

        //subscribe to notifications if he/she is sercice provider
        if(serviceProvider.isChecked()) {
            FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.msg_subscribed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.msg_subscribe_failed);
                            }
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               email = txtemail.getText().toString().trim();
                password = txtpassword.getText().toString().trim();
                phonenumber = txtphonenumber.getText().toString().trim();
                name = fullname.getText().toString().trim();
                if (user.isChecked()){
                    isUser = true;
                }
                else {
                    isUser = false;
                }
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Registering...");
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            uploadImage();
                            progressDialog.dismiss();

                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });








        mSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mRef.setValue("User1");
//                mRef.child("Name").push().setValue("Mansi");
//                mRef.child("Location").push().setValue("Latitude");
                for (int i=0;i<2;i++) {
                    userid[i] = mRef.push().getKey();
                    mRef.child(userid[i]).setValue(users[i]);
                }
            }
        });
        updateUser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = "User1@gmail.com";
                mRef.child(userid[0]).child("email").setValue(newEmail);
            }
        });

        deleteUser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printUserid();
//                mRef.child(userid[1]).setValue(null);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });

    }

    private void getDataBaseReferences() {
        //gets reference to database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        //creates node at top level named users
        mRef = mDatabase.getDatabase().getReference("Users");
    }

    private void findViews() {
        mSendData = findViewById(R.id.mSendData);
        updateUser1 = findViewById(R.id.updateUser1);
        deleteUser2 = findViewById(R.id.deleteUser2);
        fullname = findViewById(R.id.fullname);
        txtLogin = findViewById(R.id.login_text);
        signup = findViewById(R.id.signup_button);
        profile = findViewById(R.id.profile);
        user = findViewById(R.id.radioUser);
        serviceProvider = findViewById(R.id.radioServiceprovider);
        txtemail = findViewById(R.id.txtemail);


        txtphonenumber = findViewById(R.id.txtphonenumber);
        txtpassword = findViewById(R.id.txtpassword);
    }

    private void generateToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                userToken = instanceIdResult.getToken();
                Log.e("Token generation succes",instanceIdResult.getToken());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Token generation failed",e.getMessage());
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel =
                    new NotificationChannel("pushNotifications","pushNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    private void uploadImage() {

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        if(path!=null && firebaseAuth.getUid()!=null){
            storageReference = firebaseStorage.getReference();
            storageReference.child("Profilepictures").child(firebaseAuth.getUid()).putFile(path).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Image uploaded successfully",Toast.LENGTH_SHORT).show();

                        storageReference.child("Profilepictures").child(firebaseAuth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DownloadUri = uri.toString();
                                Toast.makeText(getApplicationContext(),"Imageul successfully stored",Toast.LENGTH_SHORT).show();
                                sendEmailverification();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Image uploading failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode  == RESULT_OK && data.getData()!=null){
            path = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private boolean validateData() {
        Boolean flag = true;
        if(fullname.getText()!=null){
            Toast.makeText(getApplicationContext(),"Enter name",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if (txtemail.getText()!=null){
            Toast.makeText(getApplicationContext(),"Enter email",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if (txtphonenumber.getText()!=null){
            Toast.makeText(getApplicationContext(),"Enter phonenumber",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if (txtpassword.getText()!=null){
            Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    private void printUserid() {
        Toast.makeText(getApplicationContext(),"Userid1 : " + userid[0],Toast.LENGTH_SHORT).show();
    }

    //send data to database
    private void sendData(){
        if (!serviceProvider.isChecked()) {
            userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        }
        else{
            userDatabase = FirebaseDatabase.getInstance().getReference("Service Providers");
        }
        mRef1 = userDatabase.child(Objects.requireNonNull(firebaseAuth.getUid()));
//        generateToken();
        if (firebaseAuth.getUid()!=null) {
            Users users = new Users(name,email,phonenumber,"",firebaseAuth.getUid());
            mRef1.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Data uploaded successfully",Toast.LENGTH_SHORT).show();
                        mRef1.child("imageUrl").setValue(DownloadUri);
                        Toast.makeText(getApplicationContext(),"Imageul uploaded",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Imageul uploaded failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    //send email verification
    private void sendEmailverification(){
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Email has been sent to mail:" + firebaseUser.getEmail() +
                                " kindly verify it" ,Toast.LENGTH_SHORT).show();
                        sendData();

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Email for verification could not be sent, please try again later" ,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
