package com.riandy.fas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.riandy.fas.Alert.AlertDBHelper;


public class HomePage extends Fragment {

    ListView list;

    public HomePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);


        list = (ListView) view.findViewById(R.id.listView_alert);
        AlertsArrayAdapter adapter = new AlertsArrayAdapter(view.getContext(), AlertDBHelper.getInstance(view.getContext()).getAlerts());
        list.setAdapter(adapter);
        return view;
    }


}
