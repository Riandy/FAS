package com.riandy.fas;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.riandy.fas.Alert.PInfo;

import java.util.List;

public class AppsArrayAdapter extends ArrayAdapter<PInfo> {
	private final Context context;
	private List<PInfo> list;

	public AppsArrayAdapter(Context context, List<PInfo> list){
		super(context, R.layout.apps_rowlayout,list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.apps_rowlayout, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.apps_row_textview);
		textView.setText(list.get(position).getAppname());
		Drawable d = list.get(position).getIcon();
		d.setBounds(0, 0, 50, 50);
		textView.setCompoundDrawables(d, null, null, null);
		return rowView;		
	}


}
