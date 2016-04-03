package com.tatsuowatanabe.funukulelequiz.model;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;

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
     * returns quiz of choices list.
     * @return
     */
    public final ArrayList<Choice> getChoices() {
        return choices;
    }

    public void shuffleChoices() {
        // TODO: shuffle the choice.
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
        private String  lang;
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
         * returns choice content body in language that Quiz has.
         * @return String
         */
        public final String getBody() { return getBody(lang); }

        /**
         * returns body of choice in specified language.
         * @param specifiedLang
         * @return body of choice
         */
        public final String getBody(String specifiedLang) {
            final String body = specifiedLang.equals("ja") ? body_ja :
                                specifiedLang.equals("en") ? body_en : "";
            return body;
        }
    }
}
