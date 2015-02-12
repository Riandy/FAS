package com.riandy.fas.Alert;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riandy.fas.R;


public class AlertScreenFragment extends Fragment {

    AlertModel alert;

    public AlertScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle bundle = this.getActivity().getIntent().getExtras();

        Log.d("Output", bundle.getString(AlertContract.Alert.COLUMN_NAME_ALERT_DESCRIPTION));
        Log.d("Output","ID="+bundle.getInt("id"));

        AlertDBHelper dbHelper = AlertDBHelper.getInstance(this.getActivity().getApplicationContext());
        alert  = dbHelper.getAlert(bundle.getInt("id"));

        alert.getAlertFeature().launchAlerts(getActivity().getApplicationContext());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alert_screen, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        alert.getAlertFeature().stopAlerts();
    }

}
