package com.example.lex.huiseten;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.checked;
import static android.R.attr.visible;

public class EatDataSetter extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;

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

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("signed in", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("signed out", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void setUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        username = user.getDisplayName();
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

    public void onSubmit(View view){
        setUsername();
        setComments();

        if (eating == null) {
            Toast.makeText(EatDataSetter.this, "Chose if you are eating at home or not",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            if (hasComments == false) {
                addToDB();
            }
            else {
                if (comments == "") {
                    Toast.makeText(EatDataSetter.this, "fill in your comments",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    addToDB();
                }
            }
        }
    }

    public void addToDB() {
        EatData data = new EatData(username, eating, hasComments, comments);
        mDatabase.child("Superhuis").child(username).setValue(data);
        startEatListActivity();
        finish();
    }

    public void startEatListActivity() {
        Intent intent = new Intent(this, EatList.class);
        startActivity(intent);
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
}
