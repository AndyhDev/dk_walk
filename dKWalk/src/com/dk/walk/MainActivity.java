package com.dk.walk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.MenuItem;

import com.dk.walk.fragments.GoOverviewFragment;
import com.dk.walk.fragments.LastWayFragment;
import com.dk.walk.fragments.StartFragment;
import com.dk.walk.fragments.StatisticOverviewFragment;

public class MainActivity extends Activity {
	WakeLock mWakeLock;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container1, new StartFragment()).commit();
			getFragmentManager().beginTransaction().add(R.id.container2, new StatisticOverviewFragment()).commit();
			getFragmentManager().beginTransaction().add(R.id.container4, new GoOverviewFragment()).commit();
			getFragmentManager().beginTransaction().add(R.id.container3, new LastWayFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Intent i;
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
