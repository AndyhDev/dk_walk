package com.dk.walk.fragments;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.location.Location;
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
import com.dk.walk.util.ServiceFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsFragment extends ServiceFragment{
	private static final String TAG = "MapFragmen";

	public static final String ACTION = "action_map";
	public static final String TYPE = "type";
	public static final int TYPE_CURRENT = 0;
	public static final int TYPE_WAY = 1;
	public static final String WAY_ID = "way_id";

	private RelativeLayout layout;
	private GoogleMap map;

	private int mode;
	private Long wayId;

	private LatLng lastPoint;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.fragment_map, container, false);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		Intent intent = getActivity().getIntent();
		String action = intent.getAction();
		if(action != null){
			if(action.equals(ACTION)){
				if(intent.getIntExtra(TYPE, -1) == TYPE_CURRENT){
					mode = TYPE_CURRENT;
				}else if(intent.getIntExtra(TYPE, -1) == TYPE_WAY){
					mode = TYPE_WAY;
					wayId = intent.getLongExtra(WAY_ID, -1);
				}
			}
		}
		refresh();
		return layout;
	}
	private void refresh(){
		map.clear();
		lastPoint = null;

		if(mode == TYPE_CURRENT){
			if(getService() != null){
				SQLWay way = getService().getWay();
				loadWay(way);
			}
		}else if(mode == TYPE_WAY){
			if(wayId != -1){
				SQLiteDataSource datasource = new SQLiteDataSource(getActivity());
				datasource.open();
				SQLWay way = datasource.getSQLWaybyId(wayId);
				datasource.close();
				loadWay(way);
			}
		}
	}
	private void loadWay(SQLWay way){
		if(way != null){
			JSONArray gps = way.getGps();
			for(int i = 0; i < gps.length(); i++){
				try {
					LatLng point = way.stringToLatLng(gps.getString(i));
					addPoint(point);
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}
		}
	}
	@Override
	public void onServiceConnected() {
		refresh();
	}

	@Override
	public void onServiceDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewLocation() {
		Log.d(TAG, "onNewLocation()");
		if(mode == TYPE_CURRENT){
			Log.d(TAG, "onNewLocation(1)");
			GPSservice service = getService();
			if(service != null){
				Log.d(TAG, "onNewLocation(2)");
				SQLWay way = service.getWay();
				if(way != null){
					Log.d(TAG, "onNewLocation(3)");
					LatLng loc = way.getLastLatLng();
					addPoint(loc);
				}
			}
		}

	}

	private void addPoint(LatLng loc) {
		Log.d(TAG, "addPoint()");
		if(loc != null){
			if(lastPoint == null){
				Log.d(TAG, "addPoint(1)");
				map.addMarker(new MarkerOptions()
				.position(loc)
				.title(getString(R.string.start2)));
			}else{
				Log.d(TAG, "addPoint(2)");
				PolylineOptions line = new PolylineOptions()
						.add(lastPoint, loc)
						.color(0x709F81F7)
						.geodesic(true);
				map.addPolyline(line);
			}
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
			lastPoint = loc;
		}

	}

	@Override
	public void onNewSteps() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewWay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdateTime() {
		// TODO Auto-generated method stub

	}

}
