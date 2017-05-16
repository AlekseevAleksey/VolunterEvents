package com.glyuk.volunterevents.activity;



import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;


import com.glyuk.volunterevents.R;
import com.glyuk.volunterevents.adpter.RVAdapter;
import com.glyuk.volunterevents.model.ModelEvents;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    //private List<ModelEvents> events;
    private RecyclerView rv;
    private Realm mRealm;
    private static MainActivity instance;

    private static int id = 1;

    FloatingActionButton fab;
    private static ArrayList<ModelEvents> eventDetailsModelArrayList = new ArrayList<>();
    private RVAdapter rvAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRealm = Realm.getInstance(MainActivity.this);
        instance = this;

        rvAdapter = new RVAdapter(eventDetailsModelArrayList, MainActivity.this);

        rv=(RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.setAdapter(rvAdapter);

        //bindWidgetsWithEvents();
        getAllEvents();

        //rv.setAdapter(adapter);

        //initializeAdapter();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdd();
            }
        });
    }

    public static MainActivity getInstance() {
        return instance;
    }

    private void bindWidgetsWithEvents () {

    }

    private void getAllEvents() {
        RealmResults<ModelEvents> results = mRealm.where(ModelEvents.class).findAll();

        mRealm.beginTransaction();

        for (int i = 0; i < results.size(); i++) {
            eventDetailsModelArrayList.add(results.get(i));
        }

        if(results.size()>0)
            id = mRealm.where(ModelEvents.class).max("id").intValue() + 1;
        mRealm.commitTransaction();
        rvAdapter.notifyDataSetChanged();
    }

    public void openAdd() {

        Intent intent=new Intent(this, CreateNewEvent.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ModelEvents searchPerson(int personId) {
        RealmResults<ModelEvents> results = mRealm.where(ModelEvents.class).equalTo("id", personId).findAll();

        mRealm.beginTransaction();
        mRealm.commitTransaction();

        return results.get(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventDetailsModelArrayList.clear();
        mRealm.close();
    }


}
