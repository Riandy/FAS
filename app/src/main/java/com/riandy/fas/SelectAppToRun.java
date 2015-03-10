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

    OnSelectAppListener callback;

    public SelectAppToRun() {
        // Required empty public constructor
    }


    public interface OnSelectAppListener {
        public void onSelectApp(String tag, String data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            callback = (OnSelectAppListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnSelectAppListener");
        }

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

                callback.onSelectApp("appSelected",appList.get(position).getAppname());
                getFragmentManager().popBackStackImmediate();
            }
        });

        return view;
    }


}
