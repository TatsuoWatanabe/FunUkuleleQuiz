package com.tatsuowatanabe.funukulelequiz.model;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.tatsuowatanabe.funukulelequiz.MainActivity;
import com.tatsuowatanabe.funukulelequiz.R;
import com.tatsuowatanabe.funukulelequiz.effect.FlashColor;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by tatsuo on 3/28/16.
 * keep the results of quizzes.
 */
public class QuizResults {
    private MainActivity activity;
    private Integer point = 0;
    private ArrayList<AnsweredQuiz> answeredes = new ArrayList<>();
    private String explanation_ja   = "";
    private String explanation_en   = "";
    private String resultMessage_ja = "";
    private String resultMessage_en = "";

    public QuizResults() { }

    /**
     * Add the point of selected choice.
     * @param pointOfOneAnswer
     * @return int
     */
    private QuizResults addPoint(Integer pointOfOneAnswer) {
        point += pointOfOneAnswer;
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
        AnsweredQuiz answeredQuiz = new AnsweredQuiz(quiz, selectedChoice.getPoint());

        if (answeredQuiz.isCorrect()) {
            showColorEffect(ContextCompat.getColor(activity, R.color.correct));
        } else {
            showColorEffect(ContextCompat.getColor(activity, R.color.incorrect));
            if (activity.prefs.shouldUseVibration()) { activity.getVibrator().vibrate(50); }
        }

        addPoint(answeredQuiz.getPoint());
        answeredes.add(answeredQuiz);
        return this;
    }

    /**
     * Show temporary color effect.
     * @param effectColor
     * @return
     */
    @Contract(pure = true)
    private QuizResults showColorEffect(final Integer effectColor) {
        if (!activity.prefs.shouldUseColorEffect()) { return this; }

        final Integer contentMainColor = ContextCompat.getColor(activity, R.color.contentMain);
        final Integer duration         = 500;
        FlashColor.show(activity.views.contentMain, contentMainColor, effectColor, duration);
        return this;
    }

    /**
     * reset the QuizResult
     * @return
     */
    public QuizResults reset() {
        answeredes.clear();
        point            = 0;
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
        String formattedPoints = activity.getString(R.string.points, point);
        Log.d("formatted points: ", formattedPoints);
        activity.views.pointDisplay.setText(formattedPoints);
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

        for (AnsweredQuiz q : answeredes) {
            if (q.isIncorrect()) { incorrects.add(q); }
        }

        if (incorrects.size() == 0) {
            this.explanation_ja = activity.getString(R.string.msg_perfect_ja);
            this.explanation_en = activity.getString(R.string.msg_perfect_en);
        } else {
            Collections.shuffle(incorrects);
            Quiz someQuiz = incorrects.get(0).quiz;
            this.explanation_ja = activity.getString(R.string.msg_explanation_ja, someQuiz.setLang(ja).getExplanation());
            this.explanation_en = activity.getString(R.string.msg_explanation_en, someQuiz.setLang(en).getExplanation());
        }

        this.resultMessage_ja = activity.getString(R.string.msg_result_message_ja, point);
        this.resultMessage_en = activity.getString(R.string.msg_result_message_en, point);

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

        activity.views.resultMessage.setText(resultMessage);
        activity.views.explanation.setText(explanation);

        return this;
    }

    /**
     * hide the result area.
     * @return
     */
    public QuizResults hideResultArea() {
        activity.views.resultArea.setVisibility(View.GONE);
        return this;
    }

    /**
     * show the result area.
     * @return
     */
    public QuizResults showResultArea() {
        activity.views.resultArea.setVisibility(View.VISIBLE);
        return this;
    }

    private class AnsweredQuiz {
        private Quiz    quiz;
        private Integer point;

        protected AnsweredQuiz(Quiz q, Integer p) {
            this.quiz  = q;
            this.point = p;
        }

        /**
         * get the result point.
         * @return
         */
        protected Integer getPoint() {
            return point;
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
