package com.tatsuowatanabe.funukulelequiz.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tatsuowatanabe.funukulelequiz.R;
import com.tatsuowatanabe.funukulelequiz.model.Quiz;

/**
 * Created by tatsuo on 12/22/15.
 * @see ://qiita.com/yu_eguchi/items/65311af1c9fc0bff0cb0
 */
public class ChoiceListAdapter extends ArrayAdapter<Quiz.Choice>  {
    LayoutInflater mInflater;
    PackageManager packageManager;

    public ChoiceListAdapter(Context context) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
        packageManager = context.getPackageManager();
    }

    /**
     * Receive the quiz object and set list to it's choices.
     * @param quiz
     * @return
     */
    public ChoiceListAdapter receiveQuiz(Quiz quiz, String lang) {
        for (Quiz.Choice choice: quiz.getChoices()) {
            this.add(choice.setLang(lang));
        }
        return this;
    }

    /**
     * Remove all elements from the list, and returns self.
     */
    public ChoiceListAdapter clearSelf() {
        clear();
        return this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_list_item_choice, parent, false);
        }

        Quiz.Choice choice = getItem(position);
        TextView tv = (TextView)convertView.findViewById(R.id.choice_body);
        tv.setText(choice.getBody(convertView.getContext()));

        // ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
        // iv.setImageDrawable(convertView.getResources().getDrawable(android.R.drawable.star_on));
        // iv.setImageDrawable(convertView.applicationInfo.loadIcon(packageManager));

        return convertView;
    }
}
