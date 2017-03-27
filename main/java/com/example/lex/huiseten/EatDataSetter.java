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

    FirebaseManager fbManager = new FirebaseManager();
    DatabaseManager dbManager = new DatabaseManager();

    EditText comments_EditText;
    CheckBox comments_CheckBox;

    String username;
    Boolean eating;
    Boolean hasComments = false;
    String comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_data_setter);

        if (savedInstanceState != null) {
            setInstaneState(savedInstanceState);
        }

        fbManager.setInstance();

        fbManager.setListener();

        dbManager.setDatabase();
    }

    public void setUsername() {
        username = fbManager.getUsername();
    }

    public void setComments() {
        comments_EditText = (EditText) findViewById(R.id.comments_EditText);
        comments = comments_EditText.getText().toString();
    }

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onSubmit(View view){
        setUsername();
        setComments();

        if (eating == null) {
            Toast.makeText(EatDataSetter.this, "Chose if you are eating at home or not",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            if (hasComments == false) {
                EatData eatData = new EatData(username, eating);
                dbManager.addToDB(EatDataSetter.this, eatData);
            }
            else {
                if (Objects.equals(comments, "")) {
                    Toast.makeText(EatDataSetter.this, "fill in your comments",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    EatData eatData = new EatData(username, eating, comments);
                    dbManager.addToDB(EatDataSetter.this, eatData);                }
            }
        }
    }

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
