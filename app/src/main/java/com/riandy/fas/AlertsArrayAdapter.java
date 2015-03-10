package com.riandy.fas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.riandy.fas.Alert.AlertModel;

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
        AlertModel data = list.get(position);
        startTime.setText(data.getAlertSpecs().getHourSpecs().getStartTime().toString(DateTimeFormat.forPattern("hh:mm:ss aaa")));
        name.setText(data.getAlertFeature().getName());

        return rowView;
    }
}
