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

    String username;

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
                }
            }
        };
    }

    public void setDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void setUsername(final Context context) {
        FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
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
                        }
                    }
                });
    }

    public String getUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getDisplayName();
    }

    public void checkAndLogIn(final Context context, String email, String password, final boolean firstTime) {
        mAuth.signInWithEmailAndPassword(email, password)
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
                            if (firstTime) {
                                setUsername(context);
                            } else {
                                Intent intent = new Intent(context, EatList.class);
                                context.startActivity(intent);
                            }
                        }
                    }
                });
    }

    public void createUser(final Context context, final String email, final String password, final String Username) {
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
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
                            username = Username;
                            checkAndLogIn(context, email, password, true);
                        }
                    }
                });
    }

    public void getFromDB(final Context context, final ListView eatList) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<EatData> eatArrayList = new ArrayList<EatData>();

                for (DataSnapshot house : dataSnapshot.getChildren()) {
                    for (DataSnapshot person : house.getChildren()){
                        eatArrayList.add(person.getValue(EatData.class));
                    }
                }

                Collections.sort(eatArrayList, new Comparator<EatData>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public int compare(EatData ed1, EatData ed2) {
                        return Boolean.compare(ed2.getEating(), ed1.getEating());
                    }
                });

                EatListAdapter eatListAdapter = new EatListAdapter(context, eatArrayList);

                eatList.setAdapter(eatListAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    public void addToDB(Context context, String username, Boolean eating, Boolean hasComments, String comments) {
        EatData data = new EatData(username, eating, hasComments, comments);
        mDatabase.child("Superhuis").child(username).setValue(data);

        Intent intent = new Intent(context, EatList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
