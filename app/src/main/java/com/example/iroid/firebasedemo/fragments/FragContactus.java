package com.example.iroid.firebasedemo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.iroid.firebasedemo.R;

public class FragContactus extends Fragment {

    private EditText edtSubject,edtMessage;
    private Button btnSubmit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_contactus,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        initClick();
    }

    private void findViews() {
        edtSubject = getView().findViewById(R.id.edtSubject);
        edtMessage = getView().findViewById(R.id.edtMessage);
        btnSubmit = getView().findViewById(R.id.btnSubmit);
    }

    private void initClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
