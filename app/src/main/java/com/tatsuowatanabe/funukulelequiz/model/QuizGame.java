package com.tatsuowatanabe.funukulelequiz.model;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.tatsuowatanabe.funukulelequiz.MainActivity;
import com.tatsuowatanabe.funukulelequiz.R;
import com.tatsuowatanabe.funukulelequiz.adapter.ChoiceListAdapter;

import java.util.Locale;

/**
 * Created by tatsuo on 11/29/15.
 * Quiz game management class.
 */
public class QuizGame {
    //** amount of quizzes at one game. */
    private static final Integer QUIZ_AMOUNT = 10;
    /** collection of quiz object for use in one game. */
    private Quizzes quizzes;
    /** display language setting. */
    private String lang = "";
    /** instance of MainActivity. */
    private final MainActivity activity;

    /** get the MainActivity. */
    public MainActivity getActivity() { return activity; }

    public QuizGame(MainActivity ma) {
        final String ja      = ma.getString(R.string.lang_ja);
        final String en      = ma.getString(R.string.lang_en);
        final String sysLang = Locale.getDefault().getLanguage();

        this.activity = ma;
        if      (sysLang.equals(ja)) { this.toJapanese(); }
        else if (sysLang.equals(en)) { this.toEnglish();  }
        else                         { this.toEnglish();  }
    }

    /**
     * get quizzes and show the game.
     * @param que
     */
    public void start(RequestQueue que) {
        QuizzesLoader.LoadQuizzes(this, que, QUIZ_AMOUNT, new QuizzesLoader.Listener() {
            @Override
            public void onLoad(Quizzes quizzes) {
                receiveQuizzes(quizzes).nextQuiz(activity);
            }
        });
    }

    /**
     * Set the quizzes and iterator of that.
     * @param qzs
     * @return
     */
    private QuizGame receiveQuizzes(Quizzes qzs) {
        quizzes = qzs;
        quizzes.getResults().setActivity(activity).reset();
        activity.views.quizDisplay.setVisibility(View.VISIBLE);
        activity.views.choicesList.setVisibility(View.VISIBLE);
        activity.views.welcomeArea.setVisibility(View.GONE);
        return this;
    }

    /**
     * Show next quiz if exists, if not exists then finish the game.
     * @param activity
     */
    public void nextQuiz(final MainActivity activity) {
        if (quizzes.hasNext()) {
            quizzes.next();
            showQuiz(this.lang);
        } else {
            finish(activity);
        }
    }

    /**
     * set lang to Japanese.
     * @return
     */
    public QuizGame toJapanese() {
        return setLang(R.string.lang_ja, R.string.msg_lang_changed_ja);
    }

    /**
     * set lang to English.
     * @return
     */
    public QuizGame toEnglish() {
        return setLang(R.string.lang_en, R.string.msg_lang_changed_en);
    }

    /**
     * get the lang.
     * @return
     */
    public String getLang() {
        return lang;
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
        setMessageOf(this.lang);
        if (quizzes == null) { return this; }

        showQuiz(this.lang);
        quizzes.getResults().setMessageOf(lang);
        return this;
    }

    /**
     * show message of the game.
     * @return
     */
    private QuizGame setMessageOf(String lang) {
        String welcomeMessage = langIs(R.string.lang_ja) ? activity.getString(R.string.msg_welcome_ja) :
                                langIs(R.string.lang_en) ? activity.getString(R.string.msg_welcome_en) : "";
        activity.views.welcomeMessage.setText(welcomeMessage);
        return this;
    }

    /**
     * returns whether specified lang matche or not game's lang.
     * @param resId
     * @return
     */
    public boolean langIs(Integer resId) {
        return lang.equals(activity.getString(resId));
    }

    /**
     * Show the current quiz.
     */
    private QuizGame showQuiz(String lang) {
        Quiz currentQuiz = quizzes.current().setLang(lang);
        activity.views.quizzesProgress.setText(activity.getString(R.string.quizzes_progress, quizzes.getIndex(), quizzes.size()));
        activity.views.quizzesProgress.setVisibility(View.VISIBLE);

        Log.d("System language", Locale.getDefault().getLanguage());

        // quiz body
        final String quizBody = currentQuiz.getBody();
        activity.views.quizDisplay.setText(quizBody);
        // quiz choices
        ChoiceListAdapter adapter = new ChoiceListAdapter(activity.getApplicationContext());
        adapter.receiveQuiz(currentQuiz);
        activity.views.choicesList.setAdapter(adapter);
        // choice selected event
        activity.views.choicesList.setOnItemClickListener(new ListView.OnItemClickListener() {
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
        activity.views.quizDisplay.setVisibility(View.GONE);
        activity.views.choicesList.setVisibility(View.GONE);
        activity.views.fab.setVisibility(View.VISIBLE);

        return this;
    }

}
