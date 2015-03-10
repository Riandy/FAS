package com.riandy.fas;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.riandy.fas.Alert.AlertDBHelper;
import com.riandy.fas.Alert.AlertFeature;
import com.riandy.fas.Alert.AlertManagerHelper;
import com.riandy.fas.Alert.AlertModel;
import com.riandy.fas.Alert.DaySpecs;
import com.riandy.fas.Alert.HourSpecs;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Calendar;


public class AddAlert extends Fragment implements AddAlertFeature.OnAddAlertFeatureListener, AddAlertOneOffEvent.OnAddAlertOneOffEventListener{

    private static final int ADD_FEATURE_FRAGMENT = 1234;
    private static final int ADD_SPECS_FRAGMENT = 1222;

    Switch customizedFeature,customizedSpecs;
    Button saveAlert;
    EditText alertName,alertDescription;

    AlertModel alert;
    Fragment me;

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


        final Fragment addAlertSpecs = new AddAlertOneOffEvent();
        addAlertSpecs.setTargetFragment(this, ADD_SPECS_FRAGMENT);
        addAlertSpecs.setArguments(getAddSpecsBundle());
        getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_specs, addAlertSpecs).commit();

        Fragment addAlertFeature = new AddAlertFeature();
        addAlertFeature.setTargetFragment(this, ADD_FEATURE_FRAGMENT);
        addAlertFeature.setArguments(getAddFeatureBundle());
        getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_feature, addAlertFeature).commit();

        //refactor this later
        me = this;

        customizedFeature = (Switch) view.findViewById(R.id.switch_customize_alert_feature);
        saveAlert = (Button) view.findViewById(R.id.button_save_alert);
        customizedSpecs = (Switch) view.findViewById(R.id.switch_alert_specs);
        alertName = (EditText) view.findViewById(R.id.editText_alert_name);
        alertDescription = (EditText) view.findViewById(R.id.editText_alert_desciption);

        if(customizedFeature.isChecked())
            view.findViewById(R.id.fragment_container_add_alert_feature).setVisibility(View.VISIBLE);
        else
            view.findViewById(R.id.fragment_container_add_alert_feature).setVisibility(View.GONE);

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

        customizedSpecs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    addAlertSpecs.setArguments(getAddSpecsBundle());
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_specs, addAlertSpecs).commit();
                }else{
                    getFragmentManager().beginTransaction().remove(addAlertSpecs).commit();
                }
            }
        });
        saveAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.getAlertFeature().setName(alertName.getText().toString());
                alert.getAlertFeature().setDescription(alertDescription.getText().toString());
                AlertDBHelper db = AlertDBHelper.getInstance(view.getContext());
                long id = db.createAlert(alert);
                AlertManagerHelper.setAlerts(view.getContext());
            }
        });

        return view;
    }

    @Override
    public void onAddFeatureData(String tag, Object data) {
        switch (tag){
            case AlertFeature.TAG_APP_SELECTED:
                alert.getAlertFeature().setAppToLaunch((String)data);
                break;
            case AlertFeature.TAG_LAUNCH_APP_SWITCH:
                alert.getAlertFeature().setLaunchAppEnabled((boolean)data);
                break;
            case AlertFeature.TAG_NOTIF_SWITCH:
                alert.getAlertFeature().setNotificationEnabled((boolean)data);
                break;
            case AlertFeature.TAG_SOUND_SWITCH:
                alert.getAlertFeature().setSoundEnabled((boolean)data);
                break;
            case AlertFeature.TAG_SOUND_URL:
                alert.getAlertFeature().setTone((String)data);
                break;
            case AlertFeature.TAG_VIBRATE_SWITCH:
                alert.getAlertFeature().setVibrationEnabled((boolean)data);
                break;
            case AlertFeature.TAG_VOICE_SWITCH:
                alert.getAlertFeature().setVoiceInstructionStatusEnabled((boolean)data);
                break;


        }
    }

    @Override
    public void onAddSpecsData(String tag, Object data) {
        switch (tag){
            case DaySpecs.TAG_STARTDATE:
                LocalDate date = new LocalDate(((Calendar)data).get(Calendar.YEAR),((Calendar)data).get(Calendar.MONTH)+1,((Calendar)data).get(Calendar.DAY_OF_MONTH));
                alert.getAlertSpecs().getDaySpecs().setDate(date);
                Log.d("Stored",alert.getAlertSpecs().getDaySpecs().getStartDate().toString());
                break;
            case DaySpecs.TAG_ENDDATE:
                break;
            case HourSpecs.TAG_STARTTIME:
                LocalTime time  = new LocalTime(((Calendar)data).get(Calendar.HOUR_OF_DAY),((Calendar)data).get(Calendar.MINUTE));
                alert.getAlertSpecs().getHourSpecs().setExactTime(time);
                break;
            case HourSpecs.TAG_ENDTIME:
                break;
        }
    }

    public Bundle getAddFeatureBundle() {
        Bundle data = new Bundle();
        data.putBoolean(AlertFeature.TAG_SOUND_SWITCH, alert.getAlertFeature().isSoundEnabled());
        data.putBoolean(AlertFeature.TAG_NOTIF_SWITCH,alert.getAlertFeature().isNotificationEnabled());
        data.putBoolean(AlertFeature.TAG_LAUNCH_APP_SWITCH,alert.getAlertFeature().isLaunchAppEnabled());
        data.putBoolean(AlertFeature.TAG_VIBRATE_SWITCH,alert.getAlertFeature().isVibrationEnabled());
        data.putBoolean(AlertFeature.TAG_VOICE_SWITCH,alert.getAlertFeature().isVoiceInstructionStatusEnabled());
        data.putString(AlertFeature.TAG_SOUND_URL,alert.getAlertFeature().getTone());
        data.putString(AlertFeature.TAG_APP_SELECTED,alert.getAlertFeature().getAppToLaunch());
        return data;
    }

    public Bundle getAddSpecsBundle() {
        Bundle data = new Bundle();
        data.putString(DaySpecs.TAG_STARTDATE, alert.getAlertSpecs().getDaySpecs().getStartDate().toString(DateTimeFormat.forPattern("EEE, d MMM yyyy")));
        data.putString(HourSpecs.TAG_STARTTIME, alert.getAlertSpecs().getHourSpecs().getStartTime().toString(DateTimeFormat.forPattern("hh:mm:ss aaa")));
        return data;
    }
}
