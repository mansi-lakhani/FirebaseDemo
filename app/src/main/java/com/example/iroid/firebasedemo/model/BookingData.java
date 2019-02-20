package com.example.iroid.firebasedemo.model;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;

public class BookingData {
    public String Year,Make,Model,Streenname,Apt,Date,Time,userid,key,userName;
    public Boolean bookingAccepted,bookingCompleted;

    public BookingData(){

    }
    public BookingData(String apt,String date, String make, String model, String streenname, String time,
                       String year, Boolean bookingAccepted, String userid,String key,Boolean bookingCompleted,
                        String userName) {

        Apt = apt;
        Date = date;
        Make = make;
        Model = model;
        Streenname = streenname;
        Time = time;
        Year = year;
        this.bookingAccepted = bookingAccepted;
        this.key = key;
        this.userid = userid;
        this.userName = userName;
        this.bookingCompleted = bookingCompleted;
    }

}
