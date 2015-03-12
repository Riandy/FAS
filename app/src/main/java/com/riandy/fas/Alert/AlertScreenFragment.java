package com.riandy.fas.Alert;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riandy.fas.R;


public class AlertScreenFragment extends Fragment {

    AlertModel alert;

    TextView alertName,alertDescription;
    View view;

    public AlertScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle bundle = this.getActivity().getIntent().getExtras();

        Log.d("Output final","ID="+bundle.getInt("id"));

        AlertDBHelper dbHelper = AlertDBHelper.getInstance(this.getActivity().getApplicationContext());
        alert  = dbHelper.getAlert(bundle.getInt("id"));

        alert.getAlertFeature().launchAlerts(getActivity().getApplicationContext());

        view = inflater.inflate(R.layout.fragment_alert_screen, container, false);

        alertName = (TextView) view.findViewById(R.id.textView_alertName);
        alertDescription = (TextView) view.findViewById(R.id.textView_alertDescription);

        alertName.setText(alert.getAlertFeature().getName());
        alertDescription.setText(alert.getAlertFeature().getDescription());

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        alert.getAlertFeature().stopAlerts();
    }

}
