package com.riandy.fas;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.riandy.fas.Alert.AlertDBHelper;
import com.riandy.fas.Alert.AlertManagerHelper;
import com.riandy.fas.Alert.AlertModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private static final int FILE_SELECT_CODE = 0;
    private static final String MY_PREFS_NAME = "sharedDataFAS";
    View view;
    Context context;

    Switch syncGoogleCal;
    Button importXML;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        context = this.getActivity();
        syncGoogleCal = (Switch) view.findViewById(R.id.switch_syncGoogleCal);
        importXML = (Button) view.findViewById(R.id.button_browseXML);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        syncGoogleCal.setChecked(prefs.getBoolean("enableSync",false));

        syncGoogleCal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
                editor.putBoolean("enableSync", isChecked);
                editor.commit();
                GoogleCalendarSync gCal = new GoogleCalendarSync(context,1);
                if(isChecked) {
                    Toast.makeText(context, "FAS will sync to your google calendar.", Toast.LENGTH_SHORT).show();
                    gCal.addAllAlerts();
                }
                else {
                    Toast.makeText(context, "FAS will not sync to your google calendar.", Toast.LENGTH_SHORT).show();
                    gCal.deleteAllAlerts();
                }
            }
        });

        importXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        return view;
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d("FILE", "File Uri: " + uri.getPath());
                    List<AlertModel> list = AlertParser.parse(uri.getPath());
                    for(AlertModel model:list){
                        AlertDBHelper.getInstance(this.getActivity()).createAlert(model);
                    }
                    AlertManagerHelper.setAlerts(getActivity());
                }
                break;
        }
    }
}
