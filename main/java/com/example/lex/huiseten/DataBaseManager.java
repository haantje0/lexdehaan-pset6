package com.example.lex.huiseten;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ListView;

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

class DatabaseManager {

    private DatabaseReference mDatabase;

    // set database instance
    public void setDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // get information form the database
    public void getFromDB(final Context context, final ListView eatList) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // make the EatData arraylist
                ArrayList<EatData> eatArrayList = new ArrayList<EatData>();

                // get the right data and stor it in the arraylist
                for (DataSnapshot house : dataSnapshot.getChildren()) {
                    for (DataSnapshot person : house.getChildren()){
                        eatArrayList.add(person.getValue(EatData.class));
                    }
                }

                // sort the arraylist with eating people first
                Collections.sort(eatArrayList, new Comparator<EatData>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public int compare(EatData ed1, EatData ed2) {
                        return Boolean.compare(ed2.getEating(), ed1.getEating());
                    }
                });

                // inflate the eatlist in the listview
                EatListAdapter eatListAdapter = new EatListAdapter(context, eatArrayList);
                eatList.setAdapter(eatListAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }};
        mDatabase.addValueEventListener(postListener);
    }

    // add values to the database
    public void addToDB(Context context, EatData eatData) {
        mDatabase.child("Superhuis").child(eatData.getUsername()).setValue(eatData);

        // go back to the eatlist
        Intent intent = new Intent(context, EatList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

}
