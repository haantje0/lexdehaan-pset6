package com.example.lex.huiseten;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;



public class EatList extends AppCompatActivity {

    // set firebase and database managers
    FirebaseManager fbManager = new FirebaseManager();
    DatabaseManager dbManager = new DatabaseManager();

    // set the listview
    ListView eatList;

    // set the activity
    EatList activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eatlist);

        // set firebase instance
        fbManager.setInstance();

        // set firebase listener
        fbManager.setListener();

        // set firebase database
        dbManager.setDatabase();

        // set the listview
        eatList = (ListView) findViewById(R.id.eatData_ListView);

        // get the information form the database and inflate it in the listview
        dbManager.getFromDB(this, eatList);
    }

    // go to EatDataSetter
    public void IWantToEatButton(View view){
        Intent intent = new Intent(activity, EatDataSetter.class);
        startActivity(intent);
    }
}
