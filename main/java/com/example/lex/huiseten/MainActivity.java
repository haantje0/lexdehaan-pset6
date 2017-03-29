package com.example.lex.huiseten;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    // set the firebase manager
    FirebaseManager fbManager = new FirebaseManager();

    // set email and password
    String email;
    String password;

    // set edittexts
    EditText email_EditText;
    EditText password_EditText;

    // set userdata
    UserData userData = new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // retrieve saved instance state
        if (savedInstanceState != null) {
            setInstanceState(savedInstanceState);
        }

        // set firebase instance
        fbManager.setInstance();

        // set firebase listener
        fbManager.setListener();
    }

    // go to register screen
    public void register(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    // try to logg in the user
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void logIn(View view) {

        // set the email and password
        setEmail();
        setPassword();

        // check if an email and a password are given
        if (Objects.equals(email, "")) {
            Toast.makeText(MainActivity.this, "Fill in your email",
                    Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(password, "")) {
            Toast.makeText(MainActivity.this, "Fill in your password",
                    Toast.LENGTH_SHORT).show();
        } else {
            // check and login
            fbManager.checkAndLogIn(this, userData);
        }
    }

    // set email
    public void setEmail() {
        email_EditText = (EditText) findViewById(R.id.email_editText);
        email = email_EditText.getText().toString();
        userData.setEmail(email);
    }

    // set password
    public void setPassword() {
        password_EditText = (EditText) findViewById(R.id.password_editText);
        password = password_EditText.getText().toString();
        userData.setPassword(password);
    }

    // save the instance state
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // save the edittexts
        setEmail();
        outState.putString("email", userData.getEmail());
        setPassword();
        outState.putString("password", userData.getPassword());

        super.onSaveInstanceState(outState);
    }

    // set the instance state
    public void setInstanceState(Bundle savedInstanceState) {
        // set the text to the saved state
        setEmail();
        String savedEmail = savedInstanceState.getString("email");
        email_EditText.setText(savedEmail);

        setPassword();
        String savedPassword = savedInstanceState.getString("password");
        password_EditText.setText(savedPassword);
    }
}
