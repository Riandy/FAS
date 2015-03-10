package com.riandy.fas;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riandy.fas.Alert.AlertModel;
import com.riandy.fas.Alert.DaySpecs;
import com.riandy.fas.Alert.HourSpecs;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddAlertOneOffEvent extends Fragment implements DatePickerFragment.OnDatePickerListener, TimePickerFragment.OnTimePickerListener{

    private TextView startTime;
    private TextView startDate;
    private Calendar date;
    private Calendar time;

    private DatePickerDialog fromDatePickerDialog;

    public static final int DIALOG_TIME_FRAGMENT = 123;
    public static final int DIALOG_DATE_FRAGMENT = 124;
    public static final String TAG_HOUR_OF_DAY = "hourOfDay";
    public static final String TAG_MINUTE = "minute";
    public static final String TAG_YEAR = "year";
    public static final String TAG_MONTH = "month";
    public static final String TAG_DAY = "day";

    OnAddAlertOneOffEventListener callback;

    public interface OnAddAlertOneOffEventListener {
        public void onAddSpecsData(String tag, Object data);
    }

    public AddAlertOneOffEvent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            callback = (OnAddAlertOneOffEventListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddAlertOneOffEventListener");
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_alert_one_off_event, container, false);

        date = Calendar.getInstance();
        time = Calendar.getInstance();

        startTime = (TextView) view.findViewById(R.id.startTime);
        startDate = (TextView) view.findViewById(R.id.startDate);

        setupInitialValues(getArguments());

        startTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        return view;
    }

    private void showTimeDialog(){
        Bundle args = new Bundle();
        args.putInt(TAG_HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        args.putInt(TAG_MINUTE,time.get(Calendar.MINUTE));

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setTargetFragment(this,DIALOG_TIME_FRAGMENT);
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    private void showDateDialog(){
        Bundle args = new Bundle();
        args.putInt(TAG_YEAR, date.get(Calendar.YEAR));
        args.putInt(TAG_MONTH, date.get(Calendar.MONTH));
        args.putInt(TAG_DAY, date.get(Calendar.DAY_OF_MONTH));

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(this,DIALOG_DATE_FRAGMENT);
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(),"datePicker");
    }

    //package all the info and then send it to the parent activity
    private void saveAlert(){
        AlertModel model = new AlertModel();
        model.setEnabled(true);
    }

    @Override
    public void onAddDateSubmit(int year, int month, int day){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        date.set(year,month,day);
        startDate.setText(dateFormat.format(date.getTime()));
        callback.onAddSpecsData(DaySpecs.TAG_STARTDATE,date);
    }

    @Override
    public void onAddTimeSubmit(int hourOfDay, int minute) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss aaa");
        time.set(0,0,0,hourOfDay,minute);
        startTime.setText(dateFormat.format(time.getTime()));
        Log.d("Change Time to",time.getTime().toString());
        callback.onAddSpecsData(HourSpecs.TAG_STARTTIME,time);
    }

    private void setupInitialValues(Bundle data){
        if(data==null)
            throw new NullPointerException("cannot setup values, bundle is null.");
        startDate.setText(data.getString(DaySpecs.TAG_STARTDATE));
        startTime.setText(data.getString(HourSpecs.TAG_STARTTIME));
    }
}
