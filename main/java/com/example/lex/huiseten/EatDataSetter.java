package com.example.lex.huiseten;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Objects;

public class EatDataSetter extends AppCompatActivity {

    // set firebasemanager and databasemanager
    FirebaseManager fbManager = new FirebaseManager();
    DatabaseManager dbManager = new DatabaseManager();

    // set the edittexts
    EditText comments_EditText;
    CheckBox comments_CheckBox;

    // set some valeus
    String username;
    Boolean eating;
    Boolean hasComments = false;
    String comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_data_setter);

        // retrieve the saved instance
        if (savedInstanceState != null) {
            setInstaneState(savedInstanceState);
        }

        // set the firebase instance
        fbManager.setInstance();

        // set the firebase listener
        fbManager.setListener();

        // set the firebase database
        dbManager.setDatabase();
    }

    // set the current username
    public void setUsername() {
        username = fbManager.getUsername();
    }

    // set the given comments
    public void setComments() {
        comments_EditText = (EditText) findViewById(R.id.comments_EditText);
        comments = comments_EditText.getText().toString();
    }

    // set the eating radio button
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.Yes_RadioButton:
                if (checked)
                    eating = true;
                    break;
            case R.id.No_RadioButton:
                if (checked)
                    eating = false;
                    break;
        }
    }

    // set the has comments checkbox
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        comments_EditText = (EditText) findViewById(R.id.comments_EditText);

        if (checked){
            hasComments = true;
            comments_EditText.setVisibility(view.VISIBLE);
        }
        else {
            hasComments = false;
            comments_EditText.setVisibility(view.GONE);
        }
    }

    // submit the data
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onSubmit(View view){
        // set the username and comments
        setUsername();
        setComments();

        // check if the user chose an option
        if (eating == null) {
            Toast.makeText(EatDataSetter.this, "Chose if you are eating at home or not",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            // send the data without comments
            if (hasComments == false) {
                EatData eatData = new EatData(username, eating);
                dbManager.addToDB(EatDataSetter.this, eatData);
            } else {
                // check if the user filled in some comments
                if (Objects.equals(comments, "")) {
                    Toast.makeText(EatDataSetter.this, "fill in your comments",
                            Toast.LENGTH_SHORT).show();
                }
                // send the data with comments
                else {
                    EatData eatData = new EatData(username, eating, comments);
                    dbManager.addToDB(EatDataSetter.this, eatData);                }
            }
        }
    }

    // save the instance state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            outState.putBoolean("eating", eating);
        } catch (RuntimeException e){
            e.printStackTrace();
        }

        outState.putBoolean("hasComments", hasComments);

        setComments();
        outState.putString("comments", comments);

        super.onSaveInstanceState(outState);
    }

    //set the instance state
    public void setInstaneState(Bundle savedInstanceState) {
        // set the text to the saved state
        try {
            eating = savedInstanceState.getBoolean("eating");
        } catch (RuntimeException e){
            e.printStackTrace();
        }

        setComments();
        String savedComments = savedInstanceState.getString("comments");
        comments_EditText.setText(savedComments);

        comments_CheckBox = (CheckBox) findViewById(R.id.comments_CheckBox);
        if (savedInstanceState.getBoolean("hasComments")) {
            hasComments = true;
            comments_EditText.setVisibility(View.VISIBLE);
        }
    }
}
