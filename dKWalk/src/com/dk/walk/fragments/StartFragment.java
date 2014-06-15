package com.dk.walk.fragments;

import com.dk.walk.R;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class StartFragment extends Fragment implements OnClickListener {
	private static final String TAG = "StartFragment";
	
	private RelativeLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.start, container, false);
		layout.setOnClickListener(this);
		return layout;
	}

	@Override
	public void onClick(View v) {
		Log.d(TAG, "onClick");
		
	}
	
}
