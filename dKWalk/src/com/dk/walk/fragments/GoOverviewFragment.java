package com.dk.walk.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dk.walk.OverviewActivity;
import com.dk.walk.R;

public class GoOverviewFragment extends Fragment implements OnClickListener{
	private RelativeLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.go_overview, container, false);
		layout.setOnClickListener(this);
		
		return layout;
	}

	@Override
	public void onClick(View v) {
		Intent activity = new Intent(getActivity(), OverviewActivity.class);
		startActivity(activity);
		
	}
}
