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
import android.widget.Toast;

import com.riandy.fas.Alert.AlertDBHelper;
import com.riandy.fas.Alert.AlertFeature;
import com.riandy.fas.Alert.AlertManagerHelper;
import com.riandy.fas.Alert.AlertModel;
import com.riandy.fas.Alert.AlertSpecs;
import com.riandy.fas.Alert.DaySpecs;
import com.riandy.fas.Alert.HourSpecs;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;


public class AddAlert extends Fragment implements AddAlertFeature.OnAddAlertFeatureListener,
        AddAlertOneOffEvent.OnAddAlertOneOffEventListener ,AddAlertSpecs.OnAddAlertSpecsListener{

    private static final int ADD_FEATURE_FRAGMENT = 1234;
    private static final int ADD_SPECS_FRAGMENT = 1222;
    public static final String TAG_IS_NEW_ALERT = "newAlert";

    boolean isNewAlert;

    Switch customizedFeature,customizedSpecs;
    Button saveAlert;
    EditText alertName,alertDescription;

    AlertModel alert;
    Fragment me;
    Fragment addAlertSpecsOneOff;
    Fragment addAlertSpecs,addAlertFeature;

    public AddAlert() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_alert, container, false);

        if(getArguments()!=null){
            Log.d("alert model","received");
            alert = getArguments().getParcelable(AlertModel.TAG_ALERT_MODEL);
            isNewAlert = false;
        }

        if(alert==null){
            alert = new AlertModel();
            isNewAlert = true;
            Log.d("DEBUG","new alert model created");
        }

        //refactor this later
        me = this;

        customizedFeature = (Switch) view.findViewById(R.id.switch_customize_alert_feature);
        saveAlert = (Button) view.findViewById(R.id.button_save_alert);
        customizedSpecs = (Switch) view.findViewById(R.id.switch_alert_specs);
        alertName = (EditText) view.findViewById(R.id.editText_alert_name);
        alertDescription = (EditText) view.findViewById(R.id.editText_alert_desciption);

        addAlertSpecsOneOff = new AddAlertOneOffEvent();
        addAlertSpecsOneOff.setTargetFragment(this, ADD_SPECS_FRAGMENT);

        addAlertSpecs = new AddAlertSpecs();
        addAlertSpecs.setTargetFragment(this,ADD_SPECS_FRAGMENT);

        addAlertFeature = new AddAlertFeature();

        setupInitialValues();

//        addAlertSpecsOneOff.setArguments(getAddSpecsBundle());
//        getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_specs, addAlertSpecsOneOff).commit();
//
//        addAlertSpecs.setArguments(getAddSpecsBundle());

        addAlertFeature.setTargetFragment(this, ADD_FEATURE_FRAGMENT);
        addAlertFeature.setArguments(getAddFeatureBundle());
        getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_feature, addAlertFeature).commit();

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
                    addAlertSpecsOneOff.setArguments(getAddSpecsBundle());
                    alert.getAlertSpecs().getDaySpecs().setDayType(DaySpecs.DayTypes.DATEONLY);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_specs, addAlertSpecsOneOff).commit();
                }else{
                    addAlertSpecs.setArguments(getAddSpecsBundle());
                    alert.getAlertSpecs().getDaySpecs().setDayType(DaySpecs.DayTypes.DATERANGE);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_specs, addAlertSpecs).commit();
                }
            }
        });
        saveAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.getAlertFeature().setName(alertName.getText().toString());
                alert.getAlertFeature().setDescription(alertDescription.getText().toString());
                AlertDBHelper db = AlertDBHelper.getInstance(view.getContext());
                //Log.d("AddAlert alert",alert.toString());
                long id;
                if(alert.getAlertSpecs().getDaySpecs().getStartDate().isAfter(alert.getAlertSpecs().getDaySpecs().getEndDate())){
                    Toast.makeText(view.getContext(),"start date cannot be later than end date",Toast.LENGTH_SHORT).show();
                }else if(alert.getAlertSpecs().getHourSpecs().getStartTime().isAfter(alert.getAlertSpecs().getHourSpecs().getEndTime())){
                    Toast.makeText(view.getContext(),"start time cannot be later than end time",Toast.LENGTH_SHORT).show();
                }else {
                    if (isNewAlert) {
                        //save it. create new alert
                        id = db.createAlert(alert);
                        Log.d("new alert", "" + id + " created");
                    } else {
                        //save only the changes.
                        id = db.updateAlert(alert);
                        Log.d("update alert", "" + id + " updated");
                    }
                    //AlertManagerHelper.setAlerts(view.getContext());
                    AlertDBHelper dbHelper = new AlertDBHelper(view.getContext());
                    AlertManagerHelper.setAlert(view.getContext(),dbHelper.getAlert(id));
                    getFragmentManager().popBackStack();
                }
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
                alert.getAlertFeature().setTone(data.toString());
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
        Log.d("CHANGING",tag+ " " + data.toString());

        switch (tag){
            case DaySpecs.TAG_STARTDATE:
                LocalDate date = new LocalDate(((Calendar)data).get(Calendar.YEAR),((Calendar)data).get(Calendar.MONTH)+1,((Calendar)data).get(Calendar.DAY_OF_MONTH));
                alert.getAlertSpecs().getDaySpecs().setStartDate(date);
                break;
            case DaySpecs.TAG_ENDDATE:
                date = new LocalDate(((Calendar)data).get(Calendar.YEAR),((Calendar)data).get(Calendar.MONTH)+1,((Calendar)data).get(Calendar.DAY_OF_MONTH));
                alert.getAlertSpecs().getDaySpecs().setEndDate(date);
                break;
            case HourSpecs.TAG_STARTTIME:
                LocalTime time  = new LocalTime(((Calendar)data).get(Calendar.HOUR_OF_DAY),((Calendar)data).get(Calendar.MINUTE),0);
                alert.getAlertSpecs().getHourSpecs().setExactTime(time);
                break;
            case HourSpecs.TAG_ENDTIME:
                time  = new LocalTime(((Calendar)data).get(Calendar.HOUR_OF_DAY),((Calendar)data).get(Calendar.MINUTE),0);
                alert.getAlertSpecs().getHourSpecs().setEndTime(time);
                break;
            case DaySpecs.TAG_DAYOFWEEK:
                alert.getAlertSpecs().getDaySpecs().setDayOfWeek((boolean[]) data);
                break;
            case DaySpecs.TAG_DAYTYPE:
                Log.d("day type","HELLO "+data.toString());
                alert.getAlertSpecs().getDaySpecs().setDayType(DaySpecs.DayTypes.values()[(int)data]);
                break;
            case DaySpecs.TAG_EVERYNDAYS:
                //TODO fix this
                alert.getAlertSpecs().getDaySpecs().setEveryNDays(Integer.parseInt((String)data));
                break;
            case HourSpecs.TAG_HOURTYPE:
                alert.getAlertSpecs().getHourSpecs().setHourType(HourSpecs.HourTypes.values()[(int)data]);
                break;
            case HourSpecs.TAG_INTERVAL_OR_NUMOFTIMES:
                //TODO fix this
                if(!data.equals("")) {
                    if (alert.getAlertSpecs().getHourSpecs().getHourType() == HourSpecs.HourTypes.TIMERANGE)
                        alert.getAlertSpecs().getHourSpecs().setIntervalInHour(Integer.parseInt((String) data));
                    else
                        alert.getAlertSpecs().getHourSpecs().setNumOfTimes(Integer.parseInt((String) data));
                }
            default:
                Log.d("Not done",tag);
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
        data.putParcelable(AlertSpecs.TAG_ALERT_SPECS,alert.getAlertSpecs());
        return data;
    }

    public void setupInitialValues() {
        alertName.setText(alert.getAlertFeature().getName());
        alertDescription.setText(alert.getAlertFeature().getDescription());

        if(alert.getAlertSpecs().getDaySpecs().getDayType() == DaySpecs.DayTypes.DATEONLY){
            Log.d("note","date only");
            customizedSpecs.setChecked(true);
            addAlertSpecsOneOff.setArguments(getAddSpecsBundle());
            getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_specs, addAlertSpecsOneOff).commit();

        }else{
            Log.d("note","date range");
            customizedSpecs.setChecked(false);
            addAlertSpecs.setArguments(getAddSpecsBundle());
            getFragmentManager().beginTransaction().replace(R.id.fragment_container_add_alert_specs, addAlertSpecs).commit();
        }

    }
}
