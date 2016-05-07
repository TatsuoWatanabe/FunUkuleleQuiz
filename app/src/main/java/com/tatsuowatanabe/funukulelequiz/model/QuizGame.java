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

    /**
     * get quizzes and start the game.
     * @param activity
     * @param que
     */
    public void start(final MainActivity activity, RequestQueue que) {
        final Integer TIMEOUT_MS = (10 /* seconds */ * 1000);
        String url = QuizApiUrl.url(QuizGame.QUIZ_AMOUNT);
        activity.vh.quizDisplay.setVisibility(View.VISIBLE);
        Log.d("Ukulele Quiz API URL", url);
        // TODO: hide the start button, until game end.

        Response.Listener<JSONArray> jsonRecListener = new Response.Listener<JSONArray>() {
            @Override public void onResponse(JSONArray response) {

                Log.d(" response", response.toString());

                Quizzes quizzes = Quizzes.fromJson(response);

                receiveQuizzes(quizzes, activity).nextQuiz(activity);
            }
        };
        Response.ErrorListener resErrListener = new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Log.d(" onErrorResponse", error.toString());

                NetworkResponse response = error.networkResponse;
                if (response      == null) { return; }
                if (response.data == null) { return; }
                if (response.statusCode == 400) {
                    String json = new String(response.data);
                    Log.d(" onErrorResponse", json);
                    activity.displayMessage("Sorry, a network error has occurred.");
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
    private QuizGame receiveQuizzes(Quizzes qzs, MainActivity ac) {
        quizzes = qzs;
        quizzes.getResults().setActivity(ac).reset();
        return this;
    }

    /**
     * Show next quiz if exists, if not exists then finish the game.
     * @param activity
     */
    public void nextQuiz(final MainActivity activity) {
        if (quizzes.hasNext()) {
            quizzes.next();
            showQuiz(activity);
        } else {
            finish(activity);
        }
    }

    /**
     * Show the current quiz.
     * @param activity
     */
    private QuizGame showQuiz(final MainActivity activity) {
        Quiz currentQuiz = quizzes.current();
        // quiz body
        final String quizBody = currentQuiz.setLang(lang).getBody();
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
        // TODO: finish the game and show results.
        QuizResults results = quizzes.getResults();
        results.showGameResult();
        activity.vh.quizDisplay.setText("");
        activity.vh.quizDisplay.setVisibility(View.GONE);
        ChoiceListAdapter adapter = (ChoiceListAdapter)activity.vh.choicesList.getAdapter();
        adapter.clearSelf().notifyDataSetChanged();
        return this;
    }

}
