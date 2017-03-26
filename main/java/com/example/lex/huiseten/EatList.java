package com.example.lex.huiseten;

import android.content.Intent;
import android.os.Build;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EatList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;

    ListView eatList;
    EatListAdapter eatListAdapter;

    EatList activity = this;

    ArrayList<EatData> eatArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eatlist);

        mAuth = FirebaseAuth.getInstance();
        setListener();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getFromDB();
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

    public void setListener(){
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
                    goToLoginActivity();
                }
                // ...
            }
        };
    }

    public void goToLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void IWantToEatButton(View view){
        Intent intent = new Intent(activity, EatDataSetter.class);
        startActivity(intent);
    }

    public void getFromDB() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Object data = dataSnapshot.child("Superhuis").getValue();
                eatArrayList = new ArrayList<EatData>();

                EatData User = null;
                for (DataSnapshot house : dataSnapshot.getChildren()) {
                    for (DataSnapshot person : house.getChildren()){
                        User = person.getValue(EatData.class);

                        eatArrayList.add(User);
                    }
                }

                Collections.sort(eatArrayList, new Comparator<EatData>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public int compare(EatData ed1, EatData ed2) {
                                return Boolean.compare(ed2.getEating(), ed1.getEating());
                            }
                        });

                eatListAdapter = new EatListAdapter(activity, eatArrayList);

                eatList = (ListView) findViewById(R.id.eatData_ListView);
                assert eatList != null;
                eatList.setAdapter(eatListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(postListener);
    }
}
