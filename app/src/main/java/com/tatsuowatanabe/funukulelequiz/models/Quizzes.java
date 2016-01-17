package com.tatsuowatanabe.funukulelequiz.models;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by tatsuo on 11/26/15.
 * Quiz Object Collection Class.
 */
public class Quizzes {
    public ArrayList<Quiz> quizzes;
    private static final Gson gson = new Gson();

    /**
     * factory method. create self instance from json.
     * @param json json from web api response.
     * @return returns the instance.
     */
    public static Quizzes fromJson(JSONArray json) {
        return gson.fromJson("{quizzes:" + json.toString() + "}", Quizzes.class);
    }

    /**
     * iterator of quizzes ArrayList.
     * @return
     */
    public Iterator<Quiz> iterator() {
        return quizzes.iterator();
    }

    @Override
    public String toString() {
        return TextUtils.join("\n", gson.toJson(quizzes).split(","));
    }

}
