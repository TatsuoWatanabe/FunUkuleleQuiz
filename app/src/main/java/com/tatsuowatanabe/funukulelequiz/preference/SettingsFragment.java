package com.tatsuowatanabe.funukulelequiz.preference;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;

import com.tatsuowatanabe.funukulelequiz.R;

/**
 * Created by tatsuo on 5/25/16.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SettingsActivity s = (SettingsActivity)getActivity();
        String lang = s.getLang();
        setLang(lang);
    }

    private SettingsFragment setLang(String lang) {
        Boolean isJa = getString(R.string.lang_ja).equals(lang);
        Boolean isEn = getString(R.string.lang_en).equals(lang);

        CheckBoxPreference prefLocalMode = (CheckBoxPreference) findPreference("local_mode");
        prefLocalMode.setTitle(getString(
            isJa ? R.string.pref_title_local_mode_ja :
            isEn ? R.string.pref_title_local_mode_en :
                   R.string.pref_title_local_mode_en
        ));
        prefLocalMode.setSummary(getString(
            isJa ? R.string.pref_summ_local_mode_ja :
            isEn ? R.string.pref_summ_local_mode_en :
                   R.string.pref_summ_local_mode_en
        ));

        return this;
    }

}
