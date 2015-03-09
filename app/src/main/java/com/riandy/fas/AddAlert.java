package com.riandy.fas;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.riandy.fas.Alert.AlertFeature;
import com.riandy.fas.Alert.AlertModel;


public class AddAlert extends Fragment implements AddAlertFeature.OnAddAlertFeatureListener {

    private static final int ADD_FEATURE_FRAGMENT = 1234;
    Switch customizedFeature;
    Button saveAlert;
    AlertModel alert;

    public AddAlert() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_alert, container, false);

        if(alert==null){
            alert = new AlertModel();
            Log.d("DEBUG","new alert model created");
        }

        Fragment addAlertSpecsFragment = getFragmentManager().findFragmentById(R.id.fragment_container_add_alert_specs);
        Fragment addAlertFeatureFragment = getFragmentManager().findFragmentById(R.id.fragment_container_add_alert_feature);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_specs, new AddAlertOneOffEvent()).commit();

        Fragment newFragment = new AddAlertFeature();
        newFragment.setTargetFragment(this,ADD_FEATURE_FRAGMENT);
        newFragment.setArguments(getAddFeatureBundle());
        getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_feature, newFragment).commit();

//        if(addAlertSpecsFragment==null) {
//            getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_specs, new AddAlertOneOffEvent()).commit();
//        }else{
//            view.findViewById(R.id.fragment_container_add_alert_specs).setVisibility(View.VISIBLE);
//        }
//        if(addAlertFeatureFragment==null) {
//            Fragment newFragment = new AddAlertFeature();
//            newFragment.setTargetFragment(this,ADD_FEATURE_FRAGMENT);
//            getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_feature, newFragment).commit();
//            view.findViewById(R.id.fragment_container_add_alert_feature).setVisibility(View.GONE);
//        }else{
//            view.findViewById(R.id.fragment_container_add_alert_feature).setVisibility(View.VISIBLE);
//
//        }

        customizedFeature = (Switch) view.findViewById(R.id.switch_customize_alert_feature);
        customizedFeature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //show the alert feature layout
                    view.findViewById(R.id.fragment_container_add_alert_feature).setVisibility(View.VISIBLE);
                }else{
                    view.findViewById(R.id.fragment_container_add_alert_feature).setVisibility(View.GONE);
                }
            }
        });

        saveAlert = (Button) view.findViewById(R.id.button_save_alert);
        saveAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAlertFeature alertFeatureFragment = (AddAlertFeature)getFragmentManager().findFragmentById(R.id.fragment_container_add_alert_feature);

                //AddAlertOneOffEvent alertSpecs = (AddAlertOneOffEvent)getFragmentManager().findFragmentById(R.id.fragment_container_add_alert_specs);

                AlertFeature alertFeature = alertFeatureFragment.getData();
                Log.d("TEST",""+alertFeature.isLaunchAppEnabled()+alertFeature.isSoundEnabled());
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("ADD ALERT","SAVING");
    }

    @Override
    public void onAddFeatureData(String tag, Object data) {
        switch (tag){
            case AddAlertFeature.TAG_APP_SELECTED:
                alert.getAlertFeature().setAppToLaunch((String)data);
                break;
            case AddAlertFeature.TAG_LAUNCH_APP_SWITCH:
                alert.getAlertFeature().setLaunchAppEnabled((boolean)data);
                break;
            case AddAlertFeature.TAG_NOTIF_SWITCH:
                alert.getAlertFeature().setNotificationEnabled((boolean)data);
                break;
            case AddAlertFeature.TAG_SOUND_SWITCH:
                alert.getAlertFeature().setSoundEnabled((boolean)data);
                break;
            case AddAlertFeature.TAG_SOUND_URL:
                alert.getAlertFeature().setTone((String)data);
                break;
            case AddAlertFeature.TAG_VIBRATE_SWITCH:
                alert.getAlertFeature().setVibrationEnabled((boolean)data);
                break;
            case AddAlertFeature.TAG_VOICE_SWITCH:
                alert.getAlertFeature().setVoiceInstructionStatusEnabled((boolean)data);
                break;


        }
    }

    public Bundle getAddFeatureBundle() {
        Bundle data = new Bundle();
        data.putBoolean(AddAlertFeature.TAG_SOUND_SWITCH,alert.getAlertFeature().isSoundEnabled());
        data.putBoolean(AddAlertFeature.TAG_NOTIF_SWITCH,alert.getAlertFeature().isNotificationEnabled());
        data.putBoolean(AddAlertFeature.TAG_LAUNCH_APP_SWITCH,alert.getAlertFeature().isLaunchAppEnabled());
        data.putBoolean(AddAlertFeature.TAG_VIBRATE_SWITCH,alert.getAlertFeature().isVibrationEnabled());
        data.putBoolean(AddAlertFeature.TAG_VOICE_SWITCH,alert.getAlertFeature().isVoiceInstructionStatusEnabled());
        data.putString(AddAlertFeature.TAG_SOUND_URL,alert.getAlertFeature().getTone());
        data.putString(AddAlertFeature.TAG_APP_SELECTED,alert.getAlertFeature().getAppToLaunch());
        return data;
    }
}
