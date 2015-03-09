package com.riandy.fas;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.riandy.fas.Alert.AlertFeature;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAlertFeature extends Fragment {


    public static final String TAG_SOUND_SWITCH = "soundSwitch";
    public static final String TAG_NOTIF_SWITCH = "notifSwitch";
    public static final String TAG_LAUNCH_APP_SWITCH = "launchAppSwitch";
    public static final String TAG_VIBRATE_SWITCH = "vibrateSwitch";
    public static final String TAG_VOICE_SWITCH = "voiceSwitch";
    public static final String TAG_SOUND_URL = "soundURL";
    public static final String TAG_APP_SELECTED = "appSelected";


    private Switch soundSwitch, notificationSwitch, launchAppSwitch, vibrateSwitch, voiceInstructionSwitch;
    private Button selectSound, selectApp;
    private TextView soundURL, appSelected;

    AlertFeature data;
    OnAddAlertFeatureListener callback;

    public interface OnAddAlertFeatureListener {
        public void onAddFeatureData(String tag, Object data);
    }

    public AddAlertFeature() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        try {
            callback = (OnAddAlertFeatureListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddAlertFeatureListener");
        }

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_alert_feature, container, false);

        data = new AlertFeature();
        soundSwitch = (Switch) view.findViewById(R.id.switch_sound);
        notificationSwitch = (Switch) view.findViewById(R.id.switch_notification);
        launchAppSwitch = (Switch) view.findViewById(R.id.switch_launch_app);
        vibrateSwitch = (Switch) view.findViewById(R.id.switch_vibrate);
        voiceInstructionSwitch = (Switch) view.findViewById(R.id.switch_voice_instruction);
        selectSound = (Button) view.findViewById(R.id.button_select_sound);
        selectApp = (Button) view.findViewById(R.id.button_select_app);
        soundURL = (TextView) view.findViewById(R.id.sound_url);
        appSelected = (TextView) view.findViewById(R.id.app_selected);

        if(getArguments()!=null){
            Log.d("Riandy","setting up initial values");
            setupInitialValues(getArguments());
        }else{
            Log.d("Bundle","is null");
        }

        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout soundLayout = (LinearLayout) view.findViewById(R.id.sound_selected_layout);
                callback.onAddFeatureData(TAG_SOUND_SWITCH,isChecked);
                if (isChecked) {
                    soundLayout.setVisibility(View.VISIBLE);
                } else {
                    soundLayout.setVisibility(View.GONE);
                }
                data.setSoundEnabled(isChecked);
            }
        });

        launchAppSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout launchAppLayout = (LinearLayout) view.findViewById(R.id.launch_app_layout);
                callback.onAddFeatureData(TAG_LAUNCH_APP_SWITCH,isChecked);
                if (isChecked) {
                    launchAppLayout.setVisibility(View.VISIBLE);
                } else {
                    launchAppLayout.setVisibility(View.GONE);
                }
                data.setLaunchAppEnabled(isChecked);
            }
        });

        vibrateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callback.onAddFeatureData(TAG_VIBRATE_SWITCH,isChecked);
                data.setVibrationEnabled(isChecked);
            }
        });

        voiceInstructionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callback.onAddFeatureData(TAG_VOICE_SWITCH,isChecked);
                data.setVoiceInstructionStatusEnabled(isChecked);
            }
        });

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callback.onAddFeatureData(TAG_NOTIF_SWITCH,isChecked);
                data.setNotificationEnabled(isChecked);
            }
        });

        selectSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundURL.setText("");
                callback.onAddFeatureData(TAG_SOUND_URL,"null");
            }
        });

        selectApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSelected.setText("");
                callback.onAddFeatureData(TAG_APP_SELECTED,"null");
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new SelectAppToRun()).addToBackStack(null).commit();
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("RIANDY","I AM SAVED");
        outState.putBoolean(TAG_SOUND_SWITCH,soundSwitch.isChecked());
        outState.putBoolean(TAG_NOTIF_SWITCH,notificationSwitch.isChecked());
        outState.putBoolean(TAG_LAUNCH_APP_SWITCH,launchAppSwitch.isChecked());
        outState.putBoolean(TAG_VIBRATE_SWITCH,vibrateSwitch.isChecked());
        outState.putBoolean(TAG_VOICE_SWITCH,voiceInstructionSwitch.isChecked());
        outState.putString(TAG_SOUND_URL,soundURL.getText().toString());
        outState.putString(TAG_APP_SELECTED,appSelected.getText().toString());
    }

    private void setupInitialValues(Bundle data){
        soundSwitch.setChecked(data.getBoolean(TAG_SOUND_SWITCH));
        notificationSwitch.setChecked(data.getBoolean(TAG_NOTIF_SWITCH));
        launchAppSwitch.setChecked(data.getBoolean(TAG_LAUNCH_APP_SWITCH));
        vibrateSwitch.setChecked(data.getBoolean(TAG_VIBRATE_SWITCH));
        voiceInstructionSwitch.setChecked(data.getBoolean(TAG_VOICE_SWITCH));
        soundURL.setText(data.getString(TAG_SOUND_URL));
        appSelected.setText(data.getString(TAG_APP_SELECTED));
    }

    public AlertFeature getData(){
        return data;
    }
}
