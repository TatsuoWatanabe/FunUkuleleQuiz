package com.tatsuowatanabe.funukulelequiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tatsuowatanabe.funukulelequiz.model.QuizGame;
import com.tatsuowatanabe.funukulelequiz.preference.SettingsActivity;

public class MainActivity extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * Volley request Queue.
     */
    private RequestQueue mQueue;

    /**
     * Game Manager Object.
     */
    private QuizGame game;

    /**
     * Vibrator service.
     */
    private Vibrator vibrator;

    /**
     * Holder of activity view resource.
     */
    public ViewHolder views;

    /**
     * Holder of preferences.
     */
    public PreferencesHolder prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = new PreferencesHolder();
        views = new ViewHolder();
        setSupportActionBar(views.toolbar);
        game     = new QuizGame(this);
        mQueue   = Volley.newRequestQueue(this);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        // floating action button
        views.fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View view) { startQuiz(); }
        });
        // TODO: Set the progress bar of quizzes.
        // TODO: Add setting of advertisement.
        // TODO: Add in app purchase.
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * show the quiz game.
     */
    public void startQuiz() {
        game.start(mQueue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify in_right parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new android.content.Intent(this, SettingsActivity.class);
            settingsIntent.putExtra("lang", game.getLang());
            startActivity(settingsIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        } else if (id == R.id.action_lang_en) {
            game.toEnglish();
            return true;
        } else if (id == R.id.action_lang_ja) {
            game.toJapanese();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * display the message
     * @param toastString
     */
    public void displayMessage(String toastString) {
        Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
    }

    /**
     * get the shared preferences.
     * @return
     */
    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * get the vibrator.
     * @return
     */
    public Vibrator getVibrator() {
        return this.vibrator;
    }

    /**
     * Holder class of shared preferences.
     */
    public class PreferencesHolder {
        private SharedPreferences sharedPreferences = getSharedPreferences();
        public Boolean isLocalMode()          { return sharedPreferences.getBoolean(getString(R.string.pref_key_local_mode)  , false); }
        public Boolean shouldUseVibration()   { return sharedPreferences.getBoolean(getString(R.string.pref_key_vibration)   , true); }
        public Boolean shouldUseColorEffect() { return sharedPreferences.getBoolean(getString(R.string.pref_key_color_effect), true); }
    }

    /**
     * Holder class of activity view resource.
     */
    public class ViewHolder {
        public final Toolbar              toolbar         = (Toolbar)findViewById(R.id.toolbar);
        public final FloatingActionButton fab             = (FloatingActionButton)findViewById(R.id.fab);
        public final TextView             quizDisplay     = (TextView)findViewById(R.id.quiz_display);
        public final TextView             pointDisplay    = (TextView)findViewById(R.id.point_display);
        public final ListView             choicesList     = (ListView)findViewById(R.id.choices_list);
        public final TextView             resultMessage   = (TextView)findViewById(R.id.result_message);
        public final TextView             explanation     = (TextView)findViewById(R.id.explanation);
        public final View                 resultArea      = (View)findViewById(R.id.result_area);
        public final View                 welcomeArea     = (View)findViewById(R.id.welcome_area);
        public final TextView             welcomeMessage  = (TextView)findViewById(R.id.welcome_message);
        public final View                 loadingArea     = (View)findViewById(R.id.loading_area);
        public final View                 contentMain     = (View)findViewById(R.id.content_main);
        public final TextView             quizzesProgress = (TextView)findViewById(R.id.quizzes_progress);
        public ViewHolder() {
            resultArea.setVisibility(View.GONE);
            quizzesProgress.setVisibility(View.GONE);
            pointDisplay.setText(getString(R.string.points, 0)); // 0 pt
        }
    }

}