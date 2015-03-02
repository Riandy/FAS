package com.riandy.fas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SplashScreen extends Fragment {

    View view;

    public SplashScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        getActivity().getActionBar().hide();

        Button btn_continue = (Button) view.findViewById(R.id.imageButton_continue);
        btn_continue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().getActionBar().show();
                Fragment fragment = new HomePage();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

            }
        });

        // Inflate the layout for this fragment
        return view;

    }

}
