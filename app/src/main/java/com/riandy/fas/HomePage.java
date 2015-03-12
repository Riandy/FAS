package com.riandy.fas;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.riandy.fas.Alert.AlertDBHelper;
import com.riandy.fas.Alert.AlertModel;

import java.util.List;


public class HomePage extends Fragment {

    ListView listView;
    List<AlertModel> alertList;

    public HomePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        alertList = AlertDBHelper.getInstance(view.getContext()).getAlerts();
        listView = (ListView) view.findViewById(R.id.listView_alert);
        final AlertsArrayAdapter adapter = new AlertsArrayAdapter(view.getContext(), alertList);
        listView.setAdapter(adapter);

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
                        AlertDBHelper.getInstance(v.getContext()).deleteAlert(((AlertModel) adapter.getItem(positionToRemove)).id);
                        alertList.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
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


}
