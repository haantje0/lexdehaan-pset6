package com.example.lex.huiseten;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String email;
    String username;
    String password;
    String passwordAgain;

    EditText email_EditText;
    EditText username_EditText;
    EditText password_EditText;
    EditText passwordAgain_EditText;

    Register activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (savedInstanceState != null) {
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

        mAuth = FirebaseAuth.getInstance();

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

    public void Register(View view) {
        setEmail();
        setUsername();
        setPassword();
        setPasswordAgain();

        password_EditText.getText().clear();

        final Register activity = this;

        if (!passwordAgain.equals(password)) {
            Toast.makeText(Register.this, "Passwords do not match",
                    Toast.LENGTH_SHORT).show();
            passwordAgain_EditText.getText().clear();
        }
        else if (password.length() < 6) {
            Toast.makeText(Register.this, "Password not long enough",
                    Toast.LENGTH_SHORT).show();
            password_EditText.getText().clear();
            passwordAgain_EditText.getText().clear();
        }
        else if (username == null) {
            Toast.makeText(Register.this, "Fill in a first name",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            createUser();
        }
    }

    public void createUser() {
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("create user", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "fill in a valid emailadres",
                                    Toast.LENGTH_SHORT).show();
                            email_EditText.getText().clear();

                        } else {
                            Toast.makeText(Register.this, "Created user: " + email,
                                    Toast.LENGTH_SHORT).show();
                            logIn();
                        }
                    }
                });
    }

    public void setEmail() {
        email_EditText = (EditText) findViewById(R.id.email_editText);
        email = email_EditText.getText().toString();
    }

    public void setUsername() {
        username_EditText = (EditText) findViewById(R.id.username_editText);
        username = username_EditText.getText().toString();
    }

    public void setPassword() {
        password_EditText = (EditText) findViewById(R.id.password_editText);
        password = password_EditText.getText().toString();
    }

    public void setPasswordAgain() {
        passwordAgain_EditText = (EditText) findViewById(R.id.password_again_editText);
        passwordAgain = passwordAgain_EditText.getText().toString();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // save the edittexts
        setEmail();
        outState.putString("email", email);
        setPassword();
        outState.putString("password", password);
        setPasswordAgain();
        outState.putString("passwordAgain", passwordAgain);

        super.onSaveInstanceState(outState);
    }

    public void giveUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("username created", "User profile updated.");
                            goToEatListActivity();
                        }
                    }
                });
    }

    public void logIn() {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Sign in", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("email", "signInWithEmail", task.getException());
                            Toast.makeText(Register.this, "Email and password do not match",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Register.this, "Logged in user: " + email,
                                    Toast.LENGTH_SHORT).show();
                            giveUsername();
                        }
                    }
                });
    }

    public void goToEatListActivity() {
        Intent intent = new Intent(this, EatList.class);
        startActivity(intent);
        finish();
    }
}
