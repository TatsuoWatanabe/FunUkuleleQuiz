package com.tatsuowatanabe.funukulelequiz.models;

/**
 * Created by tatsuo on 11/26/15.
 * Api Url Management Class.
 */
public class QuizApiUrl {
    public static final String url = "http://mongoquizserver.herokuapp.com/api/";

    public static String url(int limit) {
        return url + "?limit=" + String.valueOf(limit);
    }
}
