package com.tatsuowatanabe.funukulelequiz.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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

    /** display language setting. */
    private String lang = "ja"; // TODO: get lang from system on initialize.

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
     * @param activity
     * @param que
     */
    public void start(final MainActivity activity, RequestQueue que) {
        String url = QuizApiUrl.url(10);
        Log.d("Ukulele Quiz API URL", url);
        // TODO: hide the start button, until game end.
        // this.$btnStart.hide();
        // this.resetResults();

        Response.Listener<JSONArray> jsonRecListener = new Response.Listener<JSONArray>() {
            @Override public void onResponse(JSONArray response) {

                Log.d(" response", response.toString());

                Quizzes quizzes = Quizzes.fromJson(response);
                setQuizzes(quizzes).nextQuiz(activity);
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
                    activity.displayMessage("Sorry, a network error has occurred.");
                }
            }
        };
        JsonArrayRequest jsonRec = new JsonArrayRequest(Request.Method.GET, url, jsonRecListener, resErrListener);
        que.add(jsonRec);
    }

    /**
     * get the next quiz from quizIterator.
     */
    public void nextQuiz(final MainActivity activity) {
        if (quizIterator == null) { return; }
        if (quizIterator.hasNext()) {
            Quiz quiz = quizIterator.next();
            setCurrentQuiz(quiz).showQuiz(activity);
        } else {
            // TODO: finish the game and show results.
            TextView quizDisplay = (TextView)activity.findViewById(R.id.quiz_display);
            quizDisplay.setText("TODO: finish the game and show results.");
            // this.closeResults();

        }
    }

    /**
     * show the current quiz.
     * @param activity
     */
    private void showQuiz(final MainActivity activity) {
        TextView quizDisplay = (TextView)activity.findViewById(R.id.quiz_display);
        ListView choicesList = (ListView)activity.findViewById(R.id.choices_list);

        String quizBody = currentQuiz.setLang(lang).getBody();

        activity.displayMessage(quizBody);

        quizDisplay.setText(quizBody);
        ChoiceListAdapter adapter = new ChoiceListAdapter(activity.getApplicationContext());
        for (Quiz.Choice choice: currentQuiz.getChoices()) {
            adapter.add(choice);
        }
        // --- Sample Source -------------------------
        // PackageManager packageManager = activity.getPackageManager();
        // List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        // if (packageInfoList != null) {
        //     for (PackageInfo info : packageInfoList) {
        //         adapter.add(info);
        //     }
        // }
        // --- /Sample Source -------------------------

        int padding = (int) (activity.getResources().getDisplayMetrics().density * 8);
        ListView listView = (ListView) activity.findViewById(R.id.choices_list);
        listView.setPadding(padding, 0, padding, 0);
        listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        listView.setDivider(null);

        LayoutInflater inflater = LayoutInflater.from(activity.getApplicationContext());
        View header = inflater.inflate(R.layout.list_header_footer, listView, false);
        View footer = inflater.inflate(R.layout.list_header_footer, listView, false);
        listView.addHeaderView(header, null, false);
        listView.addFooterView(footer, null, false);
        listView.setAdapter(adapter);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity.getApplicationContext(), android.R.layout.simple_list_item_1);
//        for (Quiz.Choice choice: currentQuiz.getChoices()) {
//            adapter.add(choice.getBody(lang));
//        }
        choicesList.setAdapter(adapter);
//        choicesList.setOnItemClickListener(new ListView.OnItemClickListener() {
//            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ListView listView = (ListView) parent;
//                String item = (String)listView.getItemAtPosition(position);
//                activity.displayMessage(item);
//                // TODO: receive user's answer.
//            }
//        });

        // TODO: show current quiz.

    }

}
