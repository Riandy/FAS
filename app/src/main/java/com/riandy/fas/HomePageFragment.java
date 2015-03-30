package com.riandy.fas;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.riandy.fas.Alert.AlertDBHelper;
import com.riandy.fas.Alert.AlertManagerHelper;
import com.riandy.fas.Alert.AlertModel;
import com.riandy.fas.Alert.DaySpecs;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.List;


public class HomePageFragment extends Fragment {

    ListView listView;
    TextView dateToShow;
    CheckBox showAllAlerts;
    View view;
    List<AlertModel> alertList;
    LocalDate selectedDate;
    AlertsArrayAdapter adapter;

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_page, container, false);

        dateToShow = (TextView) view.findViewById(R.id.textView_dateToShow);
        listView = (ListView) view.findViewById(R.id.listView_alert);
        showAllAlerts = (CheckBox) view.findViewById(R.id.checkBox_showAllAlerts);

        showAllAlerts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    showAlert(null);
                }else{
                    showAlert(selectedDate);
                }
            }
        });
        selectedDate = new LocalDate();

        dateToShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout ll= (LinearLayout)inflater.inflate(R.layout.calendar_view, null, false);
                CalendarView cv = (CalendarView) ll.getChildAt(0);
                selectedDate = new LocalDate();
                cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month,
                                                    int dayOfMonth) {
                        selectedDate= new LocalDate(year,month+1,dayOfMonth);
                    }
                });
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Event Calendar")
                        .setView(ll)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dateToShow.setText(selectedDate.toString(DateTimeFormat.forPattern("EEE, d MMM yyyy")));
                                showAlert(selectedDate);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Do nothing.
                            }
                        }
                ).show();
            }
        });

        alertList = AlertDBHelper.getInstance(view.getContext()).getAlerts();
        adapter = new AlertsArrayAdapter(view.getContext(), alertList);
        listView.setAdapter(adapter);
        showAlert(selectedDate);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final View v = view;
                AlertDialog.Builder adb = new AlertDialog.Builder(view.getContext());
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + position);
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel the alert first

                        AlertManagerHelper.cancelAlert(v.getContext(),(AlertModel) adapter.getItem(positionToRemove));
                        AlertDBHelper.getInstance(v.getContext()).deleteAlert(((AlertModel) adapter.getItem(positionToRemove)).id);
                        alertList.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                        AlertManagerHelper.setAlerts(v.getContext());
                    }
                });
                adb.show();
                return false;

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle data = new Bundle();
                data.putParcelable(AlertModel.TAG_ALERT_MODEL,alertList.get(position));
                Fragment fragment = new AddAlert();
                fragment.setArguments(data);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).
                        commit();
            }
        });
        return view;
    }


    public void showAlert(LocalDate date){
        List<AlertModel> tempList = AlertDBHelper.getInstance(view.getContext()).getAlerts();
        if(alertList!=null)
            alertList.clear();
        for(AlertModel model:tempList){
            if(date==null||model.getAlertSpecs().getDaySpecs().getDayType()== DaySpecs.DayTypes.UNLIMITED||
                    model.getAlertSpecs().getDaySpecs().getStartDate().equals(date)){
                alertList.add(model);

            }
        }
        TextView message = (TextView) view.findViewById(R.id.textView_noAlertsScheduled);

        if(alertList.isEmpty()){
            message.setVisibility(View.VISIBLE);
        }else{
            message.setVisibility(View.GONE);
        }
        listView.setAdapter(adapter);
    }
}
