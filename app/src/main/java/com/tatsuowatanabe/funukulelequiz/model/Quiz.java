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
    private String body_ja;
    private String body_en;
    private String explanation_ja;
    private String explanation_en;
    private ArrayList<Choice> choices;
    private transient String lang;
    private transient Context context;

    /**
     * set the specified language of quiz.
     * @param lang
     * @return Quiz
     */
    public final Quiz setLang(String lang) {
        this.lang = lang;
        return this;
    }

    /**
     * get the language of quiz.
     * @return Quiz
     */
    public final String getLang() {
        return this.lang;
    }

    /**
     * set the context object
     * @param cont
     * @return
     */
    public final Quiz setContext(Context cont) {
        this.context = cont;
        return this;
    }

    /**
     * returns whether the quiz langage is Japanese or not.
     * @return
     */
    private final boolean isJapanese() {
        String ja = context.getString(R.string.lang_ja);
        return lang.equals(ja);
    }

    /**
     * returns whether the quiz langage is English or not.
     * @return
     */
    private final boolean isEnglish() {
        String en = context.getString(R.string.lang_en);
        return lang.equals(en);
    }

    /**
     * returns quiz body in specified language.
     * @return
     */
    public final String getBody() {
        final String body = this.isJapanese() ? body_ja :
                            this.isEnglish()  ? body_en : "";
        return body + "?";
    }

    /**
     * returns quiz explanation in specified language.
     * @return
     */
    public final String getExplanation() {
        final String explanation = this.isJapanese() ? explanation_ja :
                                   this.isEnglish()  ? explanation_en : "";
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
        private String  body_ja;
        private String  body_en;
        private Integer point;
        private transient String  lang;
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
