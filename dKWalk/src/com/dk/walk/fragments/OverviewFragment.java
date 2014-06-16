package com.dk.walk.fragments;

import java.util.Collections;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dk.walk.R;
import com.dk.walk.ShowWayActivity;
import com.dk.walk.database.SQLWay;
import com.dk.walk.database.SQLiteDataSource;
import com.dk.walk.util.WayAdapter;

public class OverviewFragment extends Fragment implements OnItemClickListener {
	private RelativeLayout layout;
	private ListView list;
	private WayAdapter adapter;
	private List<SQLWay> ways;
	
	private SQLWay selectedWay;
	
	private static final int ID_VIEW = 0;
	private static final int ID_DELETE = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.overview_fragment, container, false);
		
		readWays();
		
		list = (ListView) layout.findViewById(R.id.list);
		adapter = new WayAdapter(getActivity(), ways);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		registerForContextMenu(list);
		
		return layout;
	}

	private void readWays() {
		SQLiteDataSource datasource = new SQLiteDataSource(getActivity());
		datasource.open();
		
		ways = datasource.getAllSQLWays();
		Collections.reverse(ways);
		
		datasource.close();
	}
	
	private void refresh(){
		readWays();
		adapter.setSongList(ways);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if(v.getId() == R.id.list){
			AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
			selectedWay = (SQLWay)list.getItemAtPosition(acmi.position);
			menu.add(0, ID_VIEW, 0, getString(R.string.view));
		    menu.add(0, ID_DELETE, 0, getString(R.string.delete));  
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == ID_VIEW){
			Intent activity = new Intent(getActivity(), ShowWayActivity.class);
			activity.setAction(ShowWayFragment.ACTION_SHOW_WAY);
			activity.putExtra(ShowWayFragment.WAY_ID, selectedWay.getId());
			
			startActivity(activity);
		}else if(item.getItemId() == ID_DELETE){
			SQLiteDataSource datasource = new SQLiteDataSource(getActivity());
			datasource.open();
			
			datasource.deleteSQLWay(selectedWay);
			Collections.reverse(ways);
			
			datasource.close();
			refresh();
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SQLWay way = (SQLWay) list.getItemAtPosition(position);
		Intent activity = new Intent(getActivity(), ShowWayActivity.class);
		activity.setAction(ShowWayFragment.ACTION_SHOW_WAY);
		activity.putExtra(ShowWayFragment.WAY_ID, way.getId());
		
		startActivity(activity);
	}
	
}
