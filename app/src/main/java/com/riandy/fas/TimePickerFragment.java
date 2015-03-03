package com.riandy.fas;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private OnTimePickerListener callback;
    private int hour,minute;

    public static final String TAG_HOUR_OF_DAY = "hourOfDay";
    public static final String TAG_MINUTE = "minute";

    public interface OnTimePickerListener {
        public void onAddTimeSubmit(int hourOfDay, int minute);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (OnTimePickerListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnTimePickerListener");
        }


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = (int) getArguments().get(TAG_HOUR_OF_DAY);
        int minute = (int) getArguments().get(TAG_MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        if(callback!=null)
            callback.onAddTimeSubmit(hourOfDay,+minute);
    }
}