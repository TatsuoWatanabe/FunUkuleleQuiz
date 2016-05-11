package com.tatsuowatanabe.funukulelequiz;

import android.os.Bundle;
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
     * Holder of activity view resource.
     */
    public ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vh = new ViewHolder();
        vh.resultArea.setVisibility(View.GONE);
        vh.pointDisplay.setText(getString(R.string.points, 0)); // 0 pt
        setSupportActionBar(vh.toolbar);
        game   = new QuizGame(this);
        mQueue = Volley.newRequestQueue(this);

        // floating action button
        vh.fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View view) { startQuiz(); }
        });
        // TODO: Show welcome message for user.
        // TODO: Show loading indicator.
        // TODO: Disable the button when loading.
        // TODO: If network error  is occurred then load quizzes from local data.
        // TODO: Set the progress bar of quizzes.

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * start the quiz game.
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
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // TODO: Add setting of vaibrate
            // TODO: Add setting of advertisement
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
        Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
    }

    /**
     * display the message
     * @param resId
     */
    public void displayMessage(Integer resId) {
        displayMessage(getString(resId));
    }

    /**
     * Holder class of activity view resource.
     */
    public class ViewHolder {
        public final Toolbar              toolbar       = (Toolbar)findViewById(R.id.toolbar);
        public final FloatingActionButton fab           = (FloatingActionButton)findViewById(R.id.fab);
        public final TextView             quizDisplay   = (TextView)findViewById(R.id.quiz_display);
        public final TextView             pointDisplay  = (TextView)findViewById(R.id.point_display);
        public final ListView             choicesList   = (ListView)findViewById(R.id.choices_list);
        public final TextView             resultMessage = (TextView)findViewById(R.id.result_message);
        public final TextView             explanation   = (TextView)findViewById(R.id.explanation);
        public final View                 resultArea    = (View)findViewById(R.id.result_area);
    }

}