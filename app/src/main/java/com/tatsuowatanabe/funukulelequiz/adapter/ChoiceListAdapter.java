package com.tatsuowatanabe.funukulelequiz.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tatsuowatanabe.funukulelequiz.R;
import com.tatsuowatanabe.funukulelequiz.model.Quiz;

/**
 * Created by tatsuo on 12/22/15.
 * @see http://qiita.com/yu_eguchi/items/65311af1c9fc0bff0cb0
 */
public class ChoiceListAdapter extends ArrayAdapter<Quiz.Choice>  {
    LayoutInflater mInflater;
    PackageManager packageManager;

    public ChoiceListAdapter(Context context) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
        packageManager = context.getPackageManager();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_list_item_choice, parent, false);
        }

        Quiz.Choice choice = getItem(position);

        TextView tv = (TextView) convertView.findViewById(R.id.title);
        tv.setText(choice.getBody());

        tv = (TextView) convertView.findViewById(R.id.sub);
        tv.setText(choice.getBody("en"));

        // ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
        // iv.setImageDrawable(convertView.getResources().getDrawable(android.R.drawable.star_on));
        // iv.setImageDrawable(convertView.applicationInfo.loadIcon(packageManager));

        return convertView;
    }
}
