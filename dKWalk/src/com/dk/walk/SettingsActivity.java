package com.dk.walk;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import com.dk.walk.util.EditTextIntegerPreference;


public class SettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		final EditTextIntegerPreference weight = (EditTextIntegerPreference) findPreference("weight");
		
		weight.setSummary(weight.getText());
		weight.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// Set the value as the new value
				weight.setText(newValue.toString());
				// Get the entry which corresponds to the current value and set as summary
				preference.setSummary(weight.getText());
				return false;
			}
		});

	}
}
