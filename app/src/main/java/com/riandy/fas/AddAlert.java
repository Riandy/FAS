package com.riandy.fas;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riandy.fas.Alert.AddAlertFeature;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAlert extends Fragment {


    public AddAlert() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_alert, container, false);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_specs,new AddAlertOneOffEvent()).commit();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_feature, new AddAlertFeature()).commit();
        return view;
    }


}
