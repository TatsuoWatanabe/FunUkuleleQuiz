package com.tatsuowatanabe.funukulelequiz.Model;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tatsuowatanabe.funukulelequiz.MainActivity;

import org.json.JSONArray;

import java.util.Iterator;

/**
 * Created by tatsuo on 11/29/15.
 * Quiz game management class.
 */
public class QuizGame {
//    public  $el              = $('#quizapp');
//    private $quizApp         = $('#quizapp');
//    private $quizDisplay     = $('#quiz-display');
//    private $pointDisplay    = $('#point-display');
//    private $progressDisplay = $('#progress-display');
//    private $choicesList     = $('#choices-list');
//    private $btnStart        = $('#btn-start');
//    private mainApiPath      = 'http://mongoquizserver.herokuapp.com/api';
//    private quizzes       = [];
//    private allQuizzes    = [];
//    private currentQuiz: any;
//    private results = {
//        total      : 0,
//                incorrects : [],
//        isEnd      : false,
//                explanation: { ja: '', en: '' },
//        reset      : () => {
//            this.results.total      = 0;
//            this.results.isEnd      = false;
//            this.results.incorrects = [];
//            this.results.explanation.ja  = this.results.explanation.en  = '';
//        }
//    };

//    private startQuiz() {
//        var url = this.apiPaths[location.host] || this.mainApiPath;
//        this.$btnStart.hide();
//        this.resetResults();
//        $.ajax(url, {
//                data: { limit: 10 }
//        }).done((data) => {
//            this.quizzes    = _.clone(data);
//            this.allQuizzes = _.clone(data);
//            this.initProgress(this.quizzes);
//            this.nextQuiz();
//        }).fail(() => {
//            this.$btnStart.show();
//        });
//    }

    /** collection of quiz object for use in one game. */
    private Quizzes quizzes;

    /** iterator of quizzes ArrayList. */
    private Iterator<Quiz> quizIterator;

    /**
     * set the quizzes and iterator of that.
     * @param qzs
     * @return
     */
    private QuizGame setQuizzes(Quizzes qzs) {
        this.quizzes      = qzs;
        this.quizIterator = qzs.iterator();
        return this;
    }

    /** quiz object of now displaying. */
    private Quiz currentQuiz;

    /**
     * set the current quiz object.
     * @param Quiz qz
     * @return QuizGame
     */
    private QuizGame setCurrentQuiz(Quiz qz) {
        qz.shuffleChoices();
        this.currentQuiz = qz;
        return this;
    }

    /**
     * get quizzes and start the game.
     * @param que
     */
    public void start(final MainActivity activity, RequestQueue que) {
        String url = QuizApiUrl.url(3);
        Log.d("Ukulele Quiz API URL", url);

        // this.$btnStart.hide();
        // this.resetResults();

        Response.Listener<JSONArray> jsonRecListener = new Response.Listener<JSONArray>() {
            @Override public void onResponse(JSONArray response) {
                Quizzes quizzes = Quizzes.fromJson(response);

                // TODO: 取得した問題を表示する
                setQuizzes(quizzes).nextQuiz();

                // this.initProgress(this.quizzes);

                Log.d(" quizzes", quizzes.toString());

                activity.displayMessage(quizzes.toString());
            }
        };
        Response.ErrorListener resErrListener = new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
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
        que.add(jsonRec);
    }

    /**
     * get the next quiz from quizIterator.
     */
    public void nextQuiz() {
        if (quizIterator == null) { return; }
        if (quizIterator.hasNext()) {
            Quiz quiz = quizIterator.next();
            setCurrentQuiz(quiz).showQuiz();
        } else {
            // this.closeResults();
        }
    }

    public void showQuiz() {
        // TODO: show current quiz.
    }


}
