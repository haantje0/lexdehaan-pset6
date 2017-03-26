package com.example.lex.huiseten;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lex on 3/20/2017.
 */

public class EatListAdapter extends ArrayAdapter<EatData>{
    public EatListAdapter(Context context, List<EatData> eatdata) {
        super(context,R.layout.person_view, (List<EatData>) eatdata);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());
        View theView = theInflater.inflate(R.layout.person_view, parent, false);

        String username = getItem(position).getUsername();
        TextView usernameTextView = (TextView) theView.findViewById(R.id.usernameTextView);
        usernameTextView.setText(username);

        Boolean eating = getItem(position).getEating();
        ImageView theImageView = (ImageView) theView.findViewById(R.id.CollorIndicator_ImageView);

        if (eating){
            theImageView.setImageResource(R.color.Green);
        }
        else {
            theImageView.setImageResource(R.color.Red);
        }

        Boolean hasComments = getItem(position).getHasComments();
        String comments = getItem(position).getComments();
        TextView CommentsTextView = (TextView) theView.findViewById(R.id.commentsTextView);

        if (hasComments){
            CommentsTextView.setText(comments);
        }

        return theView;
    }
}
