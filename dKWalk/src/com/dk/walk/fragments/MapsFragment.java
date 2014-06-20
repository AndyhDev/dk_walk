package com.dk.walk.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dk.walk.R;
import com.dk.walk.database.SQLWay;
import com.dk.walk.database.SQLiteDataSource;
import com.dk.walk.service.GPSservice;
import com.dk.walk.util.ServiceFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsFragment extends ServiceFragment{
	private static final String TAG = "MapFragmen";

	public static final String ACTION = "action_map";
	public static final String TYPE = "type";
	public static final int TYPE_CURRENT = 0;
	public static final int TYPE_WAY = 1;
	public static final String WAY_ID = "way_id";

	private static final String KEY_ZOOM_LEVEL = "zoom_level";

	private RelativeLayout layout;
	private GoogleMap map;

	private int mode;
	private Long wayId;

	private LatLng lastPoint;
	private PolylineOptions line;
	private Float zoomLevel = 15f;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.fragment_map, container, false);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		if(savedInstanceState != null){
			if(savedInstanceState.containsKey(KEY_ZOOM_LEVEL)){
				zoomLevel = savedInstanceState.getFloat(KEY_ZOOM_LEVEL);
			}
		}
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
	private void loadWay(final SQLWay way){
		final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",  getString(R.string.create_map), true);
		
		new Thread(new Runnable() {
			public void run() {
				Log.d(TAG, "1");
				if(way != null){
					JSONArray gps = way.getGps();
					final List<LatLng> points = new ArrayList<LatLng>();
					for(int i = 0; i < gps.length(); i++){
						try {
							LatLng point = way.stringToLatLng(gps.getString(i));
							points.add(point);
						} catch (JSONException e) {
							e.printStackTrace();
						}	
					}
					if(points.size() != 0){
						line = new PolylineOptions();
						line.color(0x709F81F7);
						line.geodesic(true);
						line.width(20);
						line.addAll(points);
						line.visible(true);
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								map.clear();
								map.addPolyline(line);

								map.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(points.size()-1), zoomLevel));
								dialog.dismiss();
							}
						});
					}
				}
			}
		}).start();

	}
	@Override
	public void onServiceConnected() {
		if(mode == TYPE_CURRENT){
			refresh();
		}
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
			}
			List<LatLng> points;
			if(line != null){
				points = line.getPoints();
			}else{
				points = new ArrayList<LatLng>();
			}

			points.add(loc);
			line = new PolylineOptions();
			line.color(0x709F81F7);
			line.geodesic(true);
			line.width(20);
			line.addAll(points);
			line.visible(true);
			map.clear();
			map.addPolyline(line);

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoomLevel));
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
	@Override 
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putFloat(KEY_ZOOM_LEVEL, map.getCameraPosition().zoom);
		super.onSaveInstanceState(savedInstanceState);
	}
}
