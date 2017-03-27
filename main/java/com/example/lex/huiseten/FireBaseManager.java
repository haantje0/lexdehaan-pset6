package com.example.lex.huiseten;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by lex on 3/27/2017.
 */

class FirebaseManager {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;

    public void onStart() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void setInstance() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void setListener() {
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
                }}};
    }

    public void setUsername(final Context context, UserData userData) {
        FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userData.getUsername())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("username created", "User profile updated.");
                            Intent intent = new Intent(context, EatList.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }}});
    }

    public String getUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getDisplayName();
    }

    public void checkAndLogIn(final Context context, final UserData userData) {
        mAuth.signInWithEmailAndPassword(userData.getEmail(), userData.getPassword())
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Sign in", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("email", "signInWithEmail", task.getException());
                            Toast.makeText(context, "Email and password do not match",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Logged in user",
                                    Toast.LENGTH_SHORT).show();
                            if (userData.getIsnew()) {
                                setUsername(context, userData);
                            } else {
                                Intent intent = new Intent(context, EatList.class);
                                context.startActivity(intent);
                            }}}});
    }

    public void createUser(final Context context, final UserData userData) {
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(userData.getEmail(), userData.getPassword())
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("create user", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "fill in a valid emailadres",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Created user",
                                    Toast.LENGTH_SHORT).show();
                            checkAndLogIn(context, userData);
                        }}});
    }
}
