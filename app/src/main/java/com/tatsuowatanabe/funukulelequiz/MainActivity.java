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

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    private final QuizGame game = new QuizGame();

    /**
     * Holder of activity view resource.
     */
    public ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vh = new ViewHolder();

        setSupportActionBar(vh.toolbar);
        mQueue = Volley.newRequestQueue(this);

        // floating action button
        vh.fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View view) { startQuiz(); }
        });

        // floating action button
        vh.fabNext.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View view) { nextQuiz(); }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * start the quiz game.
     */
    public void startQuiz() {
        game.start(this, mQueue);
    }

    /**
     * show the next quiz.
     */
    public void nextQuiz() {
        game.nextQuiz(this);
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
            return true;
        } else if (id == R.id.action_now) {
            Toast.makeText(this, getCurrentDateTimeString(), Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Somewhere that has access to a context
    public void displayMessage(String toastString){
        Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
    }

    /**
     * 現在日時をyyyy/MM/dd HH:mm:ss形式で取得する.<br>
     */
    private String getCurrentDateTimeString() {
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    /**
     * Holder class of activity view resource.
     */
    public class ViewHolder {
        public final Toolbar              toolbar      = (Toolbar) findViewById(R.id.toolbar);
        public final FloatingActionButton fab          = (FloatingActionButton)findViewById(R.id.fab);
        public final FloatingActionButton fabNext      = (FloatingActionButton)findViewById(R.id.fab_next);
        public final TextView             quizDisplay  = (TextView)findViewById(R.id.quiz_display);
        public final TextView             pointDisplay = (TextView)findViewById(R.id.point_display);
        public final ListView             choicesList  = (ListView)findViewById(R.id.choices_list);
    }

}