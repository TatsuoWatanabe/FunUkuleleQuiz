package com.tatsuowatanabe.funukulelequiz.Model;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by tatsuo on 11/25/15.
 * Quiz Object Class.
 */
public class Quiz {
    private String body_ja;
    private String body_en;
    private String explanation_ja;
    private String explanation_en;
    private ArrayList<Choice> choices;

    /**
     * returns quiz body of specified language.
     * @param lang
     * @return
     */
    public final String getBody(String lang) {
        final String body = lang.equals("ja") ? body_ja :
                            lang.equals("en") ? body_en : body_ja;
        return body + "?";
    }

    /**
     * returns quiz of choices list.
     * @return
     */
    public final ArrayList<Choice> getChoices() {
        return choices;
    }

    public final class Choice {
        public String body_ja;
        public String body_en;
        public Integer point;

        public final String getBody(String lang) {
            final String body = lang.equals("ja") ? body_ja :
                                lang.equals("en") ? body_en : body_ja;
            return body;
        }
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
