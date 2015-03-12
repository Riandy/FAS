package com.riandy.fas;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.riandy.fas.Alert.AlertSpecs;
import com.riandy.fas.Alert.DaySpecs;
import com.riandy.fas.Alert.HourSpecs;

import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddAlertSpecs extends Fragment implements DatePickerFragment.OnDatePickerListener, TimePickerFragment.OnTimePickerListener{

    public static final int DIALOG_TIME_FRAGMENT = 123;
    public static final int DIALOG_DATE_FRAGMENT = 124;
    public static final String TAG_HOUR_OF_DAY = "hourOfDay";
    public static final String TAG_MINUTE = "minute";
    public static final String TAG_YEAR = "year";
    public static final String TAG_MONTH = "month";
    public static final String TAG_DAY = "day";

    Switch unlimitedDateRange;
    TextView startDate,endDate,startTime,endTime,startTimeOnce;
    ToggleButton[] dayOfWeek;
    EditText everyNDays,numOfTimeOrHour;
    RadioGroup radioGroupFreq, radioGroupFreqX;

    LinearLayout layoutOnce , layoutMoreThanOnce;
    private Calendar date;
    private Calendar time;
    boolean[] daySelected;
    View view;

    OnAddAlertSpecsListener callback;

    public interface OnAddAlertSpecsListener {
        public void onAddSpecsData(String tag, Object data);
    }

    public AddAlertSpecs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_alert_specs, container, false);

        try {
            callback = (OnAddAlertSpecsListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddAlertSpecsListener");
        }

        initialization();

        setupInitialValues(getArguments());

        unlimitedDateRange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_dateRange);
                if (isChecked) {
                    callback.onAddSpecsData(DaySpecs.TAG_DAYTYPE, DaySpecs.DayTypes.UNLIMITED.ordinal());
                    layout.setVisibility(View.GONE);
                } else {
                    callback.onAddSpecsData(DaySpecs.TAG_DAYTYPE, DaySpecs.DayTypes.DATERANGE.ordinal());
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog("startDate");
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog("endDate");
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog("startTime");
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog("endTime");
            }
        });

        dayOfWeek[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                daySelected[0] = isChecked;
                callback.onAddSpecsData(DaySpecs.TAG_DAYOFWEEK, daySelected);
            }
        });

        dayOfWeek[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                daySelected[1] = isChecked;
                callback.onAddSpecsData(DaySpecs.TAG_DAYOFWEEK, daySelected);
            }
        });

        dayOfWeek[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                daySelected[2] = isChecked;
                callback.onAddSpecsData(DaySpecs.TAG_DAYOFWEEK, daySelected);
            }
        });

        dayOfWeek[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                daySelected[3] = isChecked;
                callback.onAddSpecsData(DaySpecs.TAG_DAYOFWEEK, daySelected);
            }
        });

        dayOfWeek[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                daySelected[4] = isChecked;
                callback.onAddSpecsData(DaySpecs.TAG_DAYOFWEEK, daySelected);
            }
        });

        dayOfWeek[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                daySelected[5] = isChecked;
                callback.onAddSpecsData(DaySpecs.TAG_DAYOFWEEK, daySelected);
            }
        });

        dayOfWeek[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                daySelected[6] = isChecked;
                callback.onAddSpecsData(DaySpecs.TAG_DAYOFWEEK, daySelected);
            }
        });

        everyNDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString()!="")
                   callback.onAddSpecsData(DaySpecs.TAG_EVERYNDAYS, s.toString());
            }
        });

        radioGroupFreq.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton_once:
                        layoutOnce.setVisibility(View.VISIBLE);
                        layoutMoreThanOnce.setVisibility(View.GONE);
                        callback.onAddSpecsData(HourSpecs.TAG_HOURTYPE,HourSpecs.HourTypes.EXACTTIME.ordinal());
                        break;
                    case R.id.radioButton_moreThanOnce:
                        layoutOnce.setVisibility(View.GONE);
                        layoutMoreThanOnce.setVisibility(View.VISIBLE);
                        callback.onAddSpecsData(HourSpecs.TAG_HOURTYPE, HourSpecs.HourTypes.TIMERANGE.ordinal());
                        break;
                }
            }
        });

        startTimeOnce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog("startTimeOnce");
            }
        });

        radioGroupFreqX.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton_everyXHours:
                        callback.onAddSpecsData(HourSpecs.TAG_HOURTYPE, HourSpecs.HourTypes.TIMERANGE.ordinal());
                        break;
                    case R.id.radioButton_randomXTimes:
                        callback.onAddSpecsData(HourSpecs.TAG_HOURTYPE, HourSpecs.HourTypes.RANDOM.ordinal());
                        break;
                }
            }
        });

        numOfTimeOrHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                callback.onAddSpecsData(HourSpecs.TAG_INTERVAL_OR_NUMOFTIMES,s.toString());
            }
        });
        return view;

    }

    public void initialization(){
        unlimitedDateRange = (Switch) view.findViewById(R.id.switch_unlimited_date_range);
        startDate = (TextView) view.findViewById(R.id.textView_startDate);
        endDate = (TextView) view.findViewById(R.id.textView_endDate);
        startTime = (TextView) view.findViewById(R.id.textView_startTime);
        endTime = (TextView) view.findViewById(R.id.textView_endTime);
        startTimeOnce = (TextView) view.findViewById(R.id.textView_startTimeOnce);
        dayOfWeek = new ToggleButton[7];
        dayOfWeek[0] = (ToggleButton) view.findViewById(R.id.toggleButton_sun);
        dayOfWeek[1] = (ToggleButton) view.findViewById(R.id.toggleButton_mon);
        dayOfWeek[2] = (ToggleButton) view.findViewById(R.id.toggleButton_tue);
        dayOfWeek[3] = (ToggleButton) view.findViewById(R.id.toggleButton_wed);
        dayOfWeek[4] = (ToggleButton) view.findViewById(R.id.toggleButton_thu);
        dayOfWeek[5] = (ToggleButton) view.findViewById(R.id.toggleButton_fri);
        dayOfWeek[6] = (ToggleButton) view.findViewById(R.id.toggleButton_sat);
        everyNDays = (EditText) view.findViewById(R.id.editText_everyNDays);
        numOfTimeOrHour = (EditText) view.findViewById(R.id.editText_numOfTimes);
        radioGroupFreq = (RadioGroup) view.findViewById(R.id.radioGroup_frequency);
        radioGroupFreqX = (RadioGroup) view.findViewById(R.id.radioGroup_freqX);

        date = Calendar.getInstance();
        time = Calendar.getInstance();
        daySelected = new boolean[7];

        layoutOnce = (LinearLayout) view.findViewById(R.id.layout_once);
        layoutMoreThanOnce = (LinearLayout) view.findViewById(R.id.layout_moreThanOnce);
    }

    public void setupInitialValues(Bundle data){
        if(data==null)
            throw new NullPointerException("cannot setup values, bundle is null.");

        AlertSpecs specs = data.getParcelable(AlertSpecs.TAG_ALERT_SPECS);
        Log.d("specs","startTime "+specs.getHourSpecs().getStartTime().toString());

        unlimitedDateRange.setChecked(specs.getDaySpecs().getDayType() == DaySpecs.DayTypes.UNLIMITED);
        startDate.setText(specs.getDaySpecs().getStartDate().toString(DateTimeFormat.forPattern("EEE, d MMM yyyy")));
        endDate.setText(specs.getDaySpecs().getEndDate().toString(DateTimeFormat.forPattern("EEE, d MMM yyyy")));
        startTime.setText(specs.getHourSpecs().getStartTime().toString(DateTimeFormat.forPattern("hh:mm:ss aaa")));
        endTime.setText(specs.getHourSpecs().getEndTime().toString(DateTimeFormat.forPattern("hh:mm:ss aaa")));
        startTimeOnce.setText(specs.getHourSpecs().getStartTime().toString(DateTimeFormat.forPattern("hh:mm:ss aaa")));

        boolean[] arr = specs.getDaySpecs().getDayOfWeek();
        for(int i=0;i<7;i++){
            dayOfWeek[i].setChecked(arr[i]);
        }

        //TODO Fix this
        //everyNDays.setText(specs.getDaySpecs().getEveryNDays());
        if(specs.getHourSpecs().getHourType() == HourSpecs.HourTypes.TIMERANGE)
            numOfTimeOrHour.setText(specs.getHourSpecs().getIntervalInHour());
        else if(specs.getHourSpecs().getHourType() == HourSpecs.HourTypes.RANDOM)
            numOfTimeOrHour.setText(specs.getHourSpecs().getNumOfTimes());

        if(specs.getHourSpecs().getHourType() == HourSpecs.HourTypes.EXACTTIME) {
            ((RadioButton) view.findViewById(R.id.radioButton_once)).setChecked(true);
            layoutOnce.setVisibility(View.VISIBLE);
            layoutMoreThanOnce.setVisibility(View.GONE);
        }
        else {
            ((RadioButton) view.findViewById(R.id.radioButton_moreThanOnce)).setChecked(true);
            layoutOnce.setVisibility(View.GONE);
            layoutMoreThanOnce.setVisibility(View.VISIBLE);
        }
        if(specs.getHourSpecs().getHourType() == HourSpecs.HourTypes.RANDOM)
            ((RadioButton) view.findViewById(R.id.radioButton_randomXTimes)).setChecked(true);
        else if(specs.getHourSpecs().getHourType() == HourSpecs.HourTypes.TIMERANGE)
            ((RadioButton) view.findViewById(R.id.radioButton_everyXHours)).setChecked(true);

        /*
            Switch unlimitedDateRange;
    TextView startDate,endDate,startTime,endTime,startTimeOnce;
    ToggleButton[] dayOfWeek;
    EditText everyNDays,numOfTimeOrHour;
    RadioGroup radioGroupFreq, radioGroupFreqX;
         */
    }

    private void showTimeDialog(String type){
        Bundle args = new Bundle();
        args.putInt(TAG_HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        args.putInt(TAG_MINUTE,time.get(Calendar.MINUTE));
        args.putString(TimePickerFragment.TAG_TIMETYPE,type);

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setTargetFragment(this,DIALOG_TIME_FRAGMENT);
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    private void showDateDialog(String type){
        Bundle args = new Bundle();
        args.putInt(TAG_YEAR, date.get(Calendar.YEAR));
        args.putInt(TAG_MONTH, date.get(Calendar.MONTH));
        args.putInt(TAG_DAY, date.get(Calendar.DAY_OF_MONTH));
        args.putString(DatePickerFragment.TAG_DATETYPE,type);

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(this,DIALOG_DATE_FRAGMENT);
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(),"datePicker");
    }

    @Override
    public void onAddDateSubmit(int year, int month, int day, String tag){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        date.set(year, month, day);

        switch (tag){
              case "startDate":
                  startDate.setText("Start date : "+dateFormat.format(date.getTime()));
                  callback.onAddSpecsData(DaySpecs.TAG_STARTDATE, date);
                  break;
              case "endDate":
                  endDate.setText("End date : "+dateFormat.format(date.getTime()));
                  callback.onAddSpecsData(DaySpecs.TAG_ENDDATE,date);
                  break;
        }
    }

    @Override
    public void onAddTimeSubmit(int hourOfDay, int minute, String tag) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aaa");
        time.set(0,0,0,hourOfDay,minute);

        switch(tag){
            case "startTime":
                startTime.setText(dateFormat.format(time.getTime()));
                callback.onAddSpecsData(HourSpecs.TAG_STARTTIME,time);
                break;
            case "endTime":
                endTime.setText(dateFormat.format(time.getTime()));
                callback.onAddSpecsData(HourSpecs.TAG_ENDTIME,time);
                break;
            case "startTimeOnce":
                startTimeOnce.setText(dateFormat.format(time.getTime()));
                callback.onAddSpecsData(HourSpecs.TAG_STARTTIME,time);
                break;
        }
    }
}
