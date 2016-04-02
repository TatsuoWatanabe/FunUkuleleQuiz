package com.tatsuowatanabe.funukulelequiz.model;

import android.util.Log;
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
     * add the point.
     * @param additionalPoint
     * @return int
     */
    public int addPoint(int additionalPoint) {
        point += additionalPoint;
        display();
        return point;
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

        TextView pointDisplay = (TextView)activity.findViewById(R.id.point_display);
        pointDisplay.setText(String.valueOf(point) + " pt");
        return this;
    }

}
