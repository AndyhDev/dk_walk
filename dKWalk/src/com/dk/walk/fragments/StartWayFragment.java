package com.dk.walk.fragments;

import com.dk.walk.R;
import com.dk.walk.database.SQLWay;
import com.dk.walk.service.GPSservice;
import com.dk.walk.util.ServiceFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StartWayFragment extends ServiceFragment implements OnClickListener{
	@SuppressWarnings("unused")
	private static final String TAG = "StartWayFragment";

	private RelativeLayout layout;
	private Button startBnt;
	private Button stopBnt;
	private ImageButton pauseBnt;
	
	private TextView way;
	private TextView speed;
	private TextView steps;
	private TextView cal;
	private TextView duration;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.start_way, container, false);
		
		startBnt = (Button) layout.findViewById(R.id.start);
		stopBnt = (Button) layout.findViewById(R.id.stop);
		stopBnt.setEnabled(false);
		pauseBnt = (ImageButton) layout.findViewById(R.id.pause);
		pauseBnt.setEnabled(false);
		
		startBnt.setOnClickListener(this);
		stopBnt.setOnClickListener(this);
		pauseBnt.setOnClickListener(this);
		
		way = (TextView) layout.findViewById(R.id.way);
		speed = (TextView) layout.findViewById(R.id.speed);
		steps = (TextView) layout.findViewById(R.id.steps);
		cal = (TextView) layout.findViewById(R.id.calories);
		duration = (TextView) layout.findViewById(R.id.duration);
		
		refresh();
		
		return layout;
	}
	private void refresh(){
		if(getService() != null){
			int state = getService().getGPSState();
			if(state == GPSservice.GPS_STATE_PAUSE){
				startBnt.setEnabled(false);
				stopBnt.setEnabled(true);
				pauseBnt.setEnabled(true);
				pauseBnt.setImageResource(R.drawable.ic_action_play);
			}else if(state == GPSservice.GPS_STATE_START){
				startBnt.setEnabled(false);
				stopBnt.setEnabled(true);
				pauseBnt.setEnabled(true);
				pauseBnt.setImageResource(R.drawable.ic_action_pause);
			}else if(state == GPSservice.GPS_STATE_STOP){
				startBnt.setEnabled(true);
				stopBnt.setEnabled(false);
				pauseBnt.setEnabled(false);
				pauseBnt.setImageResource(R.drawable.ic_action_pause);
			}
			SQLWay way = getService().getWay();
			if(way != null){
				this.way.setText(way.getFormatedWay());
				speed.setText(way.getFormatedSpeed());
				steps.setText(way.getSteps().toString());
				cal.setText(way.getCalories().toString());
				duration.setText(way.getFormatedTime());
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
		refresh();
	}

	@Override
	public void onNewSteps() {
		refresh();
	}

	@Override
	public void onNewWay() {
		refresh();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.start){
			if(getService() != null){
				getService().startGPS();
				refresh();
			}
		}else if(id == R.id.stop){
			if(getService() != null){
				getService().stopGPS();
				refresh();
			}
		}else if(id == R.id.pause){
			if(getService() != null){
				getService().togglePauseState();
				refresh();
			}
		}
	}
	@Override
	public void onUpdateTime() {
		refresh();
	}
}
