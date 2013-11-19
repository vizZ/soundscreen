package com.arturglier.mobile.android.soundscreen;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

public class SoundscreenPreferenceActivity extends SherlockPreferenceActivity {
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);

        final ListPreference listPreference = (ListPreference) findPreference("pref_duration");
        listPreference.setSummary(getString(R.string.pref_duration_summary, getString(R.string.duration_entry_15sec)));
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Set the value as the new value
                listPreference.setValue(newValue.toString());
                // Get the entry which corresponds to the current value and set as summary
                preference.setSummary(getString(R.string.pref_duration_summary, listPreference.getEntry().toString()));
                return false;
            }
        });
    }
}