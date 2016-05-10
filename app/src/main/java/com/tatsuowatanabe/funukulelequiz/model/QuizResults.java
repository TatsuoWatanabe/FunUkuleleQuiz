package com.tatsuowatanabe.funukulelequiz.model;

import android.view.View;
import android.widget.TextView;

import com.tatsuowatanabe.funukulelequiz.MainActivity;
import com.tatsuowatanabe.funukulelequiz.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by tatsuo on 3/28/16.
 * keep the results of quizzes.
 */
public class QuizResults {
    private MainActivity activity;
    private Integer point = 0;
    private ArrayList<AnsweredQuiz> answered = new ArrayList<>();
    private String explanation_ja   = "";
    private String explanation_en   = "";
    private String resultMessage_ja = "";
    private String resultMessage_en = "";

    public QuizResults() { }

    /**
     * Add the point of selected choice.
     * @param selectedChoice
     * @return int
     */
    private QuizResults addPoint(Quiz.Choice selectedChoice) {
        point += selectedChoice.getPoint();
        showCurrentPoint();
        return this;
    }

    /**
     * receive user's answer.
     * @param selectedChoice
     * @param quiz
     * @return
     */
    public QuizResults receiveAnswer(Quiz.Choice selectedChoice, Quiz quiz) {
        addPoint(selectedChoice);
        answered.add(new AnsweredQuiz(quiz, selectedChoice.getPoint()));
        return this;
    }

    /**
     * reset the QuizResult
     * @return
     */
    public QuizResults reset() {
        point = 0;
        answered.clear();
        explanation_en   = "";
        explanation_ja   = "";
        resultMessage_en = "";
        resultMessage_ja = "";
        hideResultArea();
        showCurrentPoint();
        return this;
    }

    /**
     * set MainActivity for showCurrentPoint the Quiz Results.
     * @param ac
     * @return QuizResults
     */
    public QuizResults setActivity(MainActivity ac) {
        activity = ac;
        return this;
    }

    /**
     * show current point of one game.
     * @return
     */
    private QuizResults showCurrentPoint() {
        if (!(activity instanceof MainActivity)) { return this; }

        activity.vh.pointDisplay.setText(String.valueOf(point) + " pt");
        return this;
    }

    /**
     * generate explanation message and keep it as member variables.
     * @return
     */
    public QuizResults generateResultMessages() {
        ArrayList<AnsweredQuiz> incorrects = new ArrayList<>();
        String ja = activity.getString(R.string.lang_ja);
        String en = activity.getString(R.string.lang_en);

        for (AnsweredQuiz q : answered) {
            if (q.isIncorrect()) { incorrects.add(q); }
        }

        if (incorrects.size() == 0) {
            this.explanation_ja   = "すごい!パーフェクト！"; // TODO: literal to resource.
            this.explanation_en   = "perfect!";           // TODO: literal to resource.
        } else {
            Collections.shuffle(incorrects);
            Quiz someQuiz = incorrects.get(0).quiz;
            this.explanation_ja = "解説:\n"         + someQuiz.setLang(ja).getExplanation(); // TODO: literal to resource.
            this.explanation_en = "Explanation:\n" + someQuiz.setLang(en).getExplanation(); // TODO: literal to resource.
        }

        // TODO: Use the getString format resource.
        this.resultMessage_ja = "合計ポイントは "   + String.valueOf(point) + " ポイント でした!"; // TODO: literal to resource.
        this.resultMessage_en = "Total point is " + String.valueOf(point) + " pt!";            // TODO: literal to resource.

        return this;
    }

    /**
     * show result of one game.
     * @return
     */
    public QuizResults setMessageOf(String lang) {
        String ja = activity.getString(R.string.lang_ja);
        String en = activity.getString(R.string.lang_en);

        String resultMessage = lang.equals(ja) ? resultMessage_ja :
                               lang.equals(en) ? resultMessage_en : "";
        String explanation   = lang.equals(ja) ? explanation_ja   :
                               lang.equals(en) ? explanation_en   : "";

        setText(activity.vh.resultMessage, resultMessage);
        setText(activity.vh.explanation  , explanation);

        return this;
    }

    /**
     * hide the result area.
     * @return
     */
    public QuizResults hideResultArea() {
        activity.vh.resultArea.setVisibility(View.GONE);
        return this;
    }

    /**
     * show the result area.
     * @return
     */
    public QuizResults showResultArea() {
        activity.vh.resultArea.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * set text to TextView object on Activity.
     * @param tv
     * @param text
     * @return
     */
    private QuizResults setText(TextView tv, String text) {
        if (!(activity instanceof MainActivity)) { return this; }
        tv.setText(text);
        return this;
    }

    private class AnsweredQuiz {
        protected Quiz    quiz;
        protected Integer point;

        protected AnsweredQuiz(Quiz q, Integer p) {
            this.quiz  = q;
            this.point = p;
        }

        /**
         * returns whether answerd quiz is correct or not.
         * @return
         */
        protected boolean isCorrect() {
            return point > 0;
        }

        /**
         * returns whether answerd quiz is incorrect or not.
         * @return
         */
        protected boolean isIncorrect() {
            return !isCorrect();
        }
    }

}
