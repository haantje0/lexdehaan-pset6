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

        // put the username in the view
        String username = getItem(position).getUsername();
        TextView usernameTextView = (TextView) theView.findViewById(R.id.usernameTextView);
        usernameTextView.setText(username);

        // set if the user is eating or not
        Boolean eating = getItem(position).getEating();
        ImageView theImageView = (ImageView) theView.findViewById(R.id.CollorIndicator_ImageView);

        // change the collor if the user is eating or not
        if (eating){
            theImageView.setImageResource(R.color.Green);
        }
        else {
            theImageView.setImageResource(R.color.Red);
        }

        // set if the user has comments
        Boolean hasComments = getItem(position).getHasComments();
        String comments = getItem(position).getComments();
        TextView CommentsTextView = (TextView) theView.findViewById(R.id.commentsTextView);

        // set the comments
        if (hasComments){
            CommentsTextView.setText(comments);
        }

        return theView;
    }
}
