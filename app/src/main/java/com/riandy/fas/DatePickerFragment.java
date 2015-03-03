package com.riandy.fas;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;


public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener{

    public static final String TAG_YEAR = "year";
    public static final String TAG_MONTH = "month";
    public static final String TAG_DAY = "day";

    private OnDatePickerListener callback;

    public interface OnDatePickerListener {
        public void onAddDateSubmit(int year, int month, int day);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (OnDatePickerListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnDatePickerListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = (int) getArguments().get(TAG_YEAR);
        int month = (int) getArguments().get(TAG_MONTH);
        int day = (int) getArguments().get(TAG_DAY);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if(callback!=null)
            callback.onAddDateSubmit(year,month,day);
    }

}
