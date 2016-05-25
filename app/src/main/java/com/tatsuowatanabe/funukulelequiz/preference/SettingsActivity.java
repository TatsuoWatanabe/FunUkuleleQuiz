package com.tatsuowatanabe.funukulelequiz.preference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by tatsuo on 5/25/16.
 */
public class SettingsActivity extends Activity {

    private String lang = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        this.lang = i.getStringExtra("lang");

        // Display the fragment as the main content.
        getFragmentManager()
            .beginTransaction()
            .replace(android.R.id.content, new SettingsFragment())
            .commit();
    }

    public String getLang() {
        return lang;
    }
}
