package com.riandy.fas;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.riandy.fas.Alert.PInfo;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectAppToRun extends Fragment {


    public SelectAppToRun() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_app_to_run, container, false);

        PInfo pInfo = new PInfo(view.getContext());
        final List<PInfo> appList = pInfo.getPackages();

        ListView appListView = (ListView) view.findViewById(R.id.apps_listView);
        AppsArrayAdapter adapter = new AppsArrayAdapter(view.getContext(),appList);
        appListView.setAdapter(adapter);

        appListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                getFragmentManager().popBackStackImmediate();
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("appSelected", appList.get(position).getAppname());
//                setResult(RESULT_OK,returnIntent);
//                finish();
            }
        });

        return view;
    }


}
