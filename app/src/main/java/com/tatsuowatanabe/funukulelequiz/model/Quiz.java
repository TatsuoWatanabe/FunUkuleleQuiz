package com.tatsuowatanabe.funukulelequiz.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tatsuowatanabe.funukulelequiz.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by tatsuo on 11/25/15.
 * Quiz Object Class.
 */
public class Quiz {
    private String lang;
    private String body_ja;
    private String body_en;
    private String explanation_ja;
    private String explanation_en;
    private ArrayList<Choice> choices;

    /**
     * returns specified language of quiz.
     * @return language
     */
    private final String getLang() {
        return this.lang;
    }

    /**
     * set the specified language of quiz.
     * @param lang
     * @return Quiz
     */
    public final Quiz setLang(String lang) {
        this.lang = lang;
        for(Choice choice: choices) {
            choice.setLang(lang);
        }
        return this;
    }

    /**
     * returns quiz body in specified language.
     * @return
     */
    public final String getBody() {
        final String body = lang.equals("ja") ? body_ja :
                            lang.equals("en") ? body_en : "";
        return body + "?";
    }

    /**
     * returns quiz explanation in specified language.
     * @return
     */
    public final String getExplanation() {
        final String explanation = lang.equals("ja") ? explanation_ja :
                                   lang.equals("en") ? explanation_en : "";
        return explanation;
    }

    /**
     * returns quiz of choices list.
     * @return
     */
    public final ArrayList<Choice> getChoices() {
        return choices;
    }

    /**
     * shuffleChoices the choices.
     * @return
     */
    public Quiz shuffleChoices() {
        Collections.shuffle(choices);
        return this;
    }

    @Override
    public String toString() {
        final Gson gson = new Gson();
        return TextUtils.join("\n", gson.toJson(this).split(","));
    }

    /**
     * inner class for choices of Quiz.
     */
    public final class Choice {
        private transient String  lang;
        private String  body_ja;
        private String  body_en;
        private Integer point;

        /**
         * set the specified language of quiz.
         * @param lang
         * @return Choice
         */
        public final Choice setLang(String lang) {
            this.lang = lang;
            return this;
        }

        /**
         * get the choice's point
         * @return
         */
        public final Integer getPoint() {
            return this.point;
        }

        /**
         * returns body of choice in specified language.
         * @return body of choice
         */
        public final String getBody(Context context) {
            final String ja   = context.getString(R.string.lang_ja);
            final String en   = context.getString(R.string.lang_en);
            final String body = lang.equals(ja) ? body_ja :
                                lang.equals(en) ? body_en : "";
            return body;
        }
    }
}
