package com.dk.walk.fragments;

import java.util.List;

import com.dk.walk.R;
import com.dk.walk.database.SQLWay;
import com.dk.walk.database.SQLiteDataSource;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LastWayFragment extends Fragment{
private static final String TAG = "LastWayFragment";
	
	private RelativeLayout layout;
	private TextView way;
	private TextView steps;
	private TextView cal;
	private TextView speed;
	private TextView duration;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.last_way, container, false);
		
		way = (TextView) layout.findViewById(R.id.way);
		steps = (TextView) layout.findViewById(R.id.steps);
		cal = (TextView) layout.findViewById(R.id.cal);
		speed = (TextView) layout.findViewById(R.id.speed);
		duration = (TextView) layout.findViewById(R.id.duration);
		return layout;
	}
	
	public void refresh(){
		SQLiteDataSource datasource = new SQLiteDataSource(getActivity());
		datasource.open();
		SQLWay way = datasource.getLastWay();
		datasource.close();
		if(way != null){
			this.way.setText(way.getFormatedWay());
			steps.setText(way.getSteps().toString());
			cal.setText(way.getCalories().toString());
			speed.setText(way.getFormatedSpeed());
			duration.setText(way.getFormatedTime());
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		refresh();
	}
}
