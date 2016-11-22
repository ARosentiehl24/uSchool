package com.arrg.android.app.uschool.view.fragment;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.arrg.android.app.uschool.R;
import com.arrg.android.app.uschool.USchool;
import com.thefinestartist.utils.preferences.Pref;

/**
 * Created by Alberto on 14-Nov-16.
 */

public class ServerSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.server_settings);

        USchool uSchool = USchool.getInstance();

        String host = uSchool.getHost();
        String port = uSchool.getPort();

        EditTextPreference ipPreference = (EditTextPreference) findPreference("host");
        ipPreference.setText(host);
        ipPreference.setOnPreferenceChangeListener(this);
        ipPreference.setSummary(host);

        EditTextPreference portPreference = (EditTextPreference) findPreference("port");
        portPreference.setText(port);
        portPreference.setOnPreferenceChangeListener(this);
        portPreference.setSummary(port);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        Pref.put(preference.getKey(), o.toString());

        preference.setSummary(o.toString());

        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}
