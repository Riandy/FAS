package com.riandy.fas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.riandy.fas.Alert.AlertDBHelper;
import com.riandy.fas.Alert.AlertManagerHelper;
import com.riandy.fas.Alert.AlertModel;
import com.riandy.fas.Alert.HourSpecs;

import org.joda.time.format.DateTimeFormat;

import java.util.List;

/**
 * Created by Riandy on 10/3/15.
 */
public class AlertsArrayAdapter extends ArrayAdapter {

    private final Context context;
    private List<AlertModel> list;

    public AlertsArrayAdapter(Context context, List<AlertModel> list){
        super(context, R.layout.alerts_rowlayout,list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.alerts_rowlayout, parent, false);

        TextView startTime = (TextView) rowView.findViewById(R.id.textView_alert_time);
        TextView name = (TextView) rowView.findViewById(R.id.textView_alert_name);
        ToggleButton enabled = (ToggleButton) rowView.findViewById(R.id.toggleButton_enabled);

        final AlertModel data = list.get(position);
        //Log.d("alertsArrayAdapter alert ",data.toString());
        if(data.getAlertSpecs().getHourSpecs().getHourType()== HourSpecs.HourTypes.EXACTTIME)
            startTime.setText(data.getAlertSpecs().getHourSpecs().getStartTime().toString(DateTimeFormat.forPattern("hh:mm aaa")));
        else
            startTime.setText(data.getAlertSpecs().getHourSpecs().getLastAlertTime().toString(DateTimeFormat.forPattern("hh:mm aaa")));
        name.setText(data.getAlertFeature().getName());
        enabled.setChecked(data.isEnabled());
        enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlertManagerHelper.cancelAlert(context,data);
                data.setEnabled(isChecked);
                AlertDBHelper db = new AlertDBHelper(context);
                db.updateAlert(data);
                Log.d("alert",data.toString());
                AlertManagerHelper.setAlerts(context);
            }
        });
        return rowView;
    }

    @Override
    public int getCount(){
        if(list==null)
            return 0;
        else
            return list.size();
    }
}
