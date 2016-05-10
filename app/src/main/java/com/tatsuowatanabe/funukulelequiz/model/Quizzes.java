package com.tatsuowatanabe.funukulelequiz.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by tatsuo on 11/26/15.
 * Quiz Object Collection Class.
 */
public class Quizzes {
    private static final Gson gson = new Gson();
    public ArrayList<Quiz> quizArrayList;
    private transient Iterator<Quiz> quizIterator;
    /** @see ://www.javacreed.com/gson-annotations-example/ */
    private transient QuizResults results;
    private transient Quiz currentQuiz;

    /**
     * factory method. create self instance from json.
     * @param json json from web api response.
     * @return returns the instance.
     */
    public static Quizzes fromJson(JSONArray json) {
        Quizzes instance = gson.fromJson(
            "{" +
                "\"quizArrayList\": " + json.toString() +
            "}", Quizzes.class);
        return instance;
    }

    /**
     * Returns true if there is at least one more element, false otherwise.
     * @see #next
     */
    public boolean hasNext() {
        return getQuizIterator().hasNext();
    }

    /**
     * Returns the next object and advances the iterator.
     *
     * @return the next object.
     * @throws NoSuchElementException
     *             if there are no more elements.
     * @see #hasNext
     */
    public Quiz next() {
        currentQuiz = getQuizIterator().next();
        return currentQuiz;
    }

    /**
     * Returns the current Quiz.
     * @return
     */
    public Quiz current() {
        return currentQuiz;
    }

    /**
     * shuffle the inner choices.
     * @return
     */
    public Quizzes shuffleChoices() {
        for (int i = 0; i < quizArrayList.size(); i += 1) {
            quizArrayList.get(i).shuffleChoices();
        }
        return this;
    }

    /**
     * set the context to each quiz.
     * @return
     */
    public Quizzes setContext(Context cont) {
        for (int i = 0; i < quizArrayList.size(); i += 1) {
            quizArrayList.get(i).setContext(cont);
        }
        return this;
    }

    @Override
    public String toString() {
        return TextUtils.join("\n", gson.toJson(quizArrayList).split(","));
    }

    /**
     * get the QuizResults Object.
     * @return QuizResults
     */
    public QuizResults getResults() {
        results = (results == null) ? new QuizResults() : results;
        return results;
    }

    /**
     * Get the Iterator of Quiz Object.
     * @return
     */
    private Iterator<Quiz> getQuizIterator() {
        quizIterator = (quizIterator == null) ? quizArrayList.iterator() : quizIterator;
        return quizIterator;
    }
}
