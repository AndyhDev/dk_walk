package com.dk.walk.fragments;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.walk.R;
import com.dk.walk.database.SQLWay;
import com.dk.walk.database.SQLiteDataSource;
import com.dk.walk.service.GPSservice;
import com.dk.walk.util.Helper;
import com.dk.walk.util.ServiceFragment;

public class StatisticOverviewFragment extends ServiceFragment {
	private static final String TAG = "StatisticOverviewFragment";
	
	private RelativeLayout layout;
	private Context context;
	private GPSservice service;
	private TextView totalWay;
	private TextView totalSteps;
	private TextView totalCal;
	private TextView totalDuration;
	private TextView totalWays;
	
	private DecimalFormat format = new DecimalFormat("0.00");
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.statistic_overview, container, false);
		context = getActivity();
		
		totalWay = (TextView) layout.findViewById(R.id.total_way);
		totalSteps = (TextView) layout.findViewById(R.id.total_steps);
		totalCal = (TextView) layout.findViewById(R.id.total_cal);
		totalDuration = (TextView) layout.findViewById(R.id.total_duration);
		totalWays = (TextView) layout.findViewById(R.id.total_ways);
		
		refresh();
		
		return layout;
		
	}
	
	private void refresh(){
		SQLiteDataSource datasource = new SQLiteDataSource(context);
		datasource.open();
		List<SQLWay> ways = datasource.getAllSQLWays();
		datasource.close();
		
		Log.d(TAG, "size:" + ways.size());
		Float gWay = 0f;
		Integer gSteps = 0;
		Integer gCal = 0;
		Integer gTime = 0;
		
		SQLWay way;
		for(int i = 0; i < ways.size(); i++){
			way = ways.get(i);
			gWay += way.getWay();
			gSteps += way.getSteps();
			gCal += way.getCalories();
			gTime += way.getTime();
		}
		
		totalWay.setText(format.format(gWay) + " m");
		totalSteps.setText(String.valueOf(gSteps));
		totalCal.setText(String.valueOf(gCal));
		totalDuration.setText(Helper.formatTime(gTime));
		totalWays.setText(String.valueOf(ways.size()));
	}
	@Override
	public void onNewLocation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewSteps() {
		Log.d(TAG, "new Step");
		
	}
	
	@Override
	public void onNewWay() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onServiceDisconnected() {
		service = null;
		
	}
	
	@Override
	public void onServiceConnected() {
		service = getService();
		refresh();
	}

	@Override
	public void onUpdateTime() {
		// TODO Auto-generated method stub
		
	}
}
