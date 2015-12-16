package com.tatsuowatanabe.funukulelequiz.Model;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by tatsuo on 11/25/15.
 * Quiz Object Class.
 */
public class Quiz {
    public String body_ja;
    public String body_en;
    public String explanation_ja;
    public String explanation_en;
    public ArrayList<Choice> choices;

    private final class Choice {
        public String body_ja;
        public String body_en;
        public Integer point;
    }

    public void shuffleChoices() {
        // TODO: shuffle the choice.
    }

    @Override
    public String toString() {
        final Gson gson = new Gson();
        return TextUtils.join("\n", gson.toJson(this).split(","));
    }

}
