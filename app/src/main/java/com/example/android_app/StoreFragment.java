package com.example.android_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class StoreFragment extends Fragment {

    Context context;

    public static StoreFragment newInstance(){
        return new StoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Clicker-> ", "Ha sido llamado el fragment");

        View rootView = inflater.inflate(R.layout.fragment_store, container, false);
        context = rootView.getContext();
        ImageButton buttonBack = rootView.findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Back to main page
                Log.d("Clicker-> ", "ME voy pa´home");

                Intent myIntent = new Intent(context, Game.class);
                startActivity(myIntent);


            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }


}