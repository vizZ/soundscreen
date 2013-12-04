package com.arturglier.mobile.android.soundscreen;

import android.os.Bundle;
import android.preference.EditTextPreference;
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

        final ListPreference duration = (ListPreference) findPreference("pref_duration");
        duration.setSummary(getString(R.string.pref_duration_summary, getString(R.string.duration_entry_15sec)));
        duration.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Set the value as the new value
                duration.setValue(newValue.toString());
                // Get the entry which corresponds to the current value and set as summary
                preference.setSummary(getString(R.string.pref_duration_summary, duration.getEntry().toString()));
                return false;
            }
        });

        final ListPreference download = (ListPreference) findPreference("pref_download");
        download.setSummary(getString(R.string.pref_download_summary, getString(R.string.pref_download_entry_mobile_and_wifi)));
        download.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Set the value as the new value
                download.setValue(newValue.toString());
                // Get the entry which corresponds to the current value and set as summary
                preference.setSummary(getString(R.string.pref_download_summary, download.getEntry().toString()));
                return false;
            }
        });

        final ListPreference sync = (ListPreference) findPreference("pref_sync");
        sync.setSummary(getString(R.string.pref_sync_summary, getString(R.string.pref_sync_entry_1_hour)));
        sync.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Set the value as the new value
                sync.setValue(newValue.toString());
                // Get the entry which corresponds to the current value and set as summary
                preference.setSummary(getString(R.string.pref_sync_summary, sync.getEntry().toString()));
                return false;
            }
        });

        final ListPreference cache = (ListPreference) findPreference("pref_cache");
        cache.setSummary(getString(R.string.pref_cache_summary, getString(R.string.pref_cache_entry_10_tracks)));
        cache.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Set the value as the new value
                cache.setValue(newValue.toString());
                // Get the entry which corresponds to the current value and set as summary
                preference.setSummary(getString(R.string.pref_cache_summary, cache.getEntry().toString()));
                return false;
            }
        });

        final EditTextPreference build = (EditTextPreference) findPreference("pref_build");
        build.setSummary(getString(R.string.pref_build_summary, BuildConfig.VERSION_NAME));
    }
}