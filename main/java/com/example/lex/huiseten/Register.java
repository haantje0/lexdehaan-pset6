package com.example.lex.huiseten;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    FirebaseManager fbManager = new FirebaseManager();

    String passwordAgain;

    EditText email_EditText;
    EditText username_EditText;
    EditText password_EditText;
    EditText passwordAgain_EditText;

    UserData userData = new UserData();

    Register activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (savedInstanceState != null) {
            setInstanceState(savedInstanceState);
        }

        fbManager.setInstance();

        fbManager.setListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        fbManager.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        fbManager.onStop();
    }

    public void Register(View view) {
        setEmail();
        setUsername();
        setPassword();
        setPasswordAgain();

        if (!passwordAgain.equals(userData.getPassword())) {
            Toast.makeText(activity, "Passwords do not match",
                    Toast.LENGTH_SHORT).show();
            passwordAgain_EditText.getText().clear();
        } else if (userData.getPassword().length() < 6) {
            Toast.makeText(activity, "Password not long enough",
                    Toast.LENGTH_SHORT).show();
            password_EditText.getText().clear();
            passwordAgain_EditText.getText().clear();
        } else if (userData.getUsername() == null) {
            Toast.makeText(activity, "Fill in a first name",
                    Toast.LENGTH_SHORT).show();
        } else {
            fbManager.createUser(activity, userData);
        }
    }

    public void setEmail() {
        email_EditText = (EditText) findViewById(R.id.email_editText);
        userData.setEmail(email_EditText.getText().toString());
    }

    public void setUsername() {
        username_EditText = (EditText) findViewById(R.id.username_editText);
        userData.setUsername(username_EditText.getText().toString());
    }

    public void setPassword() {
        password_EditText = (EditText) findViewById(R.id.password_editText);
        userData.setPassword(password_EditText.getText().toString());
    }

    public void setPasswordAgain() {
        passwordAgain_EditText = (EditText) findViewById(R.id.password_again_editText);
        passwordAgain = passwordAgain_EditText.getText().toString();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // save the edittexts
        setEmail();
        outState.putString("email", userData.getEmail());
        setPassword();
        outState.putString("password", userData.getPassword());
        setPasswordAgain();
        outState.putString("passwordAgain", passwordAgain);

        super.onSaveInstanceState(outState);
    }

    protected void setInstanceState(Bundle savedInstanceState) {
        // set the text to the saved state
        setEmail();
        String savedEmail = savedInstanceState.getString("email");
        email_EditText.setText(savedEmail);

        setPassword();
        String savedPassword = savedInstanceState.getString("password");
        password_EditText.setText(savedPassword);

        setPasswordAgain();
        String savedPasswordAgain = savedInstanceState.getString("passwordAgain");
        passwordAgain_EditText.setText(savedPasswordAgain);
    }
}

