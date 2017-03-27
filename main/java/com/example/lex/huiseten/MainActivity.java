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

    FirebaseManager fbManager = new FirebaseManager();

    String email;
    String password;

    EditText email_EditText;
    EditText password_EditText;

    UserData userData = new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            setInstanceState(savedInstanceState);
        }

        fbManager.setInstance();

        fbManager.setListener();
    }

    public void register(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void logIn(View view) {

        setEmail();
        setPassword();

        if (Objects.equals(email, "")) {
            Toast.makeText(MainActivity.this, "Fill in your email",
                    Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(password, "")) {
            Toast.makeText(MainActivity.this, "Fill in your password",
                    Toast.LENGTH_SHORT).show();
        } else {
            fbManager.checkAndLogIn(this, userData);
        }
    }

    public void setEmail() {
        email_EditText = (EditText) findViewById(R.id.email_editText);
        email = email_EditText.getText().toString();
        userData.setEmail(email);
    }

    public void setPassword() {
        password_EditText = (EditText) findViewById(R.id.password_editText);
        password = password_EditText.getText().toString();
        userData.setPassword(password);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // save the edittexts
        setEmail();
        outState.putString("email", userData.getEmail());
        setPassword();
        outState.putString("password", userData.getPassword());

        super.onSaveInstanceState(outState);
    }

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
