package com.tatsuowatanabe.funukulelequiz.model;

import android.widget.TextView;

import com.tatsuowatanabe.funukulelequiz.MainActivity;
import com.tatsuowatanabe.funukulelequiz.R;

/**
 * Created by tatsuo on 3/28/16.
 * keep the results of quizzes.
 */
public class QuizResults {
    private int point = 0;
    private MainActivity activity;

    public QuizResults() { }

    /**
     * Add the point of selected choice.
     * @param selectedChoice
     * @return int
     */
    public QuizResults calcPoint(Quiz.Choice selectedChoice) {
        point += selectedChoice.getPoint();
        display();
        return this;
    }

    /**
     * reset the QuizResult
     * @return
     */
    public QuizResults reset() {
        point = 0;
        display();
        return this;
    }

    /**
     * set MainActivity for display the Quiz Results.
     * @param ac
     * @return QuizResults
     */
    public QuizResults setActivity(MainActivity ac) {
        activity = ac;
        return this;
    }

    public QuizResults display() {
        if (!(activity instanceof MainActivity)) { return this; }

        activity.vh.pointDisplay.setText(String.valueOf(point) + " pt");
        return this;
    }

}
