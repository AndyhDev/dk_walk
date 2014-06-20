package com.dk.walk.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.walk.MapActivity;
import com.dk.walk.R;
import com.dk.walk.database.SQLWay;
import com.dk.walk.database.SQLiteDataSource;

public class ShowWayFragment extends Fragment implements OnClickListener{
	private static final String TAG = "ShowWayFragment";

	public static final String ACTION_SHOW_WAY = "action_show_way";
	public static final String WAY_ID = "way_id";

	private SQLWay currentWay;

	private RelativeLayout layout;
	private TextView title;
	private TextView way;
	private TextView steps;
	private TextView cal;
	private TextView speed;
	private TextView duration;

	private ImageButton routeBnt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.show_way, container, false);

		title = (TextView) layout.findViewById(R.id.title);
		way = (TextView) layout.findViewById(R.id.way);
		steps = (TextView) layout.findViewById(R.id.steps);
		cal = (TextView) layout.findViewById(R.id.cal);
		speed = (TextView) layout.findViewById(R.id.speed);
		duration = (TextView) layout.findViewById(R.id.duration);

		routeBnt = (ImageButton) layout.findViewById(R.id.route);
		routeBnt.setOnClickListener(this);

		Intent intent = getActivity().getIntent();
		Log.d(TAG, "1");
		if(intent != null){
			Log.d(TAG, "2");
			if(intent.getAction().equals(ACTION_SHOW_WAY)){
				Log.d(TAG, "3");
				long wayId = intent.getLongExtra(WAY_ID, -1);
				Log.d(TAG, "4:" + wayId);
				if(wayId != -1){
					Log.d(TAG, "5");
					SQLiteDataSource datasource = new SQLiteDataSource(getActivity());
					datasource.open();
					currentWay = datasource.getSQLWaybyId(wayId);
					if(currentWay == null){
						Log.d(TAG, "6");
					}
					datasource.close();
					refresh();
				}
			}
		}

		return layout;
	}

	public void refresh(){
		if(currentWay != null){
			title.setText(currentWay.getTitle());
			way.setText(currentWay.getFormatedWay());
			steps.setText(currentWay.getSteps().toString());
			cal.setText(currentWay.getCalories().toString());
			speed.setText(currentWay.getFormatedSpeed());
			duration.setText(currentWay.getFormatedTime());
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		refresh();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if(id == R.id.route){
			Intent activity = new Intent(getActivity(), MapActivity.class);
			activity.setAction(MapsFragment.ACTION);
			activity.putExtra(MapsFragment.TYPE, MapsFragment.TYPE_WAY);
			if(currentWay != null){
				activity.putExtra(MapsFragment.WAY_ID, currentWay.getId());
			}
			startActivity(activity);
		}
	}
}
