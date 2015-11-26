package com.tatsuowatanabe.funukulelequiz.Model;

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

}
