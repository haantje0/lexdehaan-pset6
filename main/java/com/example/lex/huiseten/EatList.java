package com.example.lex.huiseten;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;



public class EatList extends AppCompatActivity {

    FirebaseManager fbManager = new FirebaseManager();
    DatabaseManager dbManager = new DatabaseManager();

    ListView eatList;

    EatList activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eatlist);

        fbManager.setInstance();

        fbManager.setListener();

        dbManager.setDatabase();

        eatList = (ListView) findViewById(R.id.eatData_ListView);

        dbManager.getFromDB(this, eatList);
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

    public void IWantToEatButton(View view){
        Intent intent = new Intent(activity, EatDataSetter.class);
        startActivity(intent);
    }
}
