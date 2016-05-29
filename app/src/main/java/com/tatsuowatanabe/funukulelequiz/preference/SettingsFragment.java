package com.tatsuowatanabe.funukulelequiz.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

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

        SwitchPreference prefLocalMode = (SwitchPreference)findPreference(getString(R.string.pref_key_local_mode));
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
        SwitchPreference prefVibration = (SwitchPreference)findPreference(getString(R.string.pref_key_vibration));
        prefVibration.setTitle(getString(
            isJa ? R.string.pref_title_vibration_ja :
            isEn ? R.string.pref_title_vibration_en :
                   R.string.pref_title_vibration_en
        ));
        prefVibration.setSummary(getString(
            isJa ? R.string.pref_summ_vibration_ja :
            isEn ? R.string.pref_summ_vibration_en :
                   R.string.pref_summ_vibration_en
        ));
        SwitchPreference prefColorEffect = (SwitchPreference)findPreference(getString(R.string.pref_key_color_effect));
        prefColorEffect.setTitle(getString(
                isJa ? R.string.pref_title_color_effect_ja :
                isEn ? R.string.pref_title_color_effect_en :
                       R.string.pref_title_color_effect_en
        ));
        prefColorEffect.setSummary(getString(
                isJa ? R.string.pref_summ_color_effect_ja :
                isEn ? R.string.pref_summ_color_effect_en :
                       R.string.pref_summ_color_effect_en
        ));
        SwitchPreference prefAds = (SwitchPreference)findPreference(getString(R.string.pref_key_ads));
        prefAds.setTitle(getString(
                isJa ? R.string.pref_title_ads_ja :
                isEn ? R.string.pref_title_ads_en :
                       R.string.pref_title_ads_en
        ));
        prefAds.setSummary(getString(
                isJa ? R.string.pref_summ_ads_ja :
                isEn ? R.string.pref_summ_ads_en :
                       R.string.pref_summ_ads_en
        ));
        return this;
    }

}
