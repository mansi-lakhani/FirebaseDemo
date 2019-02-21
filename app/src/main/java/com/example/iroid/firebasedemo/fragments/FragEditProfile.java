package com.example.iroid.firebasedemo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iroid.firebasedemo.R;
import com.example.iroid.firebasedemo.activities.LoginActivity;
import com.example.iroid.firebasedemo.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class FragEditProfile extends Fragment {
    private TextView signout_button;
    private FirebaseAuth firebaseAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_edit_profile,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        signout_button = getView().findViewById(R.id.signout_button);
        firebaseAuth = FirebaseAuth.getInstance();
        initListeners();
    }

    private void initListeners() {
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                ClearAllFragment();
                getActivity().finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
                Toast.makeText(getActivity(), "Successfully signed out", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void ClearAllFragment() {

        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++) {
            getFragmentManager().popBackStack();
        }
    }
}
