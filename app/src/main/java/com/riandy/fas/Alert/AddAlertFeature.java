package com.riandy.fas.Alert;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riandy.fas.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAlertFeature extends Fragment {


    public AddAlertFeature() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_alert_feature, container, false);
    }


}
