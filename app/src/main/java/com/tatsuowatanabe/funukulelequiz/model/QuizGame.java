package com.tatsuowatanabe.funukulelequiz.model;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tatsuowatanabe.funukulelequiz.MainActivity;
import com.tatsuowatanabe.funukulelequiz.R;
import com.tatsuowatanabe.funukulelequiz.adapter.ChoiceListAdapter;

import org.json.JSONArray;

/**
 * Created by tatsuo on 11/29/15.
 * Quiz game management class.
 */
public class QuizGame {
    //** amount of quizzes at one game. */
    private static final int QUIZ_AMOUNT = 10;
    /** collection of quiz object for use in one game. */
    private Quizzes quizzes;
    /** display language setting. */
    private String lang = "ja"; // TODO: get lang from system on receiveQuizzes.
    /** instance of MainActivity. */
    private MainActivity activity;

    public QuizGame(MainActivity ma) {
        this.activity = ma;
    }

    /**
     * get quizzes and start the game.
     * @param que
     */
    public void start(RequestQueue que) {
        final Integer TIMEOUT_MS = (10 /* seconds */ * 1000);
        String url = QuizApiUrl.url(QuizGame.QUIZ_AMOUNT);
        activity.vh.fab.setVisibility(View.GONE);
        Log.d("Ukulele Quiz API URL", url);
        // TODO: if failed to get the quizzes, load the local quizzes.

        Response.Listener<JSONArray> jsonRecListener = new Response.Listener<JSONArray>() {
            @Override public void onResponse(JSONArray response) {
                Log.d(" response", response.toString());
                Quizzes quizzes = Quizzes.fromJson(response).setContext(activity).shuffleChoices();
                receiveQuizzes(quizzes).nextQuiz(activity);
            }
        };
        Response.ErrorListener resErrListener = new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                activity.vh.fab.setVisibility(View.VISIBLE);
                activity.displayMessage("Sorry, a network error has occurred.\n" +  error.toString());
                Log.d(" onErrorResponse", error.toString());

                NetworkResponse response = error.networkResponse;
                if (response      == null) { return; }
                if (response.data == null) { return; }
                if (response.statusCode == 400) {
                    String json = new String(response.data);
                    Log.d(" onErrorResponse", json);
                }
            }
        };
        JsonArrayRequest jsonRec = new JsonArrayRequest(Request.Method.GET, url, jsonRecListener, resErrListener);

        // specify the request timeout setting.
        jsonRec.setRetryPolicy(new DefaultRetryPolicy(
            TIMEOUT_MS.intValue(),
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        Log.d(" getCurrentTimeout", String.valueOf(jsonRec.getRetryPolicy().getCurrentTimeout()) + " ms");
        que.add(jsonRec);
    }

    /**
     * Set the quizzes and iterator of that.
     * @param qzs
     * @return
     */
    private QuizGame receiveQuizzes(Quizzes qzs) {
        quizzes = qzs;
        quizzes.getResults().setActivity(activity).reset();
        activity.vh.quizDisplay.setVisibility(View.VISIBLE);
        activity.vh.choicesList.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * Show next quiz if exists, if not exists then finish the game.
     * @param activity
     */
    public void nextQuiz(final MainActivity activity) {
        if (quizzes.hasNext()) {
            quizzes.next();
            showQuiz();
        } else {
            finish(activity);
        }
    }

    /**
     * set lang to Japanese.
     * @return
     */
    public QuizGame toJapanese() {
        return setLang(R.string.lang_ja, R.string.lang_changed_ja);
    }

    /**
     * set lang to English.
     * @return
     */
    public QuizGame toEnglish() {
        return setLang(R.string.lang_en, R.string.lang_changed_en);
    }

    /**
     * set the language.
     * @param specifiedLang
     * @return
     */
    private QuizGame setLang(Integer specifiedLang, Integer msgId) {
        if (langIs(specifiedLang)) { return this; }

        Log.d("setLang", activity.getString(msgId));

        this.lang = activity.getString(specifiedLang);
        // TODO: show the should show. not show the should not show.
        showQuiz();
        quizzes.getResults().setMessageOf(lang);
        return this;
    }

    /**
     * returns whether specified lang matche or not game's lang.
     * @param resId
     * @return
     */
    private boolean langIs(Integer resId) {
        return lang == activity.getString(resId);
    }

    /**
     * Show the current quiz.
     */
    private QuizGame showQuiz() {
        Quiz currentQuiz = quizzes.current().setLang(lang);

        // quiz body
        final String quizBody = currentQuiz.getBody();
        activity.vh.quizDisplay.setText(quizBody);
        // quiz choices
        ChoiceListAdapter adapter = new ChoiceListAdapter(activity.getApplicationContext());
        adapter.receiveQuiz(currentQuiz);
        activity.vh.choicesList.setAdapter(adapter);
        // choice selected event
        activity.vh.choicesList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                // receive user's answer.
                Quiz.Choice choice = (Quiz.Choice)listView.getItemAtPosition(position);
                quizzes.getResults().receiveAnswer(choice, quizzes.current());
                nextQuiz(activity);
            }
        });

        return this;
    }

    /**
     * Finish the game and show results.
     * @param activity
     * @return
     */
    private QuizGame finish(final MainActivity activity) {
        QuizResults results = quizzes.getResults();
        results.generateResultMessages().setMessageOf(lang).showResultArea();
        activity.vh.quizDisplay.setVisibility(View.GONE);
        activity.vh.choicesList.setVisibility(View.GONE);
        activity.vh.fab.setVisibility(View.VISIBLE);

        return this;
    }

}
