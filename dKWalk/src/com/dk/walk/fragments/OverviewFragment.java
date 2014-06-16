package com.dk.walk.fragments;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dk.walk.R;
import com.dk.walk.database.SQLWay;
import com.dk.walk.database.SQLiteDataSource;
import com.dk.walk.util.WayAdapter;

public class OverviewFragment extends Fragment {
	private RelativeLayout layout;
	private ListView list;
	private WayAdapter adapter;
	private List<SQLWay> ways;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.overview_fragment, container, false);
		
		readWays();
		
		list = (ListView) layout.findViewById(R.id.list);
		adapter = new WayAdapter(getActivity(), ways);
		list.setAdapter(adapter);
		
		return layout;
	}

	private void readWays() {
		SQLiteDataSource datasource = new SQLiteDataSource(getActivity());
		datasource.open();
		
		ways = datasource.getAllSQLWays();
		
		datasource.close();
	}
}
