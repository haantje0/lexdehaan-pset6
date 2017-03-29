package com.example.lex.huiseten;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class Register extends AppCompatActivity {

    FirebaseManager fbManager = new FirebaseManager();

    String username;
    String email;
    String password;
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

        // retrieve the saved instance
        if (savedInstanceState != null) {
            setInstanceState(savedInstanceState);
        }

        // set firebase instance
        fbManager.setInstance();

        // set firebase listener
        fbManager.setListener();
    }

    // register the new user
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void Register(View view) {
        // set the given data
        setEmail();
        setUsername();
        setPassword();
        setPasswordAgain();

        // check if everything is filled in correctly
        if (!passwordAgain.equals(password)) {
            Toast.makeText(activity, "Passwords do not match",
                    Toast.LENGTH_SHORT).show();
            passwordAgain_EditText.getText().clear();
        } else if (password.length() < 6) {
            Toast.makeText(activity, "Password not long enough",
                    Toast.LENGTH_SHORT).show();
            password_EditText.getText().clear();
            passwordAgain_EditText.getText().clear();
        } else if (Objects.equals(username, "")) {
            Toast.makeText(activity, "Fill in a first name",
                    Toast.LENGTH_SHORT).show();
        } else {
            // create the new user
            userData.setIsnew(true);
            fbManager.createUser(activity, userData);
        }
    }

    // set the given email
    public void setEmail() {
        email_EditText = (EditText) findViewById(R.id.email_editText);
        email = email_EditText.getText().toString();
        userData.setEmail(email);
    }

    // set the given username
    public void setUsername() {
        username_EditText = (EditText) findViewById(R.id.username_editText);
        username = username_EditText.getText().toString();
        userData.setUsername(username);
    }

    // set the give password
    public void setPassword() {
        password_EditText = (EditText) findViewById(R.id.password_editText);
        password = password_EditText.getText().toString();
        userData.setPassword(password);
    }

    // set the given seccond password
    public void setPasswordAgain() {
        passwordAgain_EditText = (EditText) findViewById(R.id.password_again_editText);
        passwordAgain = passwordAgain_EditText.getText().toString();
    }

    // save the instance state
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

    // set the instance state
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

