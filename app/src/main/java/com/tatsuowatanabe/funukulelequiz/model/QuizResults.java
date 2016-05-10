package com.tatsuowatanabe.funukulelequiz.model;

import android.view.View;
import android.widget.TextView;

import com.tatsuowatanabe.funukulelequiz.MainActivity;

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
        hideGameResult();
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

    private String getExplanation() {
        String explanation = "";
        ArrayList<AnsweredQuiz> incorrects = new ArrayList<>();

        for (AnsweredQuiz q : answered) {
            if (q.isIncorrect()) { incorrects.add(q); }
        }

        if (incorrects.size() == 0) {
            explanation = "すごい!パーフェクト！"; // TODO: literal to resource.
        } else {
            Collections.shuffle(incorrects);
            explanation = "解説:\n" + incorrects.get(0).quiz.getExplanation();
        }

        return explanation;
    }

    /**
     * show result of one game.
     * @return
     */
    public QuizResults showGameResult() {
        // TODO: literal to resource.
        setText(activity.vh.resultMessage, "合計ポイントは " + String.valueOf(point) + " ポイント でした!");
        setText(activity.vh.explanation  , getExplanation());
        activity.vh.resultArea.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * hide result of one game.
     * @return
     */
    public QuizResults hideGameResult() {
        activity.vh.resultArea.setVisibility(View.GONE);
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
